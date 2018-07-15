package com.i5i58.apis.netBar;

import com.i5i58.apis.constants.ResultDataSet;

public interface INetBarAssistant {

	ResultDataSet adminLoginByPhone(String phoneNo, String password, int device, String clientIP, String serialNum);

	ResultDataSet adminLoginByThird(int third, String unionId, int device, String clientIP, String serialNum);

	ResultDataSet addAdmin(String accId, String adminId, String name, String clientIP);

	ResultDataSet deleteAdmin(String accId, String adminId, String clientIP);

	ResultDataSet editAdmin(String accId, String adminId, String name, boolean nullity, String clientIP);

	ResultDataSet exchangePresent(String accId, String clientIP, String[] lanIPs, String serialNum, int presentId,
			int num);

	ResultDataSet confirmExchangePresent(String accId, String clientIP, String orderId);

	ResultDataSet bindPc(String accId, String clientIP, String lanIP, String name);

	ResultDataSet editBindPc(String accId, String clientIP, String lanIP, String name);

	ResultDataSet unBindPc(String accId, String clientIP, String lanIP);

	ResultDataSet queryBindPc(String accId, String clientIP, int pageNum, int pageSize);
}
