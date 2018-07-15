package com.i5i58.data.social;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "FansInfos")
@JsonInclude(Include.NON_DEFAULT)
public class FansInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7465818488511865421L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	@Column(nullable = false, length = 32)
	private String fans = "";

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

}
