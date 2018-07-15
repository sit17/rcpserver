package com.i5i58.apis.home;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 开播频道服务接口
 * @author frank
 *
 */
public interface IHotChannelOperate {
	/**
	 * 获取开播频道分页查询
	 * @author frank
	 * @param type
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getHotChannels(int type, int pageSize, int pageNum, String key)
			throws IOException;

}
