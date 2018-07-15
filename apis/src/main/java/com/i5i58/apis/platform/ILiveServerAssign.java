package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface ILiveServerAssign {
	
	/**
	 * 为直播间分配服务器
	 * @throws IOException 
	 * */
	public ResultDataSet assignLiveServer(String accId, String cId) throws IOException;
	
	/**
	 * 获取所有空闲的服务器
	 * @throws IOException 
	 * */
	public ResultDataSet getAllFreeLiveServer() throws IOException;
	
	
	/**
	 * 获取所有激活的服务器
	 * @throws IOException 
	 * */
	public ResultDataSet getAllActiveLiveServer() throws IOException;
}
