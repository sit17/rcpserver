package com.i5i58.data.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ReactNativeVersionConfigs", uniqueConstraints = { @UniqueConstraint(columnNames = { "version" }) })
@JsonInclude(Include.NON_DEFAULT)
public class ReactNativeVersionConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6434066890208882765L;

	@Id
	@Column(nullable = false, length = 32)
	String version = "";

	@Column(nullable = false, length = 255)
	String rnZipUrl = "";

	@Column(nullable = false, length = 1024)
	String rnDescribe = "";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRnZipUrl() {
		return rnZipUrl;
	}

	public void setRnZipUrl(String rnZipUrl) {
		this.rnZipUrl = rnZipUrl;
	}

	public String getRnDescribe() {
		return rnDescribe;
	}

	public void setRnDescribe(String rnDescribe) {
		this.rnDescribe = rnDescribe;
	}

}
