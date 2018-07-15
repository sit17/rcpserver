package com.i5i58.message.service;

import java.util.Map;

public class RequestMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558569100385824092L;

	private String cmd;

	private Map<String, String> params;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}
