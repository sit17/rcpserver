package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelMounts")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelMount implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3094074625283015325L;

	@Id
	private int id;

	private long price;
	private String name = "";
	private boolean forGuard;
	private boolean forVip;
	private String version = "";
	private String function = "";
	private String path = "";
	private boolean forFansClubs;
	/**
	 * 有效期
	 */
	private int validity;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isForGuard() {
		return forGuard;
	}
	public void setForGuard(boolean forGuard) {
		this.forGuard = forGuard;
	}
	public boolean isForVip() {
		return forVip;
	}
	public void setForVip(boolean forVip) {
		this.forVip = forVip;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isForFansClubs() {
		return forFansClubs;
	}
	public void setForFansClubs(boolean forFansClubs) {
		this.forFansClubs = forFansClubs;
	}
	public int getValidity() {
		return validity;
	}
	public void setValidity(int validity) {
		this.validity = validity;
	}
}
