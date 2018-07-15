package com.i5i58.data.social;

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
@Table(name = "FollowInfos", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class FollowInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8361838518628986981L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id = 0L;

	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 32)
	private String fansId = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getFansId() {
		return fansId;
	}

	public void setFansId(String fansId) {
		this.fansId = fansId;
	}

}
