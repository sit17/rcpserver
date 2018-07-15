package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@RedisHash("HotChannels")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannel implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5166009484348104351L;
	// Define Id properties
	@Id
	@JsonProperty("cId")
	private String id = "";

	private String channelId = "";

	@Indexed
	private String ownerId = "";

	private String channelName = "";

	@Indexed
	private int type = 0;

	@Indexed
	private int status = 0;
	
	private String coverUrl = "";
	private String gId = "";
	private String channelNotice = "";
	private String yunXinCId = "";
	private String yunXinRId = "";
	private int pushDevice = 0;

	@JsonIgnore
	private String pushUrl = "";

	private String httpPullUrl = "";
	private String hlsPullUrl = "";
	private String rtmpPullUrl = "";

	@JsonIgnore
	private String ConnCid = "";

	private String location = "";

	@Indexed
	private int playerCount = 0;

	@Indexed
	private int playerTimes = 0;

	@Indexed
	private long weekOffer = 0;

	private int heartCount = 0;
	private int heartUserCount = 0;

	@Indexed
	private long brightness = 0;

	private String title = "";

	private String clubName = "";
	private long clubScore = 0L;
	private int clubLevel = 0;
	private String clubTitle = "";
	private String clubIcon = "";
	private int clubMemberCount = 0;

	@Indexed
	private int hot = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getPushDevice() {
		return pushDevice;
	}

	public void setPushDevice(int pushDevice) {
		this.pushDevice = pushDevice;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
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

	public String getConnCid() {
		return ConnCid;
	}

	public void setConnCid(String connCid) {
		ConnCid = connCid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public int getPlayerTimes() {
		return playerTimes;
	}

	public void setPlayerTimes(int playerTimes) {
		this.playerTimes = playerTimes;
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

}
