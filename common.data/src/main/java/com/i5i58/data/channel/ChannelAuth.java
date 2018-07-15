package com.i5i58.data.channel;

import com.i5i58.util.KVCode;

public enum ChannelAuth implements KVCode {
	/**
	 * 禁言=1
	 */
	PROHIBIT_SPEAK("禁言", 1),
	/**
	 * 踢出房间=2
	 */
	KICKOUT_ROOM("踢出房间", 2),
	/**
	 * 委派旗下频道管理账号=4
	 */
	ASSIGN_ADMIN_AUTHORITY("委派管理账号", 4),
	/**
	 * 委派频道拥有者账号=8
	 */
	ASSIGN_OW_AUTHORITY("委派拥有者账号", 8),
	/**
	 * 指派用户频道荣誉=16
	 */
	ASSIGN_CHANNEL_USER_HONOR_AUTHORITY("指派用户频道荣誉", 16),
	/**
	 * 修改频道信息=32
	 */
	ASSIGN_EDIT_INFO_AUTHORITY("修改信息", 32),
	/**
	 * 操作麦序=64
	 */
	OPERATE_USER_MIC_SEQUENCE_AUTHORITY("操作麦序", 64);
	
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

	private ChannelAuth(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
