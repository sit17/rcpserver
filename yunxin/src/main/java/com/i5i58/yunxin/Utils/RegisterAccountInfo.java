package com.i5i58.yunxin.Utils;

public class RegisterAccountInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7980874547938595201L;

	private String token;
	
	private String accid;
	
	private String name;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
