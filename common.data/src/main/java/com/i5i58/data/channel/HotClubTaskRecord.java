package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotClubTaskRecord")
@JsonInclude(Include.NON_DEFAULT)
public class HotClubTaskRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 500315437642032138L;

	@Id
	private String id = "";
	
	@Indexed
	private String cId = "";
	
	@Indexed
	private String accId = "";

	private int taskId;
	
	private long score = 0L;
	
	private long completeDate;

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

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
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

}
