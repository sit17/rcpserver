package com.i5i58.apis.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Data Transfer Object for Response
 * 
 * @author frank
 * @param <T>
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class ResultDataSet implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2157145182878477072L;

	private String code = "";
	private String msg = "";
	private Object data = null;

	@JsonIgnore
	private Object innerData = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getInnerData() {
		return innerData;
	}

	public void setInnerData(Object innerData) {
		this.innerData = innerData;
	}

}
