package com.i5i58.data.channel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelAdmins")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelAdmin implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6985166380339419879L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	private String accId = "";
	private String cId = "";
	private int adminRight;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public int getAdminRight() {
		return adminRight;
	}
	public void setAdminRight(int adminRight) {
		this.adminRight = adminRight;
	}
	
}
