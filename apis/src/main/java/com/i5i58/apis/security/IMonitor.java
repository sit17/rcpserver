package com.i5i58.apis.security;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * monitor
 * @author frank
 *
 */
public interface IMonitor {

	/**
	 * 上报进程
	 * @param accId
	 * @param currProcInfo
	 * @param procInfo
	 * @return
	 */
	ResultDataSet reportProc(String accId, String key);
	
	/**
	 * 上报图片
	 * @param accId
	 * @param key
	 * @return
	 */
	ResultDataSet reportPic(String accId, String key);
}
