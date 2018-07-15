package com.i5i58.apis.account;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IBusinessAgent {

	ResultDataSet loginByOpenId(String openId, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException;

	ResultDataSet addAgent(String agentAdmin, String openId, String name, String phone, String qq, String clientIP)
			throws IOException;

	ResultDataSet deleteAgent(String agentAdmin, String openId, String clientIP) throws IOException;

	ResultDataSet nullityAgent(String agentAdmin, String openId, boolean nullity, String clientIP) throws IOException;

	ResultDataSet listAgent(String agent, int pageNum, int pageSize) throws IOException;
}
