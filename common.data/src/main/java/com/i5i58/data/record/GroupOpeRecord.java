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
@Table(name = "GroupOpeRecords", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
@JsonInclude(Include.NON_DEFAULT)
public class GroupOpeRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false, length = 32)
	private String operatorId = "";
	
	@Column(nullable = false, length = 32)
	private String gId = "";
	
	@Column(nullable = false, length = 32)
	private String parentId = "";
	
	@Column
	private int opeType = 0;
	
//	@Column
//	private String detail = "";
	@Column
	private String oldName = "";

	@Column
	private String newName = "";
	
	@Column 
	private long datetime = 0L;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getOpeType() {
		return opeType;
	}

	public void setOpeType(int opeType) {
		this.opeType = opeType;
	}

//	public String getDetail() {
//		return detail;
//	}
//
//	public void setDetail(String detail) {
//		this.detail = detail;
//	}
	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}
}
