package com.i5i58.apis.channel;

import java.io.IOException;
import java.util.Map;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * @author songfl
 *
 */
public interface IZegoChannelPush {
	
	

	/**
	 * 创建直播
	 * @param term_type 客户端类型(如Android,iOS,Windows)
	 * @param net_type 网络类型(如有线,无线,4G,3G,2G)
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet createLive(String accId, String term_type, String net_type) throws IOException ;
	
	/**关闭直播
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet closeLive(String accId) throws IOException;
	
	/**禁用直播
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet forbidLive(String admin, String cId) throws IOException;
	
	/**恢复直播
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public ResultDataSet resumeLive(String admin, String cId) throws IOException;
	
	/**
	 * 流创建回调
	 * @return
	 */
	public String ceateCallback(Map<String, String[]> paramMap);
	
	/**
	 * 流关闭回调
	 * @return
	 */
	public String closeCallback(Map<String, String[]> paramMap);
	
	/**
	 * 回看地址生产回调
	 * @return
	 */
	public String replayCallback(Map<String, String[]> paramMap);
	
	/**
	 * OBS专用开播
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @param device
	 * @return
	 */
	ResultDataSet openPush(String accId, String cId, int device);
	
	/**
	 * OBS专用关播
	 * @author songfl
	 * @param accId
	 * @param cId
	 * @return
	 */
	ResultDataSet closePush(String accId, String cId) throws IOException;

}
