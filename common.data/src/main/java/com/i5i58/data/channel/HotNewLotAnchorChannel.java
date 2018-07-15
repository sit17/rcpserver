package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("HotNewLotAnchorChannels")
@JsonInclude(Include.NON_DEFAULT)
public class HotNewLotAnchorChannel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4772240005356652970L;

	@Id
	@JsonProperty("cId")
	private String id = ""; // 唯一主键频道ID

	private String channelId = "";
	private String ownerId = "";
	private String channelName = "";
	private String title = "";
	private int type;
	private int status;
	private String coverUrl = "";
	private String gId = "";
	private String channelNotice = "";
	private String yunXinRId = "";
	private String httpPullUrl = "";
	private String hlsPullUrl = "";
	private String rtmpPullUrl = "";

	@Indexed
	private int sortId;

	/**
	 * 新秀或大家都在看
	 */
	@Indexed
	private int newLot;

	public String getcId() {
		return id;
	}

	public void setcId(String cId) {
		this.id = cId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getChannelNotice() {
		return channelNotice;
	}

	public void setChannelNotice(String channelNotice) {
		this.channelNotice = channelNotice;
	}

	public String getYunXinRId() {
		return yunXinRId;
	}

	public void setYunXinRId(String yunXinRId) {
		this.yunXinRId = yunXinRId;
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

	public int getNewLot() {
		return newLot;
	}

	public void setNewLot(int newLot) {
		this.newLot = newLot;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

}
