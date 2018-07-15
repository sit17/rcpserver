package com.i5i58.service.channel;

import com.i5i58.util.KVCode;

public enum CrossPlatformTarget implements KVCode {
	ENTER_CHANNEL("enter_channel", 1), OPEN_GAME_APP("open_game_app", 2), OPEN_GAME("open_game", 3);

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

	private CrossPlatformTarget(String code, int value) {
		this.code = code;
		this.value = value;
	}
}