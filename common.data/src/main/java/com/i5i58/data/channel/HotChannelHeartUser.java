package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelHeartUsers")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelHeartUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4839469394598171044L;

	@Id
	@JsonIgnore
	private String id = "";

	@Indexed
	private String cId = "";

	@Indexed
	private String accId = "";

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

}
