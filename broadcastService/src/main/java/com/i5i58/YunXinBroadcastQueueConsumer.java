package com.i5i58;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.rabbit.RabbitMessage;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.YXResultSet;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * ��ȡ���еĳ���ˣ�ʵ����Runnable�ӿڡ�
 * 
 * @author syntx
 *
 */
public class YunXinBroadcastQueueConsumer extends EndPoint implements Runnable, Consumer {

	private Logger logger = Logger.getLogger(getClass());
	
	final int maxCount = 10;

	private Integer count = 0;

	public YunXinBroadcastQueueConsumer() throws IOException, TimeoutException {
		super("yunxin_broadcast");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				System.out.println("-------设定要指定任务--------");
				count = 0;
			}
		}, 5000, 5000);// 设定指定的时间time,此处为2000毫秒
	}

	public void run() {
		try {
			// start consuming messages. Auto acknowledge messages.
			channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		while (true) {
			if (count >= maxCount) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			} else {
				break;
			}
		}
		RabbitMessage message = (RabbitMessage) SerializationUtils.deserialize(body);
		System.out.println("cmd:" + message.getCmd() + " data:" + message.getData() + " received.");
		count++;
		System.out.println("count: " + count);

		if (message.getCmd().equals("channelNotice")){
			JSONObject ob = JSON.parseObject(message.getData().toString());
			String cId = ob.getString("cId");
			String owner = ob.getString("owner");
			String ownerName = ob.getString("ownerName");
			String channelName = ob.getString("channelName");
			String to = ob.getString("to");
			YXResultSet ret = null;
			try {
				HashMap<String, Object> data = new HashMap<>();
				data.put("cId", cId);
				data.put("channelName", channelName);
				data.put("owner", owner);
				data.put("ownerName", ownerName);
				data.put("to", to);

				HashMap<String, Object> map = new HashMap<>();
				map.put("cmd", message.getCmd());
				map.put("data", data);
				
				ret = YunxinIM.sendAttachMessage(owner, "0", to, 
						new JsonUtils().toJson(map), "", "", "", "1", "");
				
				System.out.println(new JsonUtils().toJson(ret));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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