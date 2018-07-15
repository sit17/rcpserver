package com.i5i58.apis.platform;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformIIMessage {

	/**
	 * 发送系统内消息
	 * @author frank
	 * @param accIds
	 * @param title
	 * @param content
	 * @return
	 */
	public ResultDataSet queryAccountInfo(String accIds, String title, String content);
	
}
