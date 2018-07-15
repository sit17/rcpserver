package com.i5i58.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class CheckIdCard {

	public static boolean checkIdCardPost(String realName, String idCard) {
		String host = "http://aliyun.id98.cn";
		String path = "/idcard";
		String method = "GET";
		String authorization = "APPCODE 4c0eb43f3828444f85c79d8b299bc04c";
		Map<String, String> headers = new HashMap<String, String>();

		Map<String, String> querys = new HashMap<String, String>();
		querys.put("cardno", idCard);
		querys.put("name", realName);
		try {

			return doGet(host + path+"?", authorization, querys);
			
			//System.out.println(EntityUtils.toString(entity));
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static boolean doGet(String url, String authorization, Map<String, String> postParams)
			throws ClientProtocolException, IOException {

		boolean isSuccess=false;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			String getParams="";
			for (Map.Entry<String, String> params : postParams.entrySet()) {
				getParams+=params.getKey()+"="+ params.getValue()+"&";
			}
			if(getParams.length()>0)
			{
				getParams=getParams.substring(0, getParams.length()-1);			
			}

			HttpGet httpGet = new HttpGet(url+getParams);
			// 设置请求的header
			httpGet.addHeader("Authorization", authorization);

			response = httpClient.execute(httpGet);

			String returnJson = EntityUtils.toString(response.getEntity(), "utf-8");
	
			System.out.println("RETURN:"+returnJson);
			//HttpEntity entity = response.getEntity();
			AuthIdCard authIdCard=new JsonUtils().toObject(returnJson, AuthIdCard.class);

			System.out.println(""+authIdCard.getCode());
			if(authIdCard.getCode()==1)
			{
				isSuccess=true;
			}
		} finally {
			httpClient.close();
		}
		return isSuccess;
	}
}
