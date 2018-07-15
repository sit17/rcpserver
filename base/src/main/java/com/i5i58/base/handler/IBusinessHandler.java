package com.i5i58.base.handler;

import com.i5i58.base.manager.IRemotPeer;

import io.netty.channel.ChannelHandlerContext;

public interface IBusinessHandler {
	
	boolean beforeServerStart();
	
	void afterServerStart();

	void onChannelActive(ChannelHandlerContext ctx, IRemotPeer peer);

	void onChannelInactive(ChannelHandlerContext ctx, IRemotPeer peer);

	void onExceptionCaught(ChannelHandlerContext ctx, Throwable cause);

	void onChannelRead0(IRemotPeer peer, String msg);

	void onDefenseCC(ChannelHandlerContext ctx, String msg);
	
	void onCustomRequest(int cmd, IRemotPeer peer, Object request);
}
