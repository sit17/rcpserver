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
@Table(name = "ChannelFansClubs", uniqueConstraints = { @UniqueConstraint(columnNames = "id")})
@JsonInclude(Include.NON_DEFAULT)
public class ChannelFansClub implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3748750106723650569L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false, length = 32)
	private String cId = "";
	private String accId = "";

	private long endDate = 0L;
	
	//private String clubName = "";
	private long personalScore = 0L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

//	public String getClubName() {
//		return clubName;
//	}
//
//	public void setClubName(String clubName) {
//		this.clubName = clubName;
//	}

	public long getPersonalScore() {
		return personalScore;
	}

	public void setPersonalScore(long personalScore) {
		this.personalScore = personalScore;
	}
}
