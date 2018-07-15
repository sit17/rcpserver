package com.i5i58.data.group;

import com.i5i58.util.KVCode;

public enum GroupAuth  implements KVCode {
	 
	/**
	 * 委派旗下组管理账号 =1
	 */
	ASSIGN_GROUP_ADMIN("委派管理账号", 1),
	/**
	 * 委派组拥有者账号 =2
	 */
	ASSIGN_GROUP_OWNER("委派组拥有者账号", 2),
	/**
	 * 申请签约或解约 =4
	 */
	ACCESS_CONTRACT("操作合约", 4),
	/**
	 * 修改组信息 =8
	 */
	ASSIGN_EDIT_INFO_AUTHORITY("修改组信息", 8);
	
	
	 
	
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

	private GroupAuth(String code, int value) {
		this.code = code;
		this.value = value;
	}
}
