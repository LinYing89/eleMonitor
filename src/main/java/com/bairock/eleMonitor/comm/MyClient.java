package com.bairock.eleMonitor.comm;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class MyClient {
	private static MyClient ins = new MyClient();
	public boolean linking = false;

	Logger logger = LoggerFactory.getLogger(this.getClass());

//	 private String nextIp = "127.0.0.1";
	public String nextIp = "218.92.24.10";
	private int nextPort = 6007;
	private Bootstrap b;

	private MyClientHandler myClientHandler;

	private MyClient() {
		init();
	}

	public static MyClient getIns() {
		return ins;
	}

	private void init() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		b = new Bootstrap(); // (1)
		b.group(workerGroup); // (2)
		b.channel(NioSocketChannel.class); // (3)
		b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 8000);
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new IdleStateHandler(-1, 20, -1, TimeUnit.SECONDS)); // 1
				ch.pipeline().addLast(new MyClientHandler());
			}
		});
	}

	public void link() {
		if (linking || null != myClientHandler) {
			return;
		}
		linking = true;
		try {
			logger.error("Start linking");
			// Start the client.
			ChannelFuture channelFuture = b.connect(nextIp, nextPort); // (5)
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						logger.info("连接超时");
						linking = false;
					}
				}
			});
			// Wait until the connection is closed.
			channelFuture.channel().closeFuture();
		} catch (Exception e) {
			logger.error("linking error: " + e.getMessage());
			linking = false;
			myClientHandler = null;
			e.printStackTrace();
		}
	}

	void setMyClientHandler(MyClientHandler myClientHandler) {
		if (this.myClientHandler != null) {
			if (this.myClientHandler.channel != null) {
				this.myClientHandler.channel.close();
			}
			this.myClientHandler = null;
		}
		this.myClientHandler = myClientHandler;
	}

	public void closeHandler() {
		if (null != myClientHandler) {
			myClientHandler.channel.close();
			myClientHandler = null;
		}
	}

	public void send(byte[] msg) {

		if (null != myClientHandler) {
			// logger.info("转发: " + ServerHandler.bytesToHexString(msg));
			myClientHandler.send(msg);
		} else {
			logger.info("转发 not linked linking=" + MyClient.getIns().linking);
			link();
		}
	}

}
