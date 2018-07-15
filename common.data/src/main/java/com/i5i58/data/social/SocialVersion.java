package com.i5i58.data.social;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SocialVersions", uniqueConstraints = { @UniqueConstraint(columnNames = "accId") })
@JsonInclude(Include.NON_DEFAULT)
public class SocialVersion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3507509882371625933L;

	@Id
	@Column(nullable = false, length = 32)
	private String accId = "";

	private String attentionVersion = "";

	private String fansVersion = "";

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getAttentionVersion() {
		return attentionVersion;
	}

	public void setAttentionVersion(String attentionVersion) {
		this.attentionVersion = attentionVersion;
	}

	public String getFansVersion() {
		return fansVersion;
	}

	public void setFansVersion(String fansVersion) {
		this.fansVersion = fansVersion;
	}

}
