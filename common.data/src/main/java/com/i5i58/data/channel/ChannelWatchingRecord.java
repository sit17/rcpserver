package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author songfl
 * 记录用户每天观看频道的时间
 */
@Entity
@Table(name="ChannelWatchingRecord", uniqueConstraints = { @UniqueConstraint(columnNames = "id")})
@JsonInclude(Include.NON_DEFAULT)
public class ChannelWatchingRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6117625297037294846L;


	@Id
	@JsonIgnore
	@Column(nullable = false, length = 65)
	private String id = "";
	
	
	@Column(nullable = false, length = 32)
	private String cId = "";
	
	@Column(nullable = false, length = 32)
	private String accId = "";
	
	/**
	 * 累计当天观看的时间
	 * */
	private long duration;
	
	
	/**
	 * 本次开始观看时间
	 * */
	private long startTime;
	/**
	 * 本次结束观看时间,减去startTime，累计到watchingTime
	 * */
	private long finishTime;

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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
