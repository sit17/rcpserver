package com.i5i58.data.config;

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
@Table(name = "ReactNativeConfigs", uniqueConstraints = { @UniqueConstraint(columnNames = { "rnId" }) })
@JsonInclude(Include.NON_DEFAULT)
public class ReactNativeConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4755856505865902857L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private long rnId = 0L;

	@Column(nullable = false, length = 32)
	private String id = "";

	@Column(nullable = false)
	private String node = "";
	private String name = "";
	private String icon = "";
	private String module = "";
	private String rnZip = "";
	private String version = "";
	private String type = "";
	private int section = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRnZip() {
		return rnZip;
	}

	public void setRnZip(String rnZip) {
		this.rnZip = rnZip;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}
}
