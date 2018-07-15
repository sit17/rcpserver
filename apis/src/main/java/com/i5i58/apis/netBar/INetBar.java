package com.i5i58.apis.netBar;

import com.i5i58.apis.constants.ResultDataSet;

public interface INetBar {

	ResultDataSet loginByOpenId(String openId, int version, String clientIP, int device, String serialNum);

	ResultDataSet loginByOpenIdForAgent(String openId, int version, String clientIP, int device, String serialNum);

	ResultDataSet loginByOpenIdForWeb(String openId, String password, int version, String clientIP, int device,
			String serialNum);

	ResultDataSet loginByOpenIdForAgentForWeb(String openId, String password, int version, String clientIP, int device,
			String serialNum);

	ResultDataSet queryGiftRecord(String accId, long startDate, long endDate, int pageNum, int pageSize);

	ResultDataSet queryGiftIncome(String accId, long startDate, long endDate);

	ResultDataSet queryGameIncome(String accId, long startDate, long endDate);

	ResultDataSet queryNetBarByAgent(String accId, int pageNum, int pageSize, boolean viewAll);

	ResultDataSet queryAccountKind(String openId);

	ResultDataSet queryPayRebate(String accId, String searchMonth);

	ResultDataSet queryExChangeScoreRebate(String accId, String searchMonth);

	ResultDataSet queryGiveGiftRebate(String accId, String searchMonth);

}
