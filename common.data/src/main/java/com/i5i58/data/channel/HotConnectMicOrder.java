package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotConnectMicOrders")
@JsonInclude(Include.NON_DEFAULT)
public class HotConnectMicOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8138463174502638251L;
	
	@Id
	private String id = "";
	
	private String requestAccid = "";
	
	private String requestCId = "";
	
	private String targetAccid = "";
	
	private String targetCId = "";
	
	@TimeToLive
	private long expire;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequestAccid() {
		return requestAccid;
	}

	public void setRequestAccid(String requestAccid) {
		this.requestAccid = requestAccid;
	}

	public String getRequestCId() {
		return requestCId;
	}

	public void setRequestCId(String requestCId) {
		this.requestCId = requestCId;
	}

	public String getTargetAccid() {
		return targetAccid;
	}

	public void setTargetAccid(String targetAccid) {
		this.targetAccid = targetAccid;
	}

	public String getTargetCId() {
		return targetCId;
	}

	public void setTargetCId(String targetCId) {
		this.targetCId = targetCId;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
	
}
