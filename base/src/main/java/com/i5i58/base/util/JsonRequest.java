package com.i5i58.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonRequest {
	private JSONObject request;
	
	public JsonRequest(){
	}
	
//	public JsonRequest(String text){
//		request = JSON.parseObject(text);
//	}
	
	public boolean parseObject(String text){
		request = JSON.parseObject(text);
		return request != null;
	}
	
	public JSONObject getRequest(){
		return request;
	}
	
	public JSONObject getData(){
		if (request == null)
			return null;
		return request.getJSONObject("data");
	}
	
	public String getCmd(){
		if (request == null)
			return null;
		String cmd = request.getString("cmd");
		if (cmd == null){
			return null;
		}
		return cmd;
	}
	
	public void setValue(String key, Object value){
		if (request == null)
			return;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return;
		data.put(key, value);
	}

	public Object getValue(String key){
		if (request == null)
			return null;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return null;
		return data.get(key);
	}
	
	public int getIntValue(String key){
		if (request == null)
			return 0;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return 0;
		return data.getIntValue(key);
	}
	
	public long getLongValue(String key){
		if (request == null)
			return 0L;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return 0L;
		return data.getLongValue(key);
	}
	
	public String getString(String key){
		if (request == null)
			return null;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return null;
		return data.getString(key);
	}
	
	public boolean getBooleanValue(String key){
		if (request == null)
			return false;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return false;
		return data.getBooleanValue(key);
	}
	
	public byte getByteValue(String key){
		if (request == null)
			return 0;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return 0;
		return data.getByteValue(key);
	}
	
	public JSONObject getJSONObject(String key){
		if (request == null)
			return null;
		JSONObject data = request.getJSONObject("data");
		if (data == null)
			return null;
		return data.getJSONObject(key);
	}
	public String toJSONString(){
		if (request == null){
			return null;
		}
		return request.toJSONString();
	}
}
