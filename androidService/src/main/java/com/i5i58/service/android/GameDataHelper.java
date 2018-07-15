package com.i5i58.service.android;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.i5i58.config.SqlserverConfig;

public class GameDataHelper {

	SqlserverConfig sqlserverConfig;

	public GameDataHelper(SqlserverConfig sqlserverConfig) {
		this.sqlserverConfig = sqlserverConfig;
	}

	// 测试连接
	public boolean TestConn() {
		if (sqlserverConfig.GetAccountConn() != null)
			return false;

		// CloseConn();
		return true;
	}

	private Logger logger = Logger.getLogger(getClass());

	public int addAndroidAccount(String accId,String openId,String account, String gameName, byte gender, String location,
			String clientIP, String MachineID, String logonPass) {
		// Object[]
		// objParams={accId,ExchangeScore,ExchangeBeans,ExchangeType,clientIP,MachineID};
		//logger.info("accId:" + accId + "ExchangeScore:" + ExchangeScore + "ExchangeBeans:" + ExchangeBeans);
		int returnValue = 0;
		try {
			String sql = "{call GSP_GP_RegisterAccountsForAndroid(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, openId);
			cstmt.setString(2, account);
			cstmt.setString(3, gameName);
			cstmt.setString(4, "");
			cstmt.setString(5, logonPass);
			cstmt.setByte(6, gender);
			cstmt.setString(7, "");
			cstmt.setString(8, gameName);
			cstmt.setString(9, clientIP);
			cstmt.setString(10, MachineID);
			cstmt.setString(11, accId);
			cstmt.registerOutParameter(12, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(13, java.sql.Types.INTEGER);
			cstmt.execute();
			// System.out.println(cstmt.getString(9));
			returnValue = cstmt.getInt(13);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			return -1;
		}
		return returnValue;
	}	
}