package com.i5i58.data.im;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class YxCustomMsg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8420234300479846204L;

	private String cmd = "";

	private Object data = null;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
