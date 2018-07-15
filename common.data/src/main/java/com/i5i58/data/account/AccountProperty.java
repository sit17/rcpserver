package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AccountPropertys", uniqueConstraints = { @UniqueConstraint(columnNames = { "accId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class AccountProperty implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4036103146875313239L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 20)
	private long score = 0L;

	@Column(nullable = false, length = 20)
	private long richScore = 0L;

	@Column(nullable = false, length = 2)
	private int vip = 0;

	@Column(nullable = false, length = 20)
	private long vipDeadline = 0L;

	/**
	 * 包年、包月。。。
	 */
	@Column(nullable = false, length = 1)
	private byte vipPurchase = (byte)0;

	@Column(nullable = false, length = 11)
	private int mountsId = 0;

	@Column(nullable = true, length = 32)
	private String mountsName = "";

	@Column(nullable = true, length = 32)
	private String clubCid = "";

	@Column(nullable = true, length = 32)
	private String clubName = "";

	@Column(nullable = false, length = 11)
	private int fansCount;

	@Column(nullable = false, length = 11)
	private int focusCount;

	@Column(nullable = false, length = 11)
	private int essayCount;

	@Column(nullable = true, length = 255)
	private String medals;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
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

	public String getMedals() {
		return medals;
	}

	public void setMedals(String medals) {
		this.medals = medals;
	}

	public int getEssayCount() {
		return essayCount;
	}

	public void setEssayCount(int essayCount) {
		this.essayCount = essayCount;
	}

	public long getRichScore() {
		return richScore;
	}

	public void setRichScore(long richScore) {
		this.richScore = richScore;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public long getVipDeadline() {
		return vipDeadline;
	}

	public void setVipDeadline(long vipDeadline) {
		this.vipDeadline = vipDeadline;
	}

	public byte getVipPurchase() {
		return vipPurchase;
	}

	public void setVipPurchase(byte vipPurchase) {
		this.vipPurchase = vipPurchase;
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

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getClubCid() {
		return clubCid;
	}

	public void setClubCid(String clubCid) {
		this.clubCid = clubCid;
	}
 
}
