package com.i5i58.data.home;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Data Entity
 * 
 * @author Ivan Dong
 *
 */
@Entity
@Table(name = "Carousels")
@JsonInclude(Include.NON_EMPTY)
// Custom Query bind to the Object
public class Carousel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5849145274627772513L;
	// Define Id properties
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int device;
	private int position;
	private String imgUrl;
	private String action;
	private String params;
	private String cId;
	private String coverUrl;
	private String hlsPullUrl;
	private String httpPullUrl;
	private String rtmpPullUrl;
	private String yunXinRId;
	private long startTime;
	private long endTime;
	private String zegoRtmpUrl = "";
	private String zegoHlsUrl = "";
	private String zegoHdlUrl = "";
	private String zegoStreamAlias = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getHlsPullUrl() {
		return hlsPullUrl;
	}

	public void setHlsPullUrl(String hlsPullUrl) {
		this.hlsPullUrl = hlsPullUrl;
	}

	public String getHttpPullUrl() {
		return httpPullUrl;
	}

	public void setHttpPullUrl(String httpPullUrl) {
		this.httpPullUrl = httpPullUrl;
	}

	public String getRtmpPullUrl() {
		return rtmpPullUrl;
	}

	public void setRtmpPullUrl(String rtmpPullUrl) {
		this.rtmpPullUrl = rtmpPullUrl;
	}

	public String getYunXinRId() {
		return yunXinRId;
	}

	public void setYunXinRId(String yunXinRId) {
		this.yunXinRId = yunXinRId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getZegoRtmpUrl() {
		return zegoRtmpUrl;
	}

	public void setZegoRtmpUrl(String zegoRtmpUrl) {
		this.zegoRtmpUrl = zegoRtmpUrl;
	}

	public String getZegoHlsUrl() {
		return zegoHlsUrl;
	}

	public void setZegoHlsUrl(String zegoHlsUrl) {
		this.zegoHlsUrl = zegoHlsUrl;
	}

	public String getZegoHdlUrl() {
		return zegoHdlUrl;
	}

	public void setZegoHdlUrl(String zegoHdlUrl) {
		this.zegoHdlUrl = zegoHdlUrl;
	}

	public String getZegoStreamAlias() {
		return zegoStreamAlias;
	}

	public void setZegoStreamAlias(String zegoStreamAlias) {
		this.zegoStreamAlias = zegoStreamAlias;
	}
}
