package com.i5i58.data.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class HotAccount1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * accid
	 */
	@JsonProperty("accId")
	private String id = "";

	private String openId = "";

	@JsonIgnore
	private String phoneNo = "";

	/**
	 * nick name
	 */
	private String nickName = "";

	private String stageName = "";

	private boolean anchor;

	private byte gender;

	private long birthDate;

	private String faceSmallUrl = "";

	private String faceOrgUrl = "";

	private int version;

	private int vip;

	private long vipDeadLine;

	private long score;

	private long richScore;

	private int mountsId;

	private String mountsName = "";

	private String clubCid = "";

	private String clubName = "";

	// private long clubDeadLine = 0;

	private int fansCount;

	private int focusCount;

	private int essayCount;

	private String medals = "";

	private String location = "";

	private String signature = "";

	private String personalBrief = "";

	@JsonIgnore
	private boolean isAndroid = false;

	@JsonIgnore
	private boolean isFaceUseInGame = true;

	public HotAccount1(){
		
	}
	public HotAccount1(String id, String openId, String phoneNo, String nickName, String stageName, boolean anchor,
			byte gender, long birthDate, String faceSmallUrl, String faceOrgUrl, int version, int vip, long vipDeadLine,
			long richScore, long score, int mountsId, String mountsName, String clubCid, String clubName, int fansCount,
			int focusCount, int essayCount, String medals, String location, String signature, String personalBrief) {
		super();
		this.id = id;
		this.openId = openId;
		this.phoneNo = phoneNo;
		this.nickName = nickName;
		this.stageName = stageName;
		this.anchor = anchor;
		this.gender = gender;
		this.birthDate = birthDate;
		this.faceSmallUrl = faceSmallUrl;
		this.faceOrgUrl = faceOrgUrl;
		this.version = version;
		this.vip = vip;
		this.vipDeadLine = vipDeadLine;
		this.richScore = richScore;
		this.score = score;
		this.mountsId = mountsId;
		this.mountsName = mountsName;
		this.clubCid = clubCid;
		this.clubName = clubName;
		// this.clubDeadLine = clubDeadLine;

		this.fansCount = fansCount;
		this.focusCount = focusCount;
		this.essayCount = essayCount;

		this.medals = medals;
		this.location = location;
		this.signature = signature;
		this.personalBrief = personalBrief;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public boolean isAnchor() {
		return anchor;
	}

	public void setAnchor(boolean anchor) {
		this.anchor = anchor;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}

	public String getFaceSmallUrl() {
		return faceSmallUrl;
	}

	public void setFaceSmallUrl(String faceSmallUrl) {
		this.faceSmallUrl = faceSmallUrl;
	}

	public String getFaceOrgUrl() {
		return faceOrgUrl;
	}

	public void setFaceOrgUrl(String faceOrgUrl) {
		this.faceOrgUrl = faceOrgUrl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public long getVipDeadLine() {
		return vipDeadLine;
	}

	public void setVipDeadLine(long vipDeadLine) {
		this.vipDeadLine = vipDeadLine;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getMountsId() {
		return mountsId;
	}

	public void setMountsId(int mountsId) {
		this.mountsId = mountsId;
	}

	public String getMountsName() {
		return mountsName;
	}

	public void setMountsName(String mountsName) {
		this.mountsName = mountsName;
	}

	public String getClubCId() {
		return clubCid;
	}

	public void setClubCId(String clubCid) {
		this.clubCid = clubCid;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public int getFocusCount() {
		return focusCount;
	}

	public void setFocusCount(int focusCount) {
		this.focusCount = focusCount;
	}

	public int getEssayCount() {
		return essayCount;
	}

	public void setEssayCount(int essayCount) {
		this.essayCount = essayCount;
	}

	public String getMedals() {
		return medals;
	}

	public void setMedals(String medals) {
		this.medals = medals;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPersonalBrief() {
		return personalBrief;
	}

	public void setPersonalBrief(String personalBrief) {
		this.personalBrief = personalBrief;
	}

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	public boolean isFaceUseInGame() {
		return isFaceUseInGame;
	}

	public void setFaceUseInGame(boolean isFaceUseInGame) {
		this.isFaceUseInGame = isFaceUseInGame;
	}

}
