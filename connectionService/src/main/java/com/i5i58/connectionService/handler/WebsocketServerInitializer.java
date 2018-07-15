package com.i5i58.connectionService.handler;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import com.i5i58.async.threading.ITaskPool;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.util.Definition;
import com.i5i58.base.util.JdkSSLContextUtil;
import com.i5i58.base.util.NettySslContextUtil;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebsocketServerInitializer extends ChannelInitializer<SocketChannel> { // 1
	private IBusinessHandler businessHandler;
	private ITaskPool taskPool;
	private boolean ssl;
	private int sslMode;

	private SslContext sslCtx;
	
	private SSLEngine engine;
	private SSLContext context;
	
	public WebsocketServerInitializer(boolean ssl, int sslMode, IBusinessHandler businessHandler, ITaskPool taskPool) {
		this.ssl = ssl;
		this.sslMode = sslMode;
		this.businessHandler = businessHandler;
		this.taskPool = taskPool;

		if (ssl) {
			if (sslMode == 0){
				// use netty default for test 
				SelfSignedCertificate ssc;
				try {
					ssc = new SelfSignedCertificate();
					sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
				} catch (CertificateException | SSLException e) {
					e.printStackTrace();
				}
			}else if (sslMode == 1){
				sslCtx = NettySslContextUtil.getServerContext();
			}else if (sslMode == 2){
				
			}		
		}
	}
	
	public SslHandler newSslHandler(SocketChannel ch){
		SslHandler sslHandler = null;
//		SslContext sslCtx = null;
		if (ssl) {
			if (sslMode == 0 || sslMode == 1){
				sslHandler = sslCtx.newHandler(ch.alloc());
			}else{
				// use jdk mode
				context = JdkSSLContextUtil.getServerContext();
				engine = context.createSSLEngine();
				engine.setUseClientMode(false);
				engine.setNeedClientAuth(false);
				sslHandler = new SslHandler(engine);
			}
		} else {
			sslHandler = null;
		}
		return sslHandler;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {// 2
		ChannelPipeline pipeline = ch.pipeline();
		
		SslHandler sslHandler = newSslHandler(ch);
		if (sslHandler != null){
			pipeline.addLast(sslHandler);
		}
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WebsocketRequestHandler(Definition.WEBSOCKET_PATH));
		pipeline.addLast(new WebSocketServerProtocolHandler(Definition.WEBSOCKET_PATH));
		pipeline.addLast(new TextWebSocketFrameHandler(businessHandler, taskPool));

	}
}
