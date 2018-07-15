package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.List;

import com.i5i58.util.JsonUtils;

public class GetUserResult extends YXResultSet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8626944285845225979L;

	public List<GetUserInfo> getUinfos() throws IOException {
		return new JsonUtils().toList("uinfos", GetUserInfo.class);
	}
}
