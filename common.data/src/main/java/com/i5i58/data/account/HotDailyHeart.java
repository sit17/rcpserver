package com.i5i58.data.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotDailyHearts")
@JsonInclude(Include.NON_DEFAULT)
public class HotDailyHeart implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5658202569411391127L;

	@Id
	String accId = "";
	
	int heart;
	
	@JsonIgnore
	int heartMax;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}

	public int getHeartMax() {
		return heartMax;
	}

	public void setHeartMax(int heartMax) {
		this.heartMax = heartMax;
	}
	
}
