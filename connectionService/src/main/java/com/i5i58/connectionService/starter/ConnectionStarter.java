package com.i5i58.connectionService.starter;

import org.springframework.stereotype.Component;

import com.i5i58.base.starter.ApplicationServer;
import com.i5i58.connectionService.config.ConnectionTaskPool;
import com.i5i58.connectionService.config.WebsocketServerConfig;
import com.i5i58.connectionService.handler.BusinessHandler;

@Component
public class ConnectionStarter {
	
	private WebsocketServerConfig websocketServerConfig;
	private BusinessHandler businessHandler;
	private ConnectionTaskPool taskPool;
	private ApplicationServer websocketServer;

	public ConnectionStarter(){
		
	}
	
	public void init(){
		websocketServerConfig = new WebsocketServerConfig();
		businessHandler = new BusinessHandler();
		taskPool = new ConnectionTaskPool(16, 16);
		
		websocketServerConfig.setBunisnessHandler(businessHandler, taskPool);
		websocketServerConfig.init();
		
		websocketServer = new ApplicationServer(websocketServerConfig);
	}
	
	public void start() {
		if (!businessHandler.beforeServerStart()){
			return;
		}
		websocketServer.run();
		businessHandler.afterServerStart();
	}
	
	public void shutdown(){
		try {
			websocketServer.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args){
//		ConnectionStarter starter = new ConnectionStarter();
//		starter.start();
//	}
}
