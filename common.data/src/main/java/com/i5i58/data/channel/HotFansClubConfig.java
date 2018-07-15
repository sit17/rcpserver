package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotFansClubConfigs")
@JsonInclude(Include.NON_DEFAULT)
public class HotFansClubConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4417524237760837359L;

	@Id
	private int id; 
	
	private float discount;

	public int getMonth() {
		return id;
	}

	public void setMonth(int month) {
		this.id = month;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	} 
	
}
