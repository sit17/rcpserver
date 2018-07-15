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
@Table(name = "ChannelAdminors", uniqueConstraints = {@UniqueConstraint(columnNames="id")})
@JsonInclude(Include.NON_DEFAULT)
public class ChannelAdminor implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163168306007647082L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long id;

	@Column(nullable = false)
	private String accId="";
	private String cId="";
	private int adminRight;

	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public int getAdminRight() {
		return adminRight;
	}
	public void setAdminRight(int adminRight) {
		this.adminRight = adminRight;
	}
	
}
