package com.i5i58.data.channel;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ConnectMicInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750944988660857677L;

	private String cId = "";
	
	private String ownerId = "";
	
	private String stageName = "";
	
	private String faceUrl = "";

	private String httpPullUrl = "";
	private String hlsPullUrl = "";
	private String rtmpPullUrl = "";
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public String getFaceUrl() {
		return faceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}
	public String getHttpPullUrl() {
		return httpPullUrl;
	}
	public void setHttpPullUrl(String httpPullUrl) {
		this.httpPullUrl = httpPullUrl;
	}
	public String getHlsPullUrl() {
		return hlsPullUrl;
	}
	public void setHlsPullUrl(String hlsPullUrl) {
		this.hlsPullUrl = hlsPullUrl;
	}
	public String getRtmpPullUrl() {
		return rtmpPullUrl;
	}
	public void setRtmpPullUrl(String rtmpPullUrl) {
		this.rtmpPullUrl = rtmpPullUrl;
	}
	
	
}
