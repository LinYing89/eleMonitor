package com.bairock.eleMonitor.comm;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
	
	public static int PORT = 20001;

	private ServerBootstrap b;
	private ChannelFuture f;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void run() throws Exception {
		bossGroup = new NioEventLoopGroup(); // (1)
		workerGroup = new NioEventLoopGroup();
		try {
			b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
//							ch.pipeline().addLast("framedecoder",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 2, 6, 0));
							ch.pipeline().addLast(new ServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			f = b.bind(PORT); // (7)
			f.channel().closeFuture();
		} finally {
		}
	}

	public void close() {
		if (null != bossGroup) {
			bossGroup.shutdownGracefully().syncUninterruptibly();
		}
		if (null != workerGroup) {
			workerGroup.shutdownGracefully().syncUninterruptibly();
		}
	}
}
