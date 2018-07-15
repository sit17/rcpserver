package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.Map;

public class RefreshTokenResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6331984241624596369L;

	public RefreshTokenInfo getInfo() throws IOException {
		Map<?, ?> info = this.getMap("info");
		RefreshTokenInfo rti = new RefreshTokenInfo();
		rti.setAccid(info.get("accid").toString());
		rti.setToken(info.get("token").toString());
		return rti;
	}
}