package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "RemovedChannels", uniqueConstraints = { @UniqueConstraint(columnNames = "cId") })
@JsonInclude(Include.NON_DEFAULT)
public class RemovedChannel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false, length = 32)
	private String cId = ""; // 唯一主键频道ID

	/**
	 * 主播信息
	 */
	@Column(nullable = false)
	private String channelId = "";
	@Column(nullable = false, unique = true)
	private String ownerId = "";
	@Column(nullable = false)
	private String channelName = "";
	@Column(nullable = false)
	private int type = 0;
	@Column(nullable = false)
	private int status = 0;
	@Column(nullable = false)
	@JsonIgnore
	private long statusChanged=0;
	@Column(nullable = false)
	private String coverUrl = "";
	@Column(nullable = false)
	private String gId = "";
	@Column(nullable = false)
	private String channelNotice = "";
	@Column(nullable = false)
	private String yunXinCId = "";
	@Column(nullable = false)
	private String yunXinRId = "";
	@Column(nullable = false)
	private String title = "";
	@Column(nullable = false)
	@JsonIgnore
	private String pushUrl = "";
	@Column(nullable = false)
	private String httpPullUrl = "";
	@Column(nullable = false)
	private String hlsPullUrl = "";
	@Column(nullable = false)
	private String rtmpPullUrl = "";
	@Column(nullable = false)
	private boolean nullity = false;
	@Column(nullable = false)
	@JsonIgnore
	private long createDate = 0;
	@Column(nullable = false)
	@JsonIgnore
	private String createIp = "";

	@Column(nullable = false)
	private String location = "";
	
	/**
	 * 粉丝团名称
	 * */
	
	@Column(nullable = false, length = 3)
	private String clubName = "";
	
	/**
	 * 粉丝团积分
	 * */
	@Column(nullable = false)
	private long clubScore = 0L;
	
	/**
	 * 粉丝团等级
	 * */
	@Column(nullable = false)
	private int clubLevel = 0;
	
	/**
	 * 粉丝团明星头衔
	 * */
	@Column(nullable = false)
	private String clubTitle = "";
	
	private String clubIcon = "";
	private int clubMemberCount = 0;
	/**
	 * 频道创建者id
	 * */
	@Column(nullable = false)
	private String creatorId = "";

	@Column
	private int playerCount = 0;
	private int playerTimes = 0;
	private long weekOffer = 0;
	private int heartCount = 0;
	private int heartUserCount = 0;
	private long brightness = 0;

	@Column(nullable = false, length=1024)
	private String zegoPublishUrl = "";
	@Column(nullable = false, length=1024)
	private String zegoRtmpUrl = "";
	@Column(nullable = false, length=1024)
	private String zegoHlsUrl = "";
	@Column(nullable = false, length=1024)
	private String zegoHdlUrl = "";
	@Column(nullable = false, length=255)
	private String zegoStreamId = "";
	@Column(nullable = false)
	private int zegoLiveId = 0;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getStatusChanged() {
		return statusChanged;
	}
	public void setStatusChanged(long statusChanged) {
		this.statusChanged = statusChanged;
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
	public String getYunXinCId() {
		return yunXinCId;
	}
	public void setYunXinCId(String yunXinCId) {
		this.yunXinCId = yunXinCId;
	}
	public String getYunXinRId() {
		return yunXinRId;
	}
	public void setYunXinRId(String yunXinRId) {
		this.yunXinRId = yunXinRId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
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
	public boolean isNullity() {
		return nullity;
	}
	public void setNullity(boolean nullity) {
		this.nullity = nullity;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public String getCreateIp() {
		return createIp;
	}
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public long getClubScore() {
		return clubScore;
	}
	public void setClubScore(long clubScore) {
		this.clubScore = clubScore;
	}
	public int getClubLevel() {
		return clubLevel;
	}
	public void setClubLevel(int clubLevel) {
		this.clubLevel = clubLevel;
	}
	public String getClubTitle() {
		return clubTitle;
	}
	public void setClubTitle(String clubTitle) {
		this.clubTitle = clubTitle;
	}
	public String getClubIcon() {
		return clubIcon;
	}
	public void setClubIcon(String clubIcon) {
		this.clubIcon = clubIcon;
	}
	public int getClubMemberCount() {
		return clubMemberCount;
	}
	public void setClubMemberCount(int clubMemberCount) {
		this.clubMemberCount = clubMemberCount;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public int getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	public int getPlayerTimes() {
		return playerTimes;
	}
	public void setPlayerTimes(int playerTimes) {
		this.playerTimes = playerTimes;
	}
	public long getWeekOffer() {
		return weekOffer;
	}
	public void setWeekOffer(long weekOffer) {
		this.weekOffer = weekOffer;
	}
	public int getHeartCount() {
		return heartCount;
	}
	public void setHeartCount(int heartCount) {
		this.heartCount = heartCount;
	}
	public int getHeartUserCount() {
		return heartUserCount;
	}
	public void setHeartUserCount(int heartUserCount) {
		this.heartUserCount = heartUserCount;
	}
	public long getBrightness() {
		return brightness;
	}
	public void setBrightness(long brightness) {
		this.brightness = brightness;
	}
	public String getZegoPublishUrl() {
		return zegoPublishUrl;
	}
	public void setZegoPublishUrl(String zegoPublishUrl) {
		this.zegoPublishUrl = zegoPublishUrl;
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
	public String getZegoStreamId() {
		return zegoStreamId;
	}
	public void setZegoStreamId(String zegoStreamId) {
		this.zegoStreamId = zegoStreamId;
	}
	public int getZegoLiveId() {
		return zegoLiveId;
	}
	public void setZegoLiveId(int zegoLiveId) {
		this.zegoLiveId = zegoLiveId;
	}
}
