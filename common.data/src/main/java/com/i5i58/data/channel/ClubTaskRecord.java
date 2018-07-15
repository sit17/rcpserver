package com.i5i58.data.channel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="ClubTaskRecord",uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ClubTaskRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4348409266693870164L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int id;
	
	@Column(nullable = false, length = 32)
	private String cId = "";
	@Column(nullable = false, length = 32)
	private String accId = "";
	@Column(nullable = false)
	private int taskId = 0;
	@Column(nullable = false)
	private long score = 0L;
	@Column(nullable = false)
	private long completeDate = 0L;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public long getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(long completeDate) {
		this.completeDate = completeDate;
	}
	
}
