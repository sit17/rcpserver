package com.i5i58.apis.social;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 短排服务接口
 * @author frank
 *
 */
public interface IShortFilms {

	/**
	 * 获取短拍分页查询
	 * 
	 * @author frank
	 * @param pageSize
	 * @param pageNum
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	ResultDataSet getShortFilms(Integer pageSize, Integer pageNum,String sortBy,String sortDirection, String accId);
	
	/**
	 * 上传短拍
	 * @param accId
	 * @param time
	 * @param url
	 * @return
	 */
	ResultDataSet upLoadShortFilms(String accId,String url);

}
