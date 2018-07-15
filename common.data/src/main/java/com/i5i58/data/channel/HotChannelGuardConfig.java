package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelGuardConfigs")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelGuardConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5472788756200335999L;

	@Id
	private int id;

	@Indexed
	private int level;
	
	@Indexed
	private int month;

	private long price;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}


}
