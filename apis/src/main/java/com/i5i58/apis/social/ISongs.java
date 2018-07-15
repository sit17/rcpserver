package com.i5i58.apis.social;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 神曲服务接口
 * 
 * @author frank
 *
 */
public interface ISongs {

	/**
	 * 获取神曲分页查询
	 * 
	 * @author frank
	 * @param type
	 * @param pageSize
	 * @param pageNum
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	ResultDataSet getSongs(String accId,String type, Integer pageSize, Integer pageNum, String sortBy, String sortDirection);

	/**
	 * 上传神曲
	 * 
	 * @param accId
	 * @param url
	 * @return
	 */
	ResultDataSet upLoadSongs(String accId, String url);

}
