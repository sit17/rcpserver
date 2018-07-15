package com.i5i58.data.social;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "AttentionInfos")
@JsonInclude(Include.NON_DEFAULT)
public class AttentionInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3375162894504093389L;

	@Id
	@Column(length = 32)
	private String accId = "";

	private String Attention = "";

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAttention() {
		return Attention;
	}

	public void setAttention(String attention) {
		Attention = attention;
	}

}
