package com.bairock.eleMonitor.comm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bairock.eleMonitor.SpringUtil;
import com.bairock.eleMonitor.Util;
import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.MsgManager;
import com.bairock.eleMonitor.data.webData.AnalysisReceivedErrorResult;
import com.bairock.eleMonitor.data.webData.NetMessageAnalysisResult;
import com.bairock.eleMonitor.data.webData.NetMessageSentResult;
import com.bairock.eleMonitor.enums.NetMessageResultEnum;
import com.bairock.eleMonitor.exception.NetMessageException;
import com.bairock.eleMonitor.service.MsgManagerService;
import com.bairock.eleMonitor.service.TestService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public ChannelHandlerContext channel;
	public long msgManagerId = 0;

	// 是否已添加进map, 只有在第一次收到通信机id时将handler存入map, 之后此值为true
	private boolean mapAdded = false;

	/**
	 * 保存所有已连接的通信管理机的handler, 方便全局搜索通信机的handler, 只有收到通信管理机id时才保存, 键为通信机id, 值为handler
	 */
	public static Map<Long, ServerHandler> channelMap = new HashMap<>();

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private MsgManagerService msgManagerService = SpringUtil.getBean(MsgManagerService.class);
	private TestService testService = SpringUtil.getBean(TestService.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		channel = ctx;
		logger.info("new channel " + ctx.channel().id().asShortText());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		NetMessageAnalysisResult result = new NetMessageAnalysisResult();
		try {
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			String strMsg = Util.bytesToHexString(req);
			result.setData(strMsg);
			logger.info(strMsg);
			
			int startOne = 0;
			while(startOne < req.length) {
				int len = req[startOne] << 8 | req[startOne + 1];
				//一个报文的结束位置
				int to = len + 8 + startOne;
				if(to > req.length) {
					//长度不匹配
					AnalysisReceivedErrorResult errResult = new AnalysisReceivedErrorResult();
					errResult.setCode(NetMessageResultEnum.ERR_LENGTH.getCode());
					errResult.setMessage(NetMessageResultEnum.ERR_LENGTH.getMessage());
					logger.error(NetMessageResultEnum.ERR_LENGTH.getMessage());
					result.addErrResult(errResult);
					testService.broadcastReceived(result);
					return;
				}
				byte[] byOne = Arrays.copyOfRange(req, startOne, to);
				AnalysisReceivedErrorResult errResult = analysisOneMsg(byOne);
				result.addErrResult(errResult);
				startOne = to;
			}
			
//			int len = (req[0] << 8) | req[1];
//			// 数据有效长度不包括长度字节数,通信管理机字节数和crc校验,长度字节数=2,通信管理机字节数=4, crc长度=2
//			if (req.length != len + 8) {
//				result.setCode(NetMessageResultEnum.ERR_LENGTH.getCode());
//				result.setMessage(NetMessageResultEnum.ERR_LENGTH.getMessage());
//				logger.error(NetMessageResultEnum.ERR_LENGTH.getMessage());
//				testService.broadcastReceived(result);
//				return;
//			}
//			int managerNum = (req[2] << 24) | (req[3] << 16) | (req[4] << 8) | req[5];
//			MsgManager mm = msgManagerService.findByMsgManagerCode(managerNum);
//
//			if (mm == null) {
//				result.setCode(NetMessageResultEnum.UNKNOW_MANAGER.getCode());
//				result.setMessage(NetMessageResultEnum.UNKNOW_MANAGER.getMessage() + managerNum);
//				logger.error(result.getMessage());
//				testService.broadcastReceived(result);
//				return;
//			}
//			msgManagerId = mm.getId();
//			// 添加进map, 方便全局搜索
//			if (!mapAdded) {
//				mapAdded = true;
//				channelMap.put(msgManagerId, this);
//			}
//			// 设置设备值监听器
//			for (Device dev : mm.findAllDevice()) {
//				if(dev.getOnValueListener() == null) {
//					dev.setOnValueListener(new MyOnValueListener());
//				}
//			}
//
//			byte[] by = new byte[len];
//			by = Arrays.copyOfRange(req, 6, req.length);
//			try {
//				byte[] byHead = Arrays.copyOfRange(req, 0, 6);
//				result.setHead(Util.bytesToHexString(byHead));
//				List<byte[]> list = mm.handler(by);
//				for(byte[] byOne : list) {
//					result.getListOne().add(Util.bytesToHexString(byOne));
//				}
//				testService.broadcastReceived(result);
//			}catch(NetMessageException e) {
////				e.printStackTrace();
//				result.setCode(e.getCode());
//				result.setMessage(e.getMessage());
//				logger.error(e.getMessage());
//				testService.broadcastReceived(result);
//			}catch (Exception e) {
//				e.printStackTrace();
//				result.setCode(NetMessageResultEnum.UNKNOW.getCode());
//				result.setMessage(e.getMessage());
//				logger.error(e.getMessage());
//				testService.broadcastReceived(result);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			AnalysisReceivedErrorResult errResult = new AnalysisReceivedErrorResult();
			errResult.setCode(NetMessageResultEnum.UNKNOW.getCode());
			errResult.setMessage(e.getMessage());
			result.addErrResult(errResult);
			
			logger.error(e.getMessage());
		} finally {
			m.release();
			// ReferenceCountUtil.release(msg);
		}
		testService.broadcastReceived(result);
	}
	
	private AnalysisReceivedErrorResult analysisOneMsg(byte[] byOne) {
		AnalysisReceivedErrorResult errResult = new AnalysisReceivedErrorResult();
		errResult.setData(Util.bytesToHexString(byOne));
		
		byte[] by = new byte[byOne.length - 6];
		by = Arrays.copyOfRange(byOne, 6, byOne.length);
		byte[] byHead = Arrays.copyOfRange(byOne, 0, 6);
		errResult.setHead(Util.bytesToHexString(byHead));
		
		int managerNum = (byOne[2] << 24) | (byOne[3] << 16) | (byOne[4] << 8) | byOne[5];
		MsgManager mm = msgManagerService.findByMsgManagerCode(managerNum);

		if (mm == null) {
			errResult.setCode(NetMessageResultEnum.UNKNOW_MANAGER.getCode());
			errResult.setMessage(NetMessageResultEnum.UNKNOW_MANAGER.getMessage() + managerNum);
			logger.error(errResult.getMessage());
			//testService.broadcastReceived(result);
			return errResult;
		}
		msgManagerId = mm.getId();
		// 添加进map, 方便全局搜索
		if (!mapAdded) {
			mapAdded = true;
			channelMap.put(msgManagerId, this);
		}
		// 设置设备值监听器
		for (Device dev : mm.findAllDevice()) {
			if(dev.getOnValueListener() == null) {
				dev.setOnValueListener(new MyOnValueListener());
			}
		}

		try {
			List<byte[]> list = mm.handler(by);
			for(byte[] by1 : list) {
				errResult.getListOne().add(Util.bytesToHexString(by1));
			}
			//testService.broadcastReceived(result);
		}catch(NetMessageException e) {
//			e.printStackTrace();
			errResult.setCode(e.getCode());
			errResult.setMessage(e.getMessage());
			logger.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			errResult.setCode(NetMessageResultEnum.UNKNOW.getCode());
			errResult.setMessage(e.getMessage());
			logger.error(e.getMessage());
		}
		return errResult;
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
		if (null != channel) {
			channel.writeAndFlush(Unpooled.copiedBuffer(by));
		}
	}

	/**
	 * 全局发送函数
	 * 
	 * @param msgManagerId 通信机id
	 * @param by
	 */
	public static NetMessageSentResult send(long msgManagerId, byte[] by) {
		NetMessageSentResult sentResult = new NetMessageSentResult();
		sentResult.setSentMsg(Util.bytesToHexString(by));
		ServerHandler handler = channelMap.get(msgManagerId);
		if (null != handler) {
			handler.send(by);
			sentResult.setCode(0);
			return sentResult;
		}else {
			sentResult.setCode(1);
			sentResult.setMessage("通信机未连接");
		}
		return sentResult;
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { 0x00, 0, 1, 0 };
		int managerNum = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
		System.out.println(managerNum + "?");
	}

}