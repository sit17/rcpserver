package com.i5i58.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.Constant;
import com.i5i58.util.JedisUtils;
import com.i5i58.zookpeeper.ZookeeperService;

/**
 * @author songfl
 * if the user logged in the live server, call live server to process the request.
 * if the user is outside of any live server, process the request at local.
 */

public class UniqueOperationCheck {
	public static Logger logger = Logger.getLogger(UniqueOperationCheck.class);
	
	public static String getLoggedInServerKey(String accId){
		return new JedisUtils().hget(Constant.HOT_USERDATA + accId, Constant.SUB_USERDATA_SERVER_LOGGED_IN);
	}
	
	public static ResultDataSet httpPost(String serverKey, 
			String api, 
			Map<String, String> headers, 
			Map<String, String> params){
		ResultDataSet rds = new ResultDataSet();
		
		String uri = ZookeeperService.getInstance().getRequestUri(serverKey) + api;
		if (uri == null || uri.isEmpty()){
			rds.setCode(ResultCode.FOUND_LIVE_ERROR.getCode());
			rds.setMsg("找不到对应的直播间");
			return rds;
		}
//		StringBuilder builder = new StringBuilder();
//		for (Map.Entry<String, String> entry : params.entrySet()){
//			if (builder.length() > 0)
//				builder.append("&");
//			builder.append(entry.getKey()).append("=").append(entry.getValue());
//		}
//		if (builder.length() > 0){
//			builder.insert(0, '?');
//		}
//		String string = builder.toString();
//		uri += string;
//		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost(uri);
		for (Map.Entry<String, String> header : headers.entrySet()){
			httpPost.setHeader(header.getKey(), header.getValue());
		}
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()){
			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			nvps.add(pair);
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			CloseableHttpResponse response = client.execute(httpPost);
			try {
				String ret = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println("http post returned : " + ret);
				JSONObject object = JSON.parseObject(ret);
				if (object != null){				
					rds.setCode(object.getString("code"));
					rds.setMsg(object.getString("msg"));
					rds.setData(object.getString("data"));
				}else{
					logger.error("parse json object failed.");
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {				
				response.close();
			}
			return rds;
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		rds.setCode(ResultCode.REQUEST_TRANSPOND_FAILED.getCode());
		rds.setMsg("请求转发失败");
		return rds;
	}
}
