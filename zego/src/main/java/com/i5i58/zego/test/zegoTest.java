package com.i5i58.zego.test;

import java.io.IOException;
import java.util.HashMap;

import com.i5i58.util.DateUtils;
import com.i5i58.zego.ZegoLive;
import com.i5i58.zego.ZegoResult;
import com.i5i58.zego.utils.ZegoUtils;

public class zegoTest {
	private static String access_token = "Tkl2aDcxYkh3TzVmQVplcFNHZWJSVE0wOFk3WnJHZ2w3WS9vOUxMQ2dSY2FtT1pocGNPSTZnQXpRZ1pXOGFaUg";
//	private static String access_token = "";
	private static int expire = 0;
	
	private static HashMap<String, Object> data;
	
	private static String start="";
	private static String end="";
	
	public static void getToken(){
		try {
			ZegoResult result = ZegoLive.getToken();
			if (result.getCodeInt() == 0){
				HashMap<String, Object> data = result.getData();
				if (data != null){
					access_token = (String) data.get("access_token");
					expire = (Integer) data.get("expires_in");

					System.out.println("access_token " + access_token);
					System.out.println("expire " + expire);
				}
			}else{
				System.out.println(ZegoUtils.getErrorString(result.getCodeInt()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createLive(){
//		String access_token = "";
		String title= "虎玩堂-测试";
		String id_name= "0003ba9503664d89b49b2798e5e170cd";
		String nick_name= "你的眼是我永生遇不到的海";
		String term_type= "Android";
		String net_type= "无线";
		String stream_id= "0248bad961fc455492bc66dea85f28a1";
		String live_id= "";
		String live_channel= "0248bad961fc455492bc66dea85f28a1";
		String publish_url= "";
		String rtmp_url= "";
		String hls_url= "";
		String hdl_url= "";
		
		title = "开播啦";
		id_name = "a3a4900dfb3141a8984650a71a5f38a4";
		nick_name ="10099";
		stream_id = "10923193";
//		live_id = "10923193";
		live_channel = "0248bad961fc455492bc66dea85f28a1";
		
		try {
			ZegoResult result = ZegoLive.createLive(access_token, 
					title, 
					id_name, 
					nick_name, 
					term_type, 
					net_type, 
					stream_id, 
					live_id, 
					live_channel, 
					publish_url, 
					rtmp_url, 
					hls_url, 
					hdl_url);
			data = result.getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static  void closeLive(){
		String stream_id = (String) data.get("stream_id");
		try {
			ZegoResult result = ZegoLive.closeLive(access_token, stream_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void forbidLive(){
		String stream_alias = (String) data.get("stream_alias");
		try {
			ZegoResult result = ZegoLive.forbidLive(access_token, stream_alias);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void resumeLive(){
		String stream_alias = (String) data.get("stream_alias");
		try {
			ZegoResult result = ZegoLive.resumeLive(access_token, stream_alias);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createLiveRecord(){
		String stream_alias = (String) data.get("stream_alias");
//		String start = "2017-08-08T17:04:20Z";
//		String end = "2017-08-08T17:04:25Z";
		try {
			ZegoResult result = ZegoLive.createLiveRecord(access_token, stream_alias, start, end);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void checkSignature(String timestamp, String nonce){
		String md5 = ZegoLive.calcSignature(timestamp, nonce);
		System.out.println(md5);
	}

	public static void main(String[] args) {
//		checkSignature("1502187320", "789607");
//		getToken();
		createLive();
//		forbidLive();
//		closeLive();
//		createLiveRecord();
		
//		resumeLive();
//		closeLive();
	}

}
