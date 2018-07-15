package com.i5i58.data.channel;

import com.i5i58.util.KVCode;

public enum ChGoodsType implements KVCode {
	CHANNEL_GIFT("礼物", 1), CHANNEL_DRIFT("弹幕", 2);
	
	private String code;
	private int value;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private ChGoodsType(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
