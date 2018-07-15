package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface INetBarAdmin {

	public ResultDataSet addNetBar(String superAccId, String accId, String netBarName, String addr, String netBarIp, String agId) throws IOException;

	public ResultDataSet deleteNetBar(String superAccId, String nId) throws IOException;

	public ResultDataSet queryNetBar(int pageNum, int pageSize, boolean viewAll) throws IOException;

	public ResultDataSet nullityNetBar(String superAccId, String nId, boolean nullity) throws IOException;

	
	public ResultDataSet addNetBarAgent(String superAccId, String accId, String agentName, String area) throws IOException;

	public ResultDataSet deleteNetBarAgent(String superAccId, String agId) throws IOException;

	public ResultDataSet queryNetBarAgent(int pageNum, int pageSize, boolean viewAll) throws IOException;

	public ResultDataSet nullityNetBarAgent(String superAccId, String agId, boolean nullity) throws IOException;

	public ResultDataSet queryNetBarByAgent(String superAccId, String agId, int pageNum, int pageSize, boolean viewAll) throws IOException;
}
