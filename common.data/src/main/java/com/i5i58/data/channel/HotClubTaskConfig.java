package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotClubTaskConfig")
@JsonInclude(Include.NON_DEFAULT)
public class HotClubTaskConfig implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1071795015319615410L;

	@Id
	@JsonProperty("taskId")
	private int id = 0;
	
	private long taskScore = 0L;
	private String taskDesc = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
