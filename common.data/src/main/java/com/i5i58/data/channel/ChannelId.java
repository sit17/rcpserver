package com.i5i58.data.channel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ChannelIds")
@JsonInclude(Include.NON_DEFAULT)
public class ChannelId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32)
	private String channelId = "";

	@Column(nullable = false)
	private boolean used = false;

	@Column(nullable = false)
	private long usedDate = 0;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public long getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(long usedDate) {
		this.usedDate = usedDate;
	}
}
