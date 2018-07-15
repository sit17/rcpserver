package com.i5i58.message.service;

import com.i5i58.apis.constants.ResultDataSet;

public class WsResultDataSet extends ResultDataSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9141759711473712748L;

	private String cmd;

	public WsResultDataSet(ResultDataSet rds) {
		this.setCode(rds.getCode());
		this.setData(rds.getData());
		this.setMsg(rds.getMsg());
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

}
