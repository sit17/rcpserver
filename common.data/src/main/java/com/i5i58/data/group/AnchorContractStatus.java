package com.i5i58.data.group;

import com.i5i58.util.KVCode;

public enum AnchorContractStatus implements KVCode {
	REQUESTED("requested", 0), AGREED("agreed", 1), REJECTED("rejected", 2), REQUEST_CANCEL("request_cancel",
			3), CANCELED("canceled", 4), REMOVED("removed", 5);

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

	private AnchorContractStatus(String code, int value) {
		this.code = code;
		this.value = value;
	}

	public static AnchorContractStatus getStatus(int value) {
		switch (value) {
		case 0:
			return AnchorContractStatus.REQUESTED;
		case 1:
			return AnchorContractStatus.AGREED;
		case 2:
			return AnchorContractStatus.REJECTED;
		case 3:
			return AnchorContractStatus.REQUEST_CANCEL;
		case 4:
			return AnchorContractStatus.CANCELED;
		case 5:
			return AnchorContractStatus.REMOVED;
		}
		return null;
	}
}