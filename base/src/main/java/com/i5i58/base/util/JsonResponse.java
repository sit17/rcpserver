package com.i5i58.base.util;

import com.alibaba.fastjson.JSONObject;

public class JsonResponse {
	private JSONObject response;
	
	public JsonResponse(){
		response = new JSONObject();
		JSONObject data = new JSONObject();
		response.put("data", data);
	}
	
	public void setCmd(String cmd){
		response.put("cmd", cmd);
	}
	
	public void setValue(String key, Object value){
		JSONObject data = response.getJSONObject("data");
		data.put(key, value);
	}
	
	public Object getValue(String key){
		JSONObject data = response.getJSONObject("data");
		return data.get(key);
	}
	
	public JSONObject getResponse(){
		return response;
	}
	
	public JSONObject getData(){
		JSONObject data = response.getJSONObject("data");
		return data;
	}
}
