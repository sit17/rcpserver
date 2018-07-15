package com.i5i58.service.account;

import java.sql.CallableStatement;

import org.apache.log4j.Logger;

import com.i5i58.apis.constants.SQLResultData;
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
	
	public SQLResultData getGameGold(String TargetAccId,long lTransferScore,String strTransRemark,String strClientIP,String strMachineID){
		SQLResultData srd=new SQLResultData();
		String sql="{call GSP_PTF_PrizeScore(?,?,?,?,?,?,?)}";
		int returnValue=0;
		try {
			CallableStatement cs=sqlserverConfig.GetTreasureConn().prepareCall(sql);
			
			cs.setLong(1,lTransferScore);
			cs.setString(2,TargetAccId );
			cs.setString(3,strTransRemark);
			cs.setString(4,strClientIP);
			cs.setString(5,strMachineID);
			cs.registerOutParameter(6,java.sql.Types.NVARCHAR);
			cs.registerOutParameter(7,java.sql.Types.INTEGER );
			cs.execute();
			returnValue=cs.getInt(7);
			String describe=cs.getString(6);
			srd.setReutrnCode(returnValue);
			srd.setDescribe(describe);
		} catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			srd.setReutrnCode(-1);
			srd.setDescribe("数据库异常");
		}
		return srd;
	}
}