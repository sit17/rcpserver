/**
 * 记录当天完成任务获得的积分
 * */
package com.i5i58.data.channel;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RedisHash("HotClubTaskScoreRecord")
@JsonInclude(Include.NON_DEFAULT)
public class HotClubTaskScoreRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7168911504779719742L;
	
	/**
	 * 使用cId + "_" + accId
	 * */
	@Id
	String id = ""; 
	
	String cId = "";
	String accId = "";

	/**
	 * 当天获得的任务积分
	 * */
	long taskScore = 0L;

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

	public long getTaskScore() {
		return taskScore;
	}

	public void setTaskScore(long taskScore) {
		this.taskScore = taskScore;
	} 
	
}
