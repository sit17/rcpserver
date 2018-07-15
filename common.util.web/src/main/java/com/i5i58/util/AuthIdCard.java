package com.i5i58.util;

import java.io.Serializable;

public class AuthIdCard implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6554457185146830474L;
	
	private int isok;
	private int code;
	private Object data;
	
	public int getIsok() {
		return isok;
	}
	public void setIsok(int isok) {
		this.isok = isok;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	
}