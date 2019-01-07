package com.bairock.eleMonitor.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyClientHandler extends ChannelInboundHandlerAdapter {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public Channel channel;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	MyClient.getIns().setMyClientHandler(this);
        channel = ctx.channel();
        MyClient.getIns().linking = false;
        logger.info(MyClient.getIns().nextIp + "连接成功");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf)msg;
        try {
            byte[] req = new byte[m.readableBytes()];
            m.readBytes(req);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.info(cause.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info(MyClient.getIns().nextIp + "连接断开");
        ctx.close();
        channel = null;
        MyClient.getIns().linking = false;
        MyClient.getIns().setMyClientHandler(null);
        MyClient.getIns().link();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {  // 2
        	logger.info(evt.toString());
            ctx.close();
        }
        MyClient.getIns().linking = false;
    }

    public void send(byte[] by){
        try {
            if(null != channel) {
                channel.writeAndFlush(Unpooled.copiedBuffer(by));
                logger.error("转发 成功");
            }else {
            	 MyClient.getIns().link();
            }
        }catch (Exception e){
        	logger.error("转发 失败" + e.getMessage());
            e.printStackTrace();
        }
    }

}