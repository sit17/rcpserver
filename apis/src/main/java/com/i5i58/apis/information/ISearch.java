package com.i5i58.apis.information;

import com.i5i58.apis.constants.ResultDataSet;

public interface ISearch {

	
	/**
	 * 查询账号（openId，nickName）
	 * @author frank
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryAccount(String param, int pageNum, int pageSize);
	
	/**
	 * 查询群（openId，nickName）
	 * @author frank
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryTeam(String param, int pageNum, int pageSize);
	
	/**
	 * 查询频道（openId，nickName）
	 * @author frank
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryChannel(String param, int pageNum, int pageSize);

	/**
	 * 查询主播
	 * @author frank
	 * @param param
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryAnchor(String param, int pageNum, int pageSize);
	
	/**
	 * 查询新秀主播
	 * @author frank
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryNewAnchor(int pageNum, int pageSize);
	
	/**
	 * 查询大家都在看
	 * @author frank
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryLotWatchAnchor(int pageNum, int pageSize);

	/**
	 * 查询新秀/大家都在看
	 * @author frank
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ResultDataSet queryNewLotWatchAnchor(int pageNum, int pageSize);
}
