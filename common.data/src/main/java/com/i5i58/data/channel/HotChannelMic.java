package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelMics")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelMic implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2540299841724056700L;

	@Id
	@JsonIgnore
	private String id = "";

	@Indexed
	private String cId = "";

	@Indexed
	private String accId = "";

	@Indexed
	private int indexId;

	private String name = "";

	private String faceSmallUrl = "";

	private int vip;

	private long vipDeadLine;

	private int guardLevel;

	private long guardDeadLine;

	private long richScore;

	private boolean audioRight = false;

	@Indexed
	private long sitTime;

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

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaceSmallUrl() {
		return faceSmallUrl;
	}

	public void setFaceSmallUrl(String faceSmallUrl) {
		this.faceSmallUrl = faceSmallUrl;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public long getVipDeadLine() {
		return vipDeadLine;
	}

	public void setVipDeadLine(long vipDeadLine) {
		this.vipDeadLine = vipDeadLine;
	}

	public long getGuardDeadLine() {
		return guardDeadLine;
	}

	public void setGuardDeadLine(long guardDeadLine) {
		this.guardDeadLine = guardDeadLine;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}

	public long getSitTime() {
		return sitTime;
	}

	public void setSitTime(long sitTime) {
		this.sitTime = sitTime;
	}

	public boolean isAudioRight() {
		return audioRight;
	}

	public void setAudioRight(boolean audioRight) {
		this.audioRight = audioRight;
	}

}
