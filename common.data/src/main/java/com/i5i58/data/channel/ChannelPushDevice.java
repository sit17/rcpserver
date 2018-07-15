package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ChannelPushDevice", uniqueConstraints = { @UniqueConstraint(columnNames = "pushId") })
public class ChannelPushDevice implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4090006985707433476L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pushId;

	@Column(nullable = false, length = 32)
	private String accId;

	@Column(nullable = false, length = 5)
	private int device;

	@Column(nullable = false, length = 255)
	private String serialNum;

	@Column(nullable = false, length = 255)
	private String model;

	@Column(nullable = false, length = 255)
	private String osVersion;

	@Column(nullable = false)
	private int pushQuality;

	@Column(nullable = false)
	private long pushBit;

	@Column(nullable = false)
	private int pushFPS;

	@Column(nullable = false)
	private boolean enableHardEncode;

	@Column(nullable = false)
	private int pushMode;

	@Column(nullable = false)
	private int pushMark;

	public long getPushId() {
		return pushId;
	}

	public void setPushId(long pushId) {
		this.pushId = pushId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public int getPushQuality() {
		return pushQuality;
	}

	public void setPushQuality(int pushQuality) {
		this.pushQuality = pushQuality;
	}

	public long getPushBit() {
		return pushBit;
	}

	public void setPushBit(long pushBit) {
		this.pushBit = pushBit;
	}

	public int getPushFPS() {
		return pushFPS;
	}

	public void setPushFPS(int pushFPS) {
		this.pushFPS = pushFPS;
	}

	public boolean isEnableHardEncode() {
		return enableHardEncode;
	}

	public void setEnableHardEncode(boolean enableHardEncode) {
		this.enableHardEncode = enableHardEncode;
	}

	public int getPushMode() {
		return pushMode;
	}

	public void setPushMode(int pushMode) {
		this.pushMode = pushMode;
	}

	public int getPushMark() {
		return pushMark;
	}

	public void setPushMark(int pushMark) {
		this.pushMark = pushMark;
	}
}
