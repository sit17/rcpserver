package com.i5i58.service.channel;

import com.i5i58.util.KVCode;

public enum CrossPlatformAction implements KVCode {
	NATIVE("native", 1),WEB("web",2), WEBVIEW("webview", 3),RN("rn", 4), APP("app", 5);

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

	private CrossPlatformAction(String code, int value) {
		this.code = code;
		this.value = value;
	}
}