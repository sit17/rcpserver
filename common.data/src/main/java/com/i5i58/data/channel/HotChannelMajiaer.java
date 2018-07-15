package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelMajiaers")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelMajiaer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 599770142836698730L;

	@Id
	@JsonIgnore
	private String id = "";

	@Indexed
	private String cId = "";

	@Indexed
	private String accId = "";

	private int majia;
	
	private int guardLevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getMajia() {
		return majia;
	}

	public void setMajia(int majia) {
		this.majia = majia;
	}

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

}
