package com.i5i58.data.social;

public class AttentionResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6247167378725456447L;

	private String accId;

	private String Attention;
	
	private String version;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAttention() {
		return Attention;
	}

	public void setAttention(String attention) {
		Attention = attention;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
