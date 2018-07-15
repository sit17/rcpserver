package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.List;

import com.i5i58.util.JsonUtils;

public class QueryTeamResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3481982858608883657L;

	public List<QueryTeamInfo> getTinfos() throws IOException {
		return new JsonUtils().toList("tinfos", QueryTeamInfo.class);
	}
}
