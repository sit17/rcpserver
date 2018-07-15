package com.i5i58.service.channel.async;

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
	 * 弹幕内容
	 */
	private String content = "";

	private boolean levelUp;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isLevelUp() {
		return levelUp;
	}

	public void setLevelUp(boolean levelUp) {
		this.levelUp = levelUp;
	}
}
