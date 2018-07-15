package com.i5i58.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="UserTaskRecord",uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_EMPTY)
public class UserTaskRecord implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4348409266693870164L;

	@Id
	@JsonIgnore
	@Column(nullable = false, length = 64)
	private String id;
	
	@Column(nullable = false, length = 32)
	private String accId = "";
	
	@Column(nullable = false)
	private int taskType = 0;
	
	@Column(nullable = false)
	private long score = 0L;	//任务获得的积分
	
	@Column(nullable = false)
	private long completeDate = 0L; //任务完成时间
//
//	@Column(nullable = false)
//	private int performTimes = 0;	//执行的次数
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public long getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(long completeDate) {
		this.completeDate = completeDate;
	}
//	public int getPerformTimes() {
//		return performTimes;
//	}
//	public void setPerformTimes(int performTimes) {
//		this.performTimes = performTimes;
//	}

}
