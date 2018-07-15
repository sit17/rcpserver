package com.i5i58.data.pay;

import java.io.Serializable;

public class ApplePayItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1723036746061922260L;

	private String name;
	
	private String id;
	
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
