package com.i5i58.data.record;

import java.io.Serializable;

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
@Table(name = "ChannelOpeRecords", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class ChannelOpeRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false, length = 32)
	private String topGId = "";
	
	@Column(nullable = false, length = 32)
	private String operatorId = "";
	
	@Column(nullable = false, length = 32)
	private String cId = "";
	
	@Column(nullable = false, length = 32)
	private String sourceGId = "";
	
	@Column(nullable = false, length = 32)
	private String targetGId = "";
	
	@Column(nullable = false, length = 32)
	private String anchorId = "";
	
	@Column
	private int opeType = 0;

	@Column 
	private long datetime = 0L;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTopGId() {
		return topGId;
	}

	public void setTopGId(String topGId) {
		this.topGId = topGId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getSourceGId() {
		return sourceGId;
	}

	public void setSourceGId(String sourceGId) {
		this.sourceGId = sourceGId;
	}

	public String getTargetGId() {
		return targetGId;
	}

	public void setTargetGId(String targetGId) {
		this.targetGId = targetGId;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public int getOpeType() {
		return opeType;
	}

	public void setOpeType(int opeType) {
		this.opeType = opeType;
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
}
