package com.i5i58.apis.social;

import com.i5i58.apis.constants.ResultDataSet;

public interface IQueryInfoWithoutAuth {
	/**
	 * 获取TA的信息
	 * @author songfl
	 * @param accId
	 * @return
	 */
	ResultDataSet getTAInfo(String accId);
	
	/**
	 * TA的关注
	 * @author songfl
	 * @param ta
	 * @param pageNum
	 * @return
	 */
	ResultDataSet getFollowByAccId(String ta, Integer pageNum);
	
	/**
	 * 获取有马甲的频道
	 * 
	 * @author songfl
	 * @param accId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	ResultDataSet getChannelsByMajia(String accId, int pageSize, int pageNum);
	
	/**
	 * 获取用户频道
	 * 
	 * @author songfl
	 * @param accId
	 * @return
	 * */
	ResultDataSet getChannelByOwner(String owner);
	
	/**
	 * 获取频道信息
	 * 
	 * @author cw
	 * @param cId
	 * @return
	 */
	ResultDataSet getChannelInfo(String cId);

	
	/**
	 * 查詢公会或分组的展示信息
	 * */
	ResultDataSet getGroupInfo(String gId);
}
