package com.i5i58.data.channel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="ClubTaskConfig")
@JsonInclude(Include.NON_DEFAULT)
public class ClubTaskConfig implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9179694761871495356L;

	@Id
	private int taskId = 0;
	
	private long taskScore = 0L;
	private String taskDesc = "";
	
	//private int multiple = 1;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public long getTaskScore() {
		return taskScore;
	}
	public void setTaskScore(long taskScore) {
		this.taskScore = taskScore;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
}
