package com.i5i58.service.android.channel.async;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class MsgYxChatDriftComment extends MsgYxChatIdentity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1320780686358713677L;
	/**
	 * from 头像
	 */
	private String face = "";

	/**
	 * 弹幕内容
	 */
	private String content = "";

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
