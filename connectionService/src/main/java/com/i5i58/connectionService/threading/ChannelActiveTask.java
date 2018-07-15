package com.i5i58.connectionService.threading;

import com.i5i58.async.threading.Task;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;

import io.netty.channel.ChannelHandlerContext;

public class ChannelActiveTask extends Task {

	IBusinessHandler businessHandler;
	ChannelHandlerContext ctx;
	IRemotPeer peer;
	
	public ChannelActiveTask(IBusinessHandler businessHandler, ChannelHandlerContext ctx, IRemotPeer peer){
		this.ctx = ctx;
		this.peer = peer;
		this.businessHandler = businessHandler;
	}
	
	@Override
	protected void onRun() {
		businessHandler.onChannelActive(ctx, peer);
	}
}
