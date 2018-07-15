package com.i5i58.zego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.i5i58.util.JsonUtils;


/**
 * @author songfl
 * 即构服务端接口
 */
public class ZegoLive {
	private static String appid = "1989373496";
	private static String secret = "00079137e45ea5f8a64778890b30e022";
	private static String host = "https://webapi.zego.im/cgi/";
//	private static String host = "https://testwebapi.zego.im/cgi/";
	
	public static String getSha1(String str){
	    if (null == str || 0 == str.length()){
	        return null;
	    }
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'a', 'b', 'c', 'd', 'e', 'f'};
	    try {
	        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	        mdTemp.update(str.getBytes("UTF-8"));
	         
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char[] buf = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            buf[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(buf);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static String calcSignature(String timestamp, String nonce){
		List<String> params = new ArrayList<String>();
		params.add(timestamp);
		params.add(nonce);
		params.add(secret);
		Collections.sort(params);
		StringBuilder builder = new StringBuilder();
		for (String s : params){
			builder.append(s);
		}
		String str = builder.toString();
		System.out.println(str);
		return getSha1(str);
	}
	
	private static String getParameters(Map<String, String> map) {
		StringBuffer buffer = new StringBuffer();
		if (map == null || map.isEmpty()) {
			return "";
		} else {
			for (String key : map.keySet()) {
				String value = map.get(key);
				if (buffer.length() < 1) {
					buffer.append(key).append("=").append(value);
				} else {
					buffer.append("&").append(key).append("=").append(value);
				}
			}
		}
		return buffer.toString();
	}
	private static String getResult(HttpURLConnection conn) {
		StringBuffer buffer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			buffer = new StringBuffer();
			String line = null;
			while (null != (line = reader.readLine())) {
				buffer.append(line);
			}
			reader.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer == null ? "" : buffer.toString();
	}
	
	public static String doPost(String api, Map<String, String> params) {
		String result = "";
		try {
			URL urlPost = new URL(host + api);
			HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
			conn.setRequestMethod("POST");// 设置请求方式
			conn.setDoInput(true);// 设置可以使用输入流读取数�?
			conn.setDoOutput(true);// 设置可以使用输出流写数据
			conn.setUseCaches(false);// 不使用缓�?
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setRequestProperty("Charset", "utf-8");
			String data = getParameters(params);
			conn.setRequestProperty("Connection", "Keep-Alive");
			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes());
			out.flush();// 刷新
			out.close();
			result = getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	public static String httpGet(String url,  HashMap<String, String> params) throws IOException{
		String fullUrl = url + "?" + getParameters(params);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(fullUrl);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				// 打印执行结果
				HttpEntity entity = response.getEntity();
				String retString = EntityUtils.toString(entity, "utf-8");
				System.out.println(retString);
				System.out.println(response.getStatusLine());
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return "";
	}
	
	public static String httpPost(String api, Map<String, String> params) throws IOException {
//		String fullUrl = host + api + "?" + getParameters(params);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			 
			// 设置请求的参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : params.keySet()){
				String value = params.get(key);
				nvps.add(new BasicNameValuePair(key, value));
			}
			
			HttpPost httpPost = new HttpPost(host + api);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			System.out.println(httpPost.toString());
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				// 打印执行结果
				HttpEntity entity = response.getEntity();
				String retString = EntityUtils.toString(entity, "utf-8");
				System.out.println(retString);
				System.out.println(response.getStatusLine());
				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
				return retString;
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}
	
	/**
	 * @author songfl
	 * 刷新accessToke
	 * @return ZegoResultn
	 * @throws IOException
	 * */
	public static ZegoResult getToken() throws IOException{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("secret", secret);
		String ret = httpGet(new StringBuilder().append(host).append("token").toString(), params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}

	/**
	 * @author songfl
	 * 创建直播
	 * 
	 * 			参数名			是否必须		说明
	 * @param 	access_token		是			调用接口凭证
	 * @param 	title			是			直播标题
	 * @param 	id_name			是			用户ID 第三方用户唯一标识
	 * @param 	nick_name		是			用户昵称
	 * @param 	term_type		是			客户端类型(如Android,iOS,Windows)
	 * @param 	net_type		是			网络类型(如有线,无线,4G,3G,2G)
	 * @param 	stream_id		否			自定义流名，默认不填，系统自动生成，如果填写则按此填写的内容生成推流地址
	 * @param 	live_id			否			直播ID 一个直播唯一标识 当多主播同台时，非第一个主播需要带上此参数，否则视为新创建一个直播
	 * @param 	live_channel	否			自定义频道
	 * @param 	publish_url		否			自定义推流地址
	 * @param 	rtmp_url		否			自定义rtmp拉流地址 多个地址用逗号隔开
	 * @param 	hls_url			否			自定义hls拉流地址 多个地址用逗号隔开
	 * @param 	hdl_url			否			自定义hdl拉流地址 多个地址用逗号隔开
	 * @return ZegoResult
	 * @throws IOException 
	 * */
	public static ZegoResult createLive(String access_token, 
			String title,
			String id_name,
			String nick_name,
			String term_type,
			String net_type,
			String stream_id,
			String live_id,
			String live_channel,
			String publish_url,
			String rtmp_url,
			String hls_url,
			String hdl_url) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (title != null && !title.equals("")){
			params.put("title", title);
		}
		if (id_name != null && !id_name.equals("")){
			params.put("id_name", id_name);
		}
		if (nick_name != null && !nick_name.equals("")){
			params.put("nick_name", nick_name);
		}
		if (term_type != null && !term_type.equals("")){
			params.put("term_type", term_type);
		}
		if (net_type != null && !net_type.equals("")){
			params.put("net_type", net_type);
		}
		if (stream_id != null && !stream_id.equals("")){
			params.put("stream_id", stream_id);
		}
		if (live_id != null && !live_id.equals("")){
			params.put("live_id", live_id);
		}
		if (live_channel != null && !live_channel.equals("")){
			params.put("live_channel", live_channel);
		}
		if (publish_url != null && !publish_url.equals("")){			
			params.put("publish_url", publish_url);
		}
		if (rtmp_url != null && !rtmp_url.equals("")){			
			params.put("publish_url", rtmp_url);
		}
		if (hls_url != null && !hls_url.equals("")){			
			params.put("publish_url", hls_url);
		}
		if (hdl_url != null && !hdl_url.equals("")){			
			params.put("publish_url", hdl_url);
		}
		String ret = httpPost("create-live?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	
	/**
	 * @author songfl
		关闭直播
				参数名			是否必须		说明
	 * @param access_token		是			调用接口凭证
	 * @param stream_id			是			流ID 在创建直播时server端返回的stream_id
	 * @return ZegoResult
	 * @throws IOException
	 */
	public static ZegoResult closeLive(String access_token, 
			String stream_id) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (stream_id != null && !stream_id.equals("")){
			params.put("stream_id",stream_id);
		}
		
		String ret = httpPost("close-live?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	/**
	 * 禁止直播
	 * @author songfl
	 * 			参数名			是否必须		说明
	 * @param 	access_token	是			调用接口凭证
	 * @param 	stream_alias	是			流名 在RTMP地址中的流名
	 * @return ZegoResult
	 * @throws IOException 
	 */
	public static ZegoResult forbidLive(String access_token, String stream_alias) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (stream_alias != null && !stream_alias.equals("")){
			params.put("stream_alias",stream_alias);
		}
		
		String ret = httpPost("forbid-live?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	/**
	 * 恢复直播
	 * @author songfl
	 * 			参数名			是否必须		说明
	 * @param 	access_token	是			调用接口凭证
	 * @param 	stream_alias	是			流名 在RTMP地址中的流名
	 * @return ZegoResult
	 * @throws IOException 
	 */
	public static ZegoResult resumeLive(String access_token, String stream_alias) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (stream_alias != null && !stream_alias.equals("")){
			params.put("stream_alias",stream_alias);
		}
		
		String ret = httpPost("resume-live?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	/**
	 * 开始混流
	 * @author songfl
	 * 			参数名			是否必须		说明
	 * @param 	access_token	是			调用接口凭证
	 * @param 	signature		是			签名摘要
	 * @param 	id_name			是			用户标识
	 * @param 	live_channel	是			直播频道
	 * @param 	MixInput		是			混流输入 json格式
	 * @param 	MixOutput		是			混流输出 json格式
	 * @return ZegoResult
	 * @throws IOException 
	 */
	public static ZegoResult startMix(String access_token, String signature, String id_name,
			String live_channel, String MixInput, String MixOutput) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (signature != null && !signature.equals("")){
			params.put("signature",signature);
		}
		if (id_name != null && !id_name.equals("")){
			params.put("id_name",id_name);
		}
		if (live_channel != null && !live_channel.equals("")){
			params.put("live_channel",live_channel);
		}
		if (MixInput != null && !MixInput.equals("")){
			params.put("MixInput",MixInput);
		}
		if (MixOutput != null && !MixOutput.equals("")){
			params.put("MixOutput",MixOutput);
		}
		String ret = httpPost("start-mix?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	/**
	 * 停止混流
	 * @author songfl
	 * 			参数名			是否必须		说明
	 * @param 	access_token	是			调用接口凭证
	 * @param 	signature		是			签名摘要
	 * @param 	id_name			是			用户标识
	 * @param 	live_channel	是			直播频道
	 * @param 	mixurl			是			需要停止混流的地址
	 * @return ZegoResult
	 * @throws IOException 
	 */
	public static ZegoResult stopMix(String access_token, String signature, String id_name,
			String live_channel, String mixurl) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (signature != null && !signature.equals("")){
			params.put("signature",signature);
		}
		if (id_name != null && !id_name.equals("")){
			params.put("id_name",id_name);
		}
		if (live_channel != null && !live_channel.equals("")){
			params.put("live_channel",live_channel);
		}
		if (mixurl != null && !mixurl.equals("")){
			params.put("mixurl",mixurl);
		}
		String ret = httpPost("stop-mix?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
	
	/**
	 * 创建录制索引文件
	 * @author songfl
	 * 			参数名			是否必须		说明
	 * @param 	access_token	是			调用接口凭证
	 * @param 	appid			是			第三方用户唯一凭证
	 * @param 	stream_alias	是			流名
	 * @param 	start			是			开始时间，格式：2015-12-01T17:36:00Z
	 * @param 	end				是			结束时间，格式：2015-12-01T17:36:00Z
	 * @return ZegoResult
	 * @throws IOException 
	 */
	public static ZegoResult createLiveRecord(String access_token, String stream_alias,
			String start, String end) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
//		if (access_token != null && !access_token.equals("")){
//			params.put("access_token",access_token);
//		}
		if (appid != null && !appid.equals("")){
			params.put("appid",appid);
		}
		if (stream_alias != null && !stream_alias.equals("")){
			params.put("stream_alias",stream_alias);
		}
		if (start != null && !start.equals("")){
			params.put("start",start);
		}
		if (end != null && !end.equals("")){
			params.put("end",end);
		}
		String ret = httpPost("create-live-record?access_token="+access_token, params);
		return new JsonUtils().toObject(ret, ZegoResult.class);
	}
}
