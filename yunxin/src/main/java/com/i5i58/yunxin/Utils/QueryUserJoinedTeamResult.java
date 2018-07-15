package com.i5i58.yunxin.Utils;

import java.io.IOException;
import java.util.List;

import com.i5i58.util.JsonUtils;

public class QueryUserJoinedTeamResult extends YXResultSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int count;
	public List<JoinedTeamInfo> getTinfos() throws IOException {
		return new JsonUtils().toList("infos", JoinedTeamInfo.class);
	}
}
