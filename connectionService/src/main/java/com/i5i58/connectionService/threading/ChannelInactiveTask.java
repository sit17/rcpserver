package com.i5i58.connectionService.threading;

import com.i5i58.async.threading.Task;
import com.i5i58.async.threading.TaskQueue;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.manager.RemotePeerManager;
import com.i5i58.base.util.NetworkUtil;

import io.netty.channel.ChannelHandlerContext;

public class ChannelInactiveTask extends Task {
	IBusinessHandler businessHandler;
	ChannelHandlerContext ctx;
	IRemotPeer peer;
	
	public ChannelInactiveTask(IBusinessHandler businessHandler, ChannelHandlerContext ctx, IRemotPeer peer){
		this.ctx = ctx;
		this.peer = peer;
		this.businessHandler = businessHandler;
	}
	
	@Override
	protected void onRun() {
		businessHandler.onChannelInactive(ctx, peer);
		RemotePeerManager.removeClient(peer.getSessionId());
		NetworkUtil.closeGracefully(ctx);
		TaskQueue queue = peer.getTaskQueue();
		if (queue != null){
			queue.decrementPeerCount();
		}
		peer.setTaskQueue(null);
	}

}
