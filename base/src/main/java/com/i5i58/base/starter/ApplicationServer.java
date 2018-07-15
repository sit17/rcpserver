package com.i5i58.base.starter;

import java.net.InetSocketAddress;

import com.i5i58.base.config.IServerConfig;
import com.i5i58.base.util.NetworkUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ApplicationServer implements Runnable {

	protected IServerConfig serverConfig;

	protected Channel serverChannel;

	public ApplicationServer(IServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public IServerConfig getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(IServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public Channel getServerChannel() {
		return serverChannel;
	}

	public void setServerChannel(Channel serverChannel) {
		this.serverChannel = serverChannel;
	}

	public boolean isRunning() {
		if (serverChannel == null)
			return false;
		return serverChannel.isOpen() || serverChannel.isActive();
	}

	public void close() throws InterruptedException {
		if (serverChannel == null) {
			return;
		}
		serverChannel.close().sync();
	}

	public void sync() throws InterruptedException {
		if (serverChannel == null)
			return;
		serverChannel.closeFuture().sync();
	}

	private void setOption(ServerBootstrap b) {
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverConfig.getConntimeout());
		// b.option(ChannelOption.TCP_NODELAY, tcpServerConfig.isNodelay());
		b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		b.childOption(ChannelOption.SO_KEEPALIVE, serverConfig.isKeepalive());
		b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		b.childOption(ChannelOption.TCP_NODELAY, serverConfig.isNodelay());
	}

	@Override
	public void run() {
		try {
			ServerBootstrap b = new ServerBootstrap();
			EventLoopGroup bossGroup, workerGroup;

			if (serverConfig.isEpoll()) {
				bossGroup = new EpollEventLoopGroup(serverConfig.getBossthread());
				workerGroup = new EpollEventLoopGroup(serverConfig.getWorkerthread());
				b.channel(EpollServerSocketChannel.class);
			} else {
				bossGroup = new NioEventLoopGroup(serverConfig.getBossthread());
				workerGroup = new NioEventLoopGroup(serverConfig.getWorkerthread());
				b.channel(NioServerSocketChannel.class);
			}
			b.group(bossGroup, workerGroup);

			if (serverConfig.getHandler() != null) {
				b.handler(serverConfig.getHandler());
			}
			if (serverConfig.getChildHandler() != null) {
				b.childHandler(serverConfig.getChildHandler());
			}
			setOption(b);
			if (serverConfig.getPort() <= 0){
				int port = NetworkUtil.getFreePort();
				serverConfig.setPort(port);
			}
			serverChannel = b.bind(serverConfig.getPort()).sync().channel();
			if (serverChannel.isActive()) {
				serverChannel.closeFuture().addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						// TODO Auto-generated method stub
						future.channel().parent().eventLoop().shutdownGracefully();
						future.channel().eventLoop().shutdownGracefully();
					}
				});
				InetSocketAddress inetSocketAddress = (InetSocketAddress) serverChannel.localAddress();
				serverConfig.setPort(inetSocketAddress.getPort());
				System.out.println(
						serverConfig.getName() + " start success. Listen at port " + serverConfig.getPort());
			} else {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
				System.err.println(serverConfig.getName() + " start failed");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}
}
