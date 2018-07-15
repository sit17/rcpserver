package com.i5i58.yunxin.Utils;

import java.io.Serializable;

public class GetUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5957462125533509877L;

	String email;
	String accid;
	String name;
	String gender;
	String mobile;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
