package com.bairock.eleMonitor.comm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.Util;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.service.MsgManagerService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public ChannelHandlerContext channel;
	public long msgManagerId = 0;
	
	//是否已添加进map, 只有在第一次收到通信机id时将handler存入map, 之后此值为true
	private boolean mapAdded = false;
	
	/**
	 * 保存所有已连接的通信管理机的handler, 方便全局搜索通信机的handler,
	 * 只有收到通信管理机id时才保存, 键为通信机id, 值为handler
	 */
	public static Map<Long, ServerHandler> channelMap = new HashMap<>();

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName()); 
	
	private MsgManagerService msgManagerService = SpringUtil.getBean(MsgManagerService.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		channel = ctx;
		logger.info("new channel " + ctx.channel().id().asShortText());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		try {
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			logger.info(Util.bytesToHexString(req));
			int len = (req[0] << 8) | req[1];
			// 数据有效长度不包括长度字节数,通信管理机字节数和crc校验,长度字节数=2,通信管理机字节数=4, crc长度=2
			if (req.length != len + 8) {
				logger.error("长度不匹配");
				return;
			}
			int managerNum = (req[2] << 24) | (req[3] << 16) | (req[4] << 8) | req[5];
			MsgManager mm = msgManagerService.findByMsgManagerCode(managerNum);

			if(mm == null) {
				logger.error("通信管理机不存在-num:" + managerNum); 
				return;
			}
			msgManagerId = mm.getId();
			//添加进map, 方便全局搜索
			if(!mapAdded) {
				channelMap.put(msgManagerId, this);
			}
			byte[] by = new byte[len];
			by = Arrays.copyOfRange(req, 6, req.length);
			mm.handler(by);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			m.release();
			// ReferenceCountUtil.release(msg);
		}
	}
	
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

	public void send(byte[] by) {
		if(null != channel) {
			channel.writeAndFlush(Unpooled.copiedBuffer(by));
		}
	}
	
	/**
	 * 全局发送函数
	 * @param msgManagerId 通信机id
	 * @param by
	 */
	public static boolean send(long msgManagerId, byte[] by) {
		ServerHandler handler = channelMap.get(msgManagerId);
		if(null != handler) {
			handler.send(by);
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { 0x00, 0, 1, 0 };
		int managerNum = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
		System.out.println(managerNum + "?");
	}

}