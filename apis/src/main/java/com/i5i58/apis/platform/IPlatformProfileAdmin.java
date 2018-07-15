package com.i5i58.apis.platform;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

public interface IPlatformProfileAdmin {

	ResultDataSet queryProfileList(String param, int status, int pageSize, int pageNum) throws IOException;

	/**
	 * 查询采用该资质的公会（即显示该经纪公司下属的全部公会）
	 * 
	 * @author frank
	 * @param param
	 * @param status
	 * @return
	 * @throws IOException
	 */
	ResultDataSet queryTopGroupsInProfile(String fId) throws IOException;

	/**
	 * 显示实名认证时该公司提交的全部认证材料（可参考web需求的实名认证部分）
	 * 
	 * @author frank
	 * @param fId
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getProfile(String fId) throws IOException;

	/**
	 * 开启/封禁档案
	 * 
	 * @author frank
	 * @param superAccid
	 * @param fId
	 * @param enable
	 * @return
	 * @throws IOException
	 */
	ResultDataSet enableProfile(String superAccid, String fId, boolean enable) throws IOException;

	/**
	 * 工作人员审核经纪公司档案
	 * 
	 * @author frank
	 * @param superAccid
	 * @param pId
	 * @param agree
	 * @return
	 * @throws IOException
	 */
	ResultDataSet verifyGroupProfile(String superAccid, String fId, int topGroupCount, int subGroupCount,
			int channelCount, boolean agree) throws IOException;

	/**
	 * 工作人员审核经纪公司档案
	 * 
	 * @author frank
	 * @param superAccid
	 * @param pId
	 * @param agree
	 * @return
	 * @throws IOException
	 */
	ResultDataSet updateGroupAndChannelCount(String superAccid, String fId, int topGroupCount, int subGroupCount,
			int channelCount) throws IOException;
}
