package com.i5i58.apis.information;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 公会（组）信息接口
 * @author frank
 *
 */
public interface IGroupInfo {

	
	/**
	 * 模糊查询公会
	 * @param param
	 * @param sort
	 * @param dir
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet queryTopGroupList(String param, String sort, String dir, int pageSize, int pageNum)
			throws IOException;
}
