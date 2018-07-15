package com.i5i58.connectionService.config;

import com.i5i58.async.threading.ITaskPool;
import com.i5i58.base.config.BaseServerConfig;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.util.PropertiesUtil;
import com.i5i58.connectionService.handler.WebsocketServerInitializer;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class WebsocketServerConfig extends BaseServerConfig {

	private IBusinessHandler bunisnessHandler;
	private ITaskPool taskPool;

	public WebsocketServerConfig() {
		this.setName(PropertiesUtil.getProperty("server.websocket.name"));
		this.setPort(Integer.parseInt(PropertiesUtil.getProperty("server.websocket.port")));
		this.setBossthread(Integer.parseInt(PropertiesUtil.getProperty("server.websocket.bossthread")));
		this.setWorkerthread(Integer.parseInt(PropertiesUtil.getProperty("server.websocket.workerthread")));
		this.setEpoll(Boolean.parseBoolean(PropertiesUtil.getProperty("server.websocket.epoll")));
		this.setKeepalive(Boolean.parseBoolean(PropertiesUtil.getProperty("server.websocket.keepalive")));
		this.setNodelay(Boolean.parseBoolean(PropertiesUtil.getProperty("server.websocket.nodelay")));
		this.setSsl(Boolean.parseBoolean(PropertiesUtil.getProperty("server.websocket.ssl")));
		this.setConntimeout(Integer.parseInt(PropertiesUtil.getProperty("server.websocket.conntimeout")));
		this.setGate(Boolean.parseBoolean(PropertiesUtil.getProperty("server.websocket.isGate")));
		this.setSslMode(Integer.parseInt(PropertiesUtil.getProperty("server.websocket.sslmode")));
	}

	public void setBunisnessHandler(IBusinessHandler bunisnessHandler, ITaskPool taskPool) {
		this.bunisnessHandler = bunisnessHandler;
		this.taskPool = taskPool;
	}

	public void init() {
		super.init();
		handler = new LoggingHandler(LogLevel.DEBUG);
//		SslHandler sslHandler = null;
//		SslContext sslCtx = null;
//		if (isSsl()) {
////			boolean isOpenssl = OpenSsl.isAvailable();
////			if (!isOpenssl){
////				Throwable throwable = OpenSsl.unavailabilityCause();
////				System.out.println(throwable.toString());
////			}
////			sslCtx = NettySslContextUtil.getServerContext();
////			// use netty default for test 
////			SslContext sslCtx;
////			SelfSignedCertificate ssc;
////			try {
////				ssc = new SelfSignedCertificate();
////				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
////			} catch (CertificateException | SSLException e) {
////				e.printStackTrace();
////			}
////			
////			if (sslCtx != null){
////				sslHandler = sslCtx.newHandler(PooledByteBufAllocator.DEFAULT);
////			}
//			
//			// use jdk mode
//			SSLContext context = JdkSSLContextUtil.getServerContext();
//			SSLEngine engine = context.createSSLEngine();
//			
//			engine.setUseClientMode(false);
//			engine.setNeedClientAuth(false);
//			sslHandler = new SslHandler(engine);
//			
//		} else {
//			sslHandler = null;
//		}
		childHandler = new WebsocketServerInitializer(ssl, getSslMode(), bunisnessHandler, taskPool);
	}
}
