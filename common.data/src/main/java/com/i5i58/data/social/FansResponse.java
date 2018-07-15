package com.i5i58.data.social;

public class FansResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6033306082164973519L;

	private String accId;

	private String fans;

	private String version;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
