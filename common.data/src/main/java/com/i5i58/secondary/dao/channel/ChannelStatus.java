package com.i5i58.secondary.dao.channel;

import com.i5i58.util.KVCode;

public enum ChannelStatus implements KVCode{
	OPEN("open", 1), Close("close", 2), Nullity("nullity", 3);
	
	private String code;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private ChannelStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}

}
