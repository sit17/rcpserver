package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotChannelViewers")
@JsonInclude(Include.NON_DEFAULT)
public class HotChannelViewer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3579617813006968536L;

	@Id
	@JsonIgnore
	private String id;

	@Indexed
	private String cId = "";

	@Indexed
	private String accId = "";

	private String name = "";

	private String faceSmallUrl = "";

	private int vip;

	private long vipDeadLine;

	private int guardLevel;

	private long guardDeadLine;

	private long richScore;

	private long score;

	private int fansClub;

	private String clubName = "";

	private int clubLevel;

	private long clubDeadLine;

	private long fansClubScore;

	private int majia;

	private int adminRight;
	@JsonIgnore
	private int mountsId;
	@JsonIgnore
	private String mountsName;

	@JsonIgnore
	private boolean superUser = false;

	@Indexed
	@JsonIgnore
	private boolean isAndroid;

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

	public int getGuardLevel() {
		return guardLevel;
	}

	public void setGuardLevel(int guardLevel) {
		this.guardLevel = guardLevel;
	}

	public long getGuardDeadLine() {
		return guardDeadLine;
	}

	public void setGuardDeadLine(long guardDeadLine) {
		this.guardDeadLine = guardDeadLine;
	}

	public int getFansClub() {
		return fansClub;
	}

	public void setFansClub(int fansClub) {
		this.fansClub = fansClub;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public int getClubLevel() {
		return clubLevel;
	}

	public void setClubLevel(int clubLevel) {
		this.clubLevel = clubLevel;
	}

	public int getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(int adminRight) {
		this.adminRight = adminRight;
	}

	public long getClubDeadLine() {
		return clubDeadLine;
	}

	public void setClubDeadLine(long clubDeadLine) {
		this.clubDeadLine = clubDeadLine;
	}

	public long getFansClubScore() {
		return fansClubScore;
	}

	public void setFansClubScore(long fansClubScore) {
		this.fansClubScore = fansClubScore;
	}

	public int getMajia() {
		return majia;
	}

	public void setMajia(int majia) {
		this.majia = majia;
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

	public boolean isAndroid() {
		return isAndroid;
	}

	public void setAndroid(boolean isAndroid) {
		this.isAndroid = isAndroid;
	}

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}
}
