package com.i5i58.data.social;

public class FollowItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3555352518085781447L;
	private String accId;
	private byte type;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
