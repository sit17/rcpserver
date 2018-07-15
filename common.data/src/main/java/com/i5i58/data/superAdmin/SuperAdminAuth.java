package com.i5i58.data.superAdmin;

import com.i5i58.util.KVCode;

public enum SuperAdminAuth implements KVCode {
	/**
	 * 基础权限,1
	 */
	BASE_AUTH("基础权限", 1), //0x00000001
	/**
	 * 运营管理权限,128
	 */
	OPERATION_ADMIN_AUTH("运营管理权限", 128),//0x00000080
	/**
	 * 运营控制权限,256
	 */
	OPERATION_CONTROL_AUTH("运营控制权限", 256),//0x00000100
	/**
	 * 超管控制权限,512
	 */
	SUPER_ADMIN_CONTROL_AUTH("超管控制权限", 512),//0x000000200
	/**
	 * OpenId操作权限（生成OpenId，生成靓号）,1024
	 */
	SYSTEM_CONTROL_AUTH("OpenId操作权限", 1024),//0x000000400
	
	/**
	 * 财务控制
	 * */
	FIANCE_AUTH("财务控制权限", 2048);//0x000000800
	
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

	private SuperAdminAuth(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
