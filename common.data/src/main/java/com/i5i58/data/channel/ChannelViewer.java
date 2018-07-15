package com.i5i58.data.channel;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ChannelViewer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5639371485031937984L;
	
	private String accId = "";
	
	private String name = "";
	
	private String faceUrl = "";
	
	private int vip;
	
	private int guard;
	
	private int guardCount;
	
	private int mounts;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getGuard() {
		return guard;
	}

	public void setGuard(int guard) {
		this.guard = guard;
	}

	public int getGuardCount() {
		return guardCount;
	}

	public void setGuardCount(int guardCount) {
		this.guardCount = guardCount;
	}

	public int getMounts() {
		return mounts;
	}

	public void setMounts(int mounts) {
		this.mounts = mounts;
	}
	
	
	
}
