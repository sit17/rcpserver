package com.i5i58.apis.vendor;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 运营商统计数据
 * */
public interface IVendor {
	
	/**
	 *	记录用户操作
	 * @author songfl
	 * @param vendorId
	 * @param accId
	 * @param adId
	 * @param actionId
	 * @param clientIp
	 * @param macAddress
	 * @return
	 * */
	ResultDataSet userAction(String vendorId, String accId, String adId, int actionId, String clientIp, String macAddress);
	
	/**
	 *	查找用户记录
	 * @author songfl
	 * @param param
	 * @param type vendorId adId actionId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * */
	ResultDataSet searchUserActions(String param, String type,  int pageNum, int pageSize);
	
	/**
	 *	查找用户记录
	 * @author songfl
	 * @param from
	 * @param to 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * */
	ResultDataSet searchUserActionsByTime(long from, long to, int pageNum, int pageSize);
	
	
	/**
	 * 统计用户操作数量
	 * @author songfl
	 * @param param 
	 * @param type vendorId adId actionId
	 * @return
	 * */
	ResultDataSet countUserActions(String param, String type);
	
	/**
	 * 统计用户操作数量
	 * @author songfl
	 * @param from
	 * @param to
	 * @return
	 * */
	ResultDataSet countUserActionsByTime(long from, long to);
}
