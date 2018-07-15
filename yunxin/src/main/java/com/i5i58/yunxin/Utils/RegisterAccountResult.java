package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.Map;

public class RegisterAccountResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -685273363873518408L;

	public RegisterAccountInfo getInfo() throws IOException {
		/*
		 * String json = this.getStringObject("info"); System.out.println(json);
		 * json = json.replaceAll("name=", "nickName=");
		 * System.out.println(json); return new JsonUtils().toObject(json,
		 * RegisterAccountInfo.class);
		 */
		Map<?, ?> info = this.getMap("info");
		RegisterAccountInfo rai = new RegisterAccountInfo();
		rai.setAccid(info.get("accid").toString());
		rai.setName(info.get("name").toString());
		rai.setToken(info.get("token").toString());
		return rai;
	}
}
