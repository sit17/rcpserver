package com.i5i58.apis.channel;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IGateServerSelect {
	/**
	 * 选取合适的网关
	 * */
	public ResultDataSet selectGateServer() throws IOException;
}
