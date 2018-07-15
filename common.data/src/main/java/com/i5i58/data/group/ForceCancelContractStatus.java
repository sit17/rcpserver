package com.i5i58.data.group;

import com.i5i58.util.KVCode;

public enum ForceCancelContractStatus implements KVCode {
	REQUESTED("requested", 0), AGREED("agreed", 1), REJECTED("rejected", 2);

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

	private ForceCancelContractStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public static ForceCancelContractStatus getStatus(int value) {
		switch (value){
		case 0:
			return ForceCancelContractStatus.REQUESTED;
		case 1:
			return ForceCancelContractStatus.AGREED;
		case 2:
			return ForceCancelContractStatus.REJECTED;
		}
		return null;
	}
}