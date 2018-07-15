package com.i5i58.push.threading;

import com.i5i58.async.threading.Task;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.util.JsonRequest;
import com.i5i58.push.protocol.CustomRequestIds;

public class QrLoginTask extends Task {

	IBusinessHandler businessHandler;
	IRemotPeer peer;
	JsonRequest jsonRequest;
	
	public QrLoginTask(IBusinessHandler businessHandler, IRemotPeer peer, JsonRequest jsonRequest){
		this.peer = peer;
		this.businessHandler = businessHandler;
		this.jsonRequest = jsonRequest;
	}
	
	@Override
	protected void onRun() {
		//businessHandler.onQrLogin(peer, jsonRequest);
		businessHandler.onCustomRequest(CustomRequestIds.qrLogin, peer, jsonRequest);
	}

}
