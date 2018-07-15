package com.i5i58.push.threading;

import com.i5i58.async.threading.Task;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;

public class ChannelReadTask extends Task {
	IBusinessHandler businessHandler;
	String msg;
	IRemotPeer peer;

	public ChannelReadTask(IBusinessHandler businessHandler, String msg, IRemotPeer peer) {
		this.businessHandler = businessHandler;
		this.msg = msg;
		this.peer = peer;
	}

	@Override
	protected void onRun() {
		businessHandler.onChannelRead0(peer, msg);
	}

}
