package com.i5i58.push.starter;

import com.i5i58.base.starter.ApplicationServer;
import com.i5i58.push.config.PushTaskPool;
import com.i5i58.push.config.WebsocketServerConfig;
import com.i5i58.push.handler.PushBusinessHandler;
import com.i5i58.push.rabbitmq.QrCodeLoginQueueConsumer;

public class Starter {
	
	private WebsocketServerConfig websocketServerConfig;
	private PushBusinessHandler businessHandler;
	private PushTaskPool taskPool;
//	private RemotePeerManager globalPeerManager;
	private ApplicationServer websocketServer;
	private QrCodeLoginQueueConsumer qrCodeLoginQueueConsumer;

	public Starter(){
		websocketServerConfig = new WebsocketServerConfig();
		businessHandler = new PushBusinessHandler();
		taskPool = new PushTaskPool(16, 16);
		
		websocketServerConfig.setBunisnessHandler(businessHandler, taskPool);
		websocketServerConfig.init();
		
		websocketServer = new ApplicationServer(websocketServerConfig);
		
		qrCodeLoginQueueConsumer = new QrCodeLoginQueueConsumer(businessHandler);
	}
	
	public void start() {
		if (!businessHandler.beforeServerStart()){
			return;
		}
		websocketServer.run();
		businessHandler.afterServerStart();
		qrCodeLoginQueueConsumer.consume();
	}
	
	public void shutdown(){
		try {
			websocketServer.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		qrCodeLoginQueueConsumer.cancel();
	}
	
	public static void main(String[] args){
		Starter starter = new Starter();
		starter.start();
	}
}
