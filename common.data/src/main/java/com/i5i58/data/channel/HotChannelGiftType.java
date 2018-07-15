package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelGiftTypes")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelGiftType implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7567370938807457050L;

	@Id
	private int id;

	@Indexed
	private String node = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}
