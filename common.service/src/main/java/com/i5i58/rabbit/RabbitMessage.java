package com.i5i58.rabbit;

import java.io.Serializable;

public class RabbitMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3669037674924022161L;

	public String cmd;
	
	public Serializable data;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}
	
}
