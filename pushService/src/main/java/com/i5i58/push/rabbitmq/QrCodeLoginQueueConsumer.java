package com.i5i58.push.rabbitmq;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.i5i58.async.threading.ITask;
import com.i5i58.base.handler.IBusinessHandler;
import com.i5i58.base.manager.IRemotPeer;
import com.i5i58.base.manager.RemotePeerManager;
import com.i5i58.base.rabbitmq.EndPoint;
import com.i5i58.base.util.JedisWrapper;
import com.i5i58.base.util.JsonRequest;
import com.i5i58.push.protocol.NotifyCmd;
import com.i5i58.push.threading.QrLoginTask;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class QrCodeLoginQueueConsumer extends EndPoint implements Consumer {

	private Logger logger = Logger.getLogger(getClass());
	private String consumerTag;
	private IBusinessHandler businessHandler;
	public QrCodeLoginQueueConsumer(IBusinessHandler businessHandler) {
		super("qrlogin_broadcast");
		this.businessHandler = businessHandler;
	}

	public void consume(){
		// start consuming messages. Auto acknowledge messages.
		try {
			consumerTag = channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("rabbitmq basic consume, consumerTag = " + consumerTag);
	}
	
	public void cancel(){
		logger.info("rabbitmq basic cancel, consumerTag = " + consumerTag);
		try {
			channel.basicCancel(consumerTag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		consumerTag = "";
	}
	
//	public void run() {
//		try {
//			if (consumerTag == null || consumerTag.isEmpty()){
//				logger.info("rabbitmq basic cancel, consumerTag = " + consumerTag);
//				channel.basicCancel(consumerTag);
//				consumerTag = "";
//			}else{				
//				// start consuming messages. Auto acknowledge messages.
//				consumerTag = channel.basicConsume(endPointName, true, this);
//				logger.info("rabbitmq basic consume, consumerTag = " + consumerTag);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Called when consumer is registered.
	 */
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available.
	 */
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		
		String msg = new String(body, Charset.defaultCharset());
		System.out.println(msg);
		
		JsonRequest jsonRequest = new JsonRequest();
		if (!jsonRequest.parseObject(msg)){
			logger.error("parse json message failed.");
			return;
		}
		String cmd = jsonRequest.getCmd();
		if (cmd == null || cmd.isEmpty()){
			logger.error("message cmd is null or empty");
			return;
		}
		
		if (!cmd.equals(NotifyCmd.QrLoginResult)){
			logger.error("unknown cmd = " + cmd);
			return;
		}
		
		String qrToken = jsonRequest.getString("qrToken");
		if (qrToken == null || qrToken.isEmpty()){
			logger.error("qrToken is null or empty, json string = " + msg);
			return;
		}
		JedisWrapper jedisUtils = new JedisWrapper();
		if (!jedisUtils.exist(qrToken)){
			logger.error("qr login timeout or an error has occurred.");
			return;
		}
		String sessionId = jedisUtils.get(qrToken);
		IRemotPeer peer = RemotePeerManager.getClient(sessionId);
		if (peer == null){
			logger.error("qrToken =" + qrToken + ", session id = " + sessionId + ", peer is not exist.");
			return;
		}
		ITask task = new QrLoginTask(businessHandler, peer, jsonRequest);
		peer.getTaskQueue().addTask(task);
	}

	public void handleCancel(String consumerTag) {
	}

	public void handleCancelOk(String consumerTag) {
	}

	public void handleRecoverOk(String consumerTag) {
	}

	public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
	}
}