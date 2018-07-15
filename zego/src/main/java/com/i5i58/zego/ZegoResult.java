package com.i5i58.zego;

import java.util.HashMap;

public class ZegoResult extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getCodeInt(){
		if (!this.containsKey("code"))
			return -1;
		int code =  (Integer) this.get("code");
		return code;
	}
	
	public String getMsg(){
		return (String) this.get("message");
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getData(){
		Object data =  this.get("data");
		if (data instanceof HashMap){
			return (HashMap<String, Object>)data;
		}
		return null;
	}
}
