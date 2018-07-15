package com.i5i58;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.rabbit.RabbitMessage;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.YXResultSet;

import push.AndroidNotification;
import push.AndroidNotification.AfterOpenAction;
import push.PushClient;
import push.android.AndroidBroadcast;
import push.android.AndroidGroupcast;

@Component
public class YunXinBroadcastQueueReceiver {
	private static final String queueName = "yunxin_broadcast";

	final int maxCount = 5;
	private int count = 0;

	private Logger logger = Logger.getLogger(getClass());

	//UMeng android 
	private String appkey = "58660e541061d22cf9000384";
	private String appMasterSecret = "9heepek4aksobogu4vcixmrnlqqoykhx";
	
	private PushClient client;
	
	public YunXinBroadcastQueueReceiver(){
		client = new PushClient();
	}
	
	@Bean
	public Queue yinxinQueue() {
		return new Queue(queueName);
	}

	@RabbitListener(queues = queueName)
	public void onMessage(RabbitMessage message) {
		try {
			System.out.println(new JsonUtils().toJson(message));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			if (count >= maxCount) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			} else {
				break;
			}
		}
		count++;
		System.out.println("count: " + count);
		
		try {
			if (message.getCmd().equals("channelNotice")) {
				JSONObject ob = JSON.parseObject(message.getData().toString());
				String cId = ob.getString("cId");
				String channelId = ob.getString("channelId");
				String channelName = ob.getString("channelName");
				String channelTitle = ob.getString("channelTitle");
				String stageName = ob.getString("stageName");
				String yunXinRId = ob.getString("yunXinRId");
				String coverUrl = ob.getString("coverUrl");
				String httpPullUrl = ob.getString("httpPullUrl");
				String playerCount = ob.getString("playerCount");
				
				String playerTimes = ob.getString("playerTimes");
				String owner = ob.getString("owner");
				String ownerName = ob.getString("ownerName");
				long openTime = ob.getLongValue("openTime");

				JSONArray toAccIds = ob.getJSONArray("toAccIds");
				YXResultSet ret = null;
				
				List<String> accIds = toAccIds.toJavaList(String.class);
				
				try {
					HashMap<String, Object> data = new HashMap<>();
					data.put("cId", cId);
					data.put("channelId", channelId);
					data.put("channelName", channelName);
					data.put("channelTitle", channelTitle);
					data.put("stageName", stageName);
					data.put("yunXinRId", yunXinRId);
					data.put("coverUrl", coverUrl);
					data.put("httpPullUrl", httpPullUrl);
					data.put("playerCount", playerCount);
					data.put("playerTimes", playerTimes);
					data.put("owner", owner);
					data.put("ownerName", ownerName);
					data.put("openTime", openTime);

					HashMap<String, Object> map = new HashMap<>();
					map.put("cmd", message.getCmd());
					map.put("data", data);

					String content = new JsonUtils().toJson(map);
					String pushcontent = String.format("%s正在直播", ownerName);

					ret = YunxinIM.sendBatchAttachMessage(owner, toAccIds.toJSONString(), 
							content, pushcontent, content, "", "1", "");
					
					if (ret.getCode().equals("200")){					
	//					logger.info("send attach msg" + new JsonUtils().toJson(message));
					}else{
						logger.info(new JsonUtils().toJson(ret));
					}
					
					sendAndroidGroupcast(pushcontent, content, accIds);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		} catch (Throwable throwable) {
			logger.error(throwable);
		}
	}

	@Scheduled(fixedRate = 5000, initialDelay = 5000)
	public void clearCount() {
		count = 0;
//		System.out.println("重新计数");
	}

	public void sendAndroidBroadcast(String title, String content) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
		broadcast.setTicker("胖虎直播");
		broadcast.setTitle("直播通知");
		broadcast.setText(title);
		broadcast.goAppAfterOpen();
		broadcast.setAfterOpenAction(AfterOpenAction.go_custom);
		broadcast.setCustomField(content);
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setProductionMode();
//		broadcast.setTestMode();
		// Set customized fields
		client.send(broadcast);
	}
	
	public void sendAndroidGroupcast(String title, String content, List<String> accIds) throws Exception {
		if(accIds == null || accIds.size() == 0)
			return;
		AndroidGroupcast groupcast = new AndroidGroupcast(appkey,appMasterSecret);

		org.json.JSONObject filterJson = new org.json.JSONObject();
		org.json.JSONObject whereJson = new org.json.JSONObject();
		org.json.JSONArray tagArray = new org.json.JSONArray();
		org.json.JSONObject allTags = new org.json.JSONObject();
		
		for (String accId : accIds){
			org.json.JSONObject jsonObject = new org.json.JSONObject();
			jsonObject.put("tag", accId);
			tagArray.put(jsonObject);
		}

		allTags.put("or", tagArray);
		
		org.json.JSONArray andArray = new org.json.JSONArray();
		andArray.put(allTags);
		
		whereJson.put("and", andArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());
		
		groupcast.setFilter(filterJson);
		groupcast.setTicker("胖虎直播");
		groupcast.setTitle("直播通知");
		groupcast.setText(title);
		groupcast.goAppAfterOpen();
		groupcast.setAfterOpenAction(AfterOpenAction.go_custom);
		groupcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);

		groupcast.setCustomField(content);
		
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		groupcast.setProductionMode();
//		groupcast.setTestMode();
		client.send(groupcast);
	}
}
