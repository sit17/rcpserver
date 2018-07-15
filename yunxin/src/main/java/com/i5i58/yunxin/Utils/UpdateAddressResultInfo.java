package com.i5i58.yunxin.Utils;

public class UpdateAddressResultInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6911499782795908257L;
	private String pushUrl;
	private String httpPullUrl;
	private String hlsPullUrl;
	private String rtmpPullUrl;
	private String name;
	public String getPushUrl() {
		return pushUrl;
	}
	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
