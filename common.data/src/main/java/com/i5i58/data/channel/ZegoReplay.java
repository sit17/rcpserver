package com.i5i58.data.channel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ZegoReplays")
@JsonInclude(Include.NON_DEFAULT)
public class ZegoReplay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private int liveId = 0;

	@Column(nullable = false, length = 32)
	private String cId = "";

	@Column(nullable = false)
	private String streamAlias = "";

	@Column(nullable = false)
	private String title = "";

	@Column(nullable = false, length = 32)
	private String publishId = "";

	@Column(nullable = false)
	private String publishName = "";

	@Column(nullable = false)
	private int onlineNums = 0;
	
	@Column(nullable = false)
	private int playerCount = 0;

	@Column(nullable = false, length = 1024)
	private String replayUrl = "";
	
	@Column(nullable = false)
	private long beginTime = 0;

	@Column(nullable = false)
	private long endTime = 0;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLiveId() {
		return liveId;
	}

	public void setLiveId(int liveId) {
		this.liveId = liveId;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getStreamAlias() {
		return streamAlias;
	}

	public void setStreamAlias(String streamAlias) {
		this.streamAlias = streamAlias;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublishId() {
		return publishId;
	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public int getOnlineNums() {
		return onlineNums;
	}

	public void setOnlineNums(int onlineNums) {
		this.onlineNums = onlineNums;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public String getReplayUrl() {
		return replayUrl;
	}

	public void setReplayUrl(String replayUrl) {
		this.replayUrl = replayUrl;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
