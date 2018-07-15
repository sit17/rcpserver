package com.i5i58.service.android;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class SQLResultData  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8023008621332345923L;
	private int reutrnCode = 0;
	private String describe = "";
	private Object data = null;
	
	public int getReutrnCode() {
		return reutrnCode;
	}
	public void setReutrnCode(int reutrnCode) {
		this.reutrnCode = reutrnCode;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
