package com.i5i58.service.game;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.i5i58.apis.constants.ResponseData;
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

	public SQLResultData exchangeGameGold(String accId, long ExchangeScore, long ExchangeBeans, Byte ExchangeType,
			String clientIP, String MachineID, String Remark, String insurePass) {
		// Object[]
		// objParams={accId,ExchangeScore,ExchangeBeans,ExchangeType,clientIP,MachineID};
		logger.info("accId:" + accId + "ExchangeScore:" + ExchangeScore + "ExchangeBeans:" + ExchangeBeans);
		SQLResultData sd = new SQLResultData();
		int returnValue = 0;
		try {
			String sql = "{call GSP_GR_ExchangeScore(?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setDouble(2, ExchangeScore);
			cstmt.setDouble(3, ExchangeBeans);
			cstmt.setByte(4, ExchangeType);
			cstmt.setString(5, insurePass);
			cstmt.setString(6, clientIP);
			cstmt.setString(7, MachineID);
			cstmt.setString(8, Remark);
			cstmt.registerOutParameter(9, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
			cstmt.execute();
			returnValue = cstmt.getInt(10);
			String describe = cstmt.getString(9);
			sd.setReutrnCode(returnValue);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public String getBankPassword(String accId) throws Exception {
		String bankPass = null;
		String sql = "select InsurePass from THAccountsDB.dbo.AccountsInfo where UserUin='" + accId + "'";
		Statement stmt = sqlserverConfig.GetAccountConn().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs != null && rs.next()) {
			bankPass = rs.getString("InsurePass");
			bankPass = bankPass.trim();
		}
		return bankPass;
	}

	public String getGameScoreByIp(String starTime, String endTime, String ip) throws Exception {
		String ScoreSum = "0";
		String sql = "SELECT sum([Score]) as ScoreSum  FROM [THTreasureDB].[dbo].[RecordDrawScore] where DrawID in(SELECT [DrawID] "
				+ "FROM [THTreasureDB].[dbo].[RecordDrawInfo] where KindID<>401" + " and InsertTime>='" + starTime
				+ "' and InsertTime<='" + endTime + "') and ClientIP='" + ip + "'";
		Statement stmt = sqlserverConfig.GetAccountConn().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs != null && rs.next()) {
			if (rs.getObject("ScoreSum") != null) {
				ScoreSum = rs.getString("ScoreSum");
				ScoreSum = ScoreSum.trim();
			}
		}
		return ScoreSum;
	}

	public ResultSet getBank(String accId) throws Exception {
		String sql = "SELECT a.[UserID],a.[Score],a.[Revenue],a.[InsureScore],b.LoveLiness,b.Present "
				+ " FROM [THTreasureDB].[dbo].[GameScoreInfo] as a join THAccountsDB.dbo.AccountsInfo as b on a.UserID=b.UserID  where a.UserID = "
				+ "(select THAccountsDB.dbo.AccountsInfo.UserID from THAccountsDB.dbo.AccountsInfo "
				+ "where THAccountsDB.dbo.AccountsInfo.UserUin='" + accId + "')";
		Statement stmt = sqlserverConfig.GetTreasureConn().createStatement();
		return stmt.executeQuery(sql);
	}

	public SQLResultData UserEnableInsure(String accId, String strPassword, String strInsurePass, String clientIP,
			String MachineID, Integer GameID, Short Gender, String MobilePhone) {
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_UserEnableInsure(?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, strPassword);
			cstmt.setString(3, strInsurePass);
			cstmt.setString(4, clientIP);
			cstmt.setString(5, MachineID);
			cstmt.setInt(6, GameID);
			cstmt.setShort(7, Gender);
			cstmt.setString(8, MobilePhone);
			cstmt.registerOutParameter(9, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(9);
			int code = cstmt.getInt(10);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public SQLResultData SaveScore(String accId, long ExchangeScore, String clientIP, String MachineID) {
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_UserSaveScore(?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setLong(2, ExchangeScore);
			cstmt.setInt(3, 0);
			cstmt.setInt(4, 0);
			cstmt.setString(5, clientIP);
			cstmt.setString(6, MachineID);
			cstmt.registerOutParameter(7, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(8, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(7);
			int code = cstmt.getInt(8);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public SQLResultData TakeScore(String accId, String strPassword, long ExchangeScore, String clientIP,
			String MachineID) {
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_UserTakeScore(?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setLong(2, ExchangeScore);
			cstmt.setString(3, strPassword);
			cstmt.setInt(4, 0);
			cstmt.setInt(5, 0);
			cstmt.setString(6, clientIP);
			cstmt.setString(7, MachineID);
			cstmt.registerOutParameter(8, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(9, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(8);
			int code = cstmt.getInt(9);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
			// System.out.println(cstmt.getString(8));
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public SQLResultData ModifyInsurePassword(String accId, String strPassword, String strNewPassword,
			String clientIp) {
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_ModifyInsurePassword(?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, strPassword);
			cstmt.setString(3, strNewPassword);
			cstmt.setString(4, clientIp);
			cstmt.registerOutParameter(5, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
			cstmt.execute();

			String describe = cstmt.getString(5);
			int code = cstmt.getInt(6);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public SQLResultData ResetInsurePassword(String accId, String strPassword, String clientIp) {
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_ResetInsurePassword(?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, strPassword);
			cstmt.setString(3, clientIp);
			cstmt.registerOutParameter(4, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
			cstmt.execute();

			String describe = cstmt.getString(4);
			int code = cstmt.getInt(5);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public ResultSet getBankRecord(String accId, String startTime, String endTime, int pageSize, int pageIndex) {
		ResultSet rs = null;
		try {
			String sql = "{call GSP_GR_GetBankRecord(?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, startTime);
			cstmt.setString(3, endTime);
			cstmt.setInt(4, pageSize);
			cstmt.setInt(5, pageIndex);
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(7, java.sql.Types.INTEGER);
			rs = cstmt.executeQuery();
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
		}
		return rs;
	}

	public ResultSet getBankRecordByTradeType(String accId, String startTime, String endTime, int pageSize,
			int pageIndex) {
		ResultSet rs = null;
		try {
			String sql = "{call GSP_GR_GetBankRecordByTradeType(?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, startTime);
			cstmt.setString(3, endTime);
			cstmt.setInt(4, pageSize);
			cstmt.setInt(5, pageIndex);
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(7, java.sql.Types.INTEGER);
			rs = cstmt.executeQuery();
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
		}
		return rs;
	}

	public SQLResultData exchangeLoveliness(String accId, long lovelinessExchanged, String clientIp, long convertRate) {
		SQLResultData sqlRs = new SQLResultData();
		try {
			String sql = "{call NET_PW_ConvertPresent(?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setLong(2, lovelinessExchanged);
			cstmt.setLong(3, convertRate);
			cstmt.setString(4, clientIp);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(5);
			int code = cstmt.getInt(6);
			sqlRs.setReutrnCode(code);
			sqlRs.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sqlRs.setReutrnCode(-1);
			sqlRs.setDescribe("数据库异常");
		}
		return sqlRs;
	}

	public SQLResultData modifyUserNickName(String accId, String nickName, String clientIp) {
		SQLResultData sd = new SQLResultData();
		try {
			nickName = new String(nickName.getBytes("gb2312"), "gb2312");

			String sql = "{call GSP_GP_ModifyUserNickName(?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, nickName);
			cstmt.setString(3, clientIp);
			cstmt.registerOutParameter(4, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
			cstmt.execute();

			String describe = cstmt.getString(4);
			int code = cstmt.getInt(5);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public String getGameNick(String accId) {
		String sql_select = "select a.NickName from THAccountsDB.dbo.AccountsInfo as a where a.UserUin='" + accId + "'";
		Statement stmt;
		try {
			stmt = sqlserverConfig.GetAccountConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql_select);
			if (rs.next()) {
				String nickName = rs.getString("NickName");
				return nickName;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return "";
	}

	public Map<String, Object> getGameInfoByGameId(String GameId) {
		String sql_select = "select a.NickName,a.UserUin from THAccountsDB.dbo.AccountsInfo as a where a.GameID='"
				+ GameId + "'";
		Statement stmt;
		try {
			stmt = sqlserverConfig.GetAccountConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql_select);
			Map<String, Object> res = new HashMap<String, Object>();
			if (rs.next()) {
				res.put("nickName", rs.getString("NickName"));
				res.put("accId", rs.getString("UserUin"));
			}
			return res;
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}

	public int getMyRank(String accId) {
		String sql_select = "select score_row.rowId from ";
		String sql_orderrow = "(select ROW_NUMBER() over(order by Score desc) rowId,* from [THTreasureDB].[dbo].[GameScoreInfo]) score_row ";
		String sql_where = " where score_row.UserID = ("
				+ "select a.UserID from THAccountsDB.dbo.AccountsInfo as a where a.UserUin='" + accId + "')";
		String sql = sql_select + sql_orderrow + sql_where;
		System.out.println(sql);
		Statement stmt;
		try {
			stmt = sqlserverConfig.GetAccountConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				int rowId = rs.getInt("rowId");
				return rowId;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return 0;
	}

	public ResponseData getRankList(String accId, int rankFirst, int rankLast) {
		ResponseData rp = new ResponseData();
		String sql_select = "select score_row.rowId, score_row.UserID, score_row.Score, b.NickName, b.GameID from ";
		String sql_orderrow = "(select ROW_NUMBER() over(order by Score desc) rowId,* from [THTreasureDB].[dbo].[GameScoreInfo]) score_row ";
		String sql_join = "left join THAccountsDB.dbo.AccountsInfo as b on score_row.UserID=b.UserID ";
		String sql_where = " where (score_row.rowId between " + rankFirst + " and " + rankLast + ")";
		String sql = sql_select + sql_orderrow + sql_join + sql_where;
		System.out.println(sql);
		Statement stmt;
		try {
			stmt = sqlserverConfig.GetAccountConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				++i;
				ResponseData record = new ResponseData();
				int userId = rs.getInt("UserID");
				// int gameID = rs.getInt("GameID");
				long score = rs.getLong("Score");
				String nickName = rs.getString("NickName");
				if (nickName == null) {
					nickName = "";
				}
				nickName = new String(nickName.getBytes("utf-8"), "utf-8");
				record.put("userId", userId);
				// record.put("gameID", gameID);
				record.put("score", score);
				record.put("nickName", nickName);
				rp.put("" + i, record);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		int myRank = getMyRank(accId);
		rp.put("myRank", myRank);
		return rp;
	}

	public SQLResultData commissionExchangeGameGold(String accId, long commission, long gameGold, String clientIp) {
		SQLResultData resultData = new SQLResultData();
		try {
			String sql = "{call GSP_GP_CommissionExchangeGameGold(?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetAccountConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setLong(2, commission);
			cstmt.setLong(3, gameGold);
			cstmt.setString(4, clientIp);
			cstmt.registerOutParameter(5, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
			cstmt.execute();

			String describe = cstmt.getString(5);
			int code = cstmt.getInt(6);
			resultData.setReutrnCode(code);
			resultData.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			resultData.setReutrnCode(-1);
			resultData.setDescribe("数据库异常");
		}

		return resultData;
	}

	public SQLResultData TransferScore(String accId, long transferScore, String insurePass, String targetAccId,
			String transRemark, String clientIP, String machineID) {
		// TODO Auto-generated method stub
		SQLResultData sd = new SQLResultData();
		try {
			String sql = "{call GSP_PTF_UserTransferScore(?,?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setLong(2, transferScore);
			cstmt.setString(3, insurePass);
			cstmt.setString(4, targetAccId);
			cstmt.setString(5, transRemark);
			cstmt.setInt(6, 0);
			cstmt.setInt(7, 0);
			cstmt.setString(8, clientIP);
			cstmt.setString(9, machineID);
			cstmt.registerOutParameter(10, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(11, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(10);
			int code = cstmt.getInt(11);
			sd.setReutrnCode(code);
			sd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			sd.setReutrnCode(-1);
			sd.setDescribe("数据库异常");
		}
		return sd;
	}

	public SQLResultData purchaseMemberVip(String accId, String tarAccId, int level, int month, String clientIp,
			String machineId) {
		SQLResultData srd = new SQLResultData();
		try {
			String sql = "{call GSP_GR_PurchaseMemberByIGold(?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = sqlserverConfig.GetTreasureConn().prepareCall(sql);
			cstmt.setString(1, accId);
			cstmt.setString(2, tarAccId);
			cstmt.setInt(3, level);
			cstmt.setInt(4, month);
			cstmt.setString(5, clientIp);
			cstmt.setString(6, machineId);
			cstmt.registerOutParameter(7, java.sql.Types.NVARCHAR);
			cstmt.registerOutParameter(8, java.sql.Types.INTEGER);
			cstmt.execute();
			String describe = cstmt.getString(7);
			int code = cstmt.getInt(8);
			srd.setReutrnCode(code);
			srd.setDescribe(describe);
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
			logger.error(Ex.toString());
			srd.setReutrnCode(-1);
			srd.setDescribe("数据库异常");
		}
		return srd;
	}

	public GameVipInfo getGameVip(String accId) {
		GameVipInfo info = new GameVipInfo();
		String sql_select = "select a.MemberOrder, a.MemberOverDate, a.MemberSwitchDate,a.UserRight from THAccountsDB.dbo.AccountsInfo as a where a.UserUin='"
				+ accId + "'";
		Statement stmt;
		try {
			stmt = sqlserverConfig.GetAccountConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql_select);
			if (rs.next()) {
				int memberOrder = rs.getInt("MemberOrder");
				java.sql.Date memberOverDate = rs.getDate("MemberOverDate");
				java.sql.Date memberSwitchDate = rs.getDate("MemberSwitchDate");

				int userRight = rs.getInt("UserRight");
				info.setMemberOrder(memberOrder);
				info.setUserRight(userRight);
				info.setMemberOverDate(memberOverDate.getTime());
				info.setMemberSwitchDate(memberSwitchDate.getTime());
			}
			return info;
		} catch (Exception e) {
			logger.error(e.toString());
			return info;
		}
	}
	public ResultSet getGamePlayTimes(String starTime, String endTime, String ip) throws Exception {
		String sql = "SELECT THAccountsDB.dbo.AccountsInfo.GameID,THAccountsDB.dbo.AccountsInfo.NickName"
				+ ",sum([THTreasureDB].[dbo].[RecordDrawScore].[PlayTimeCount]) as playcount"
				+ " FROM THAccountsDB.dbo.AccountsInfo inner join [THTreasureDB].[dbo].[RecordDrawScore] "
				+ " on THAccountsDB.dbo.AccountsInfo.UserID =[THTreasureDB].dbo.RecordDrawScore.UserID "
				+ " where THAccountsDB.dbo.AccountsInfo.RegisterDate >= '" + starTime + "'"
				+ " and THAccountsDB.dbo.AccountsInfo.RegisterDate <='" + endTime + "'"
				+ " and THAccountsDB.dbo.AccountsInfo.RegisterIP = '" + ip + "'"
				+ " and [THTreasureDB].[dbo].[RecordDrawScore].[InsertTime] >= '" + starTime + "'"
				+ " and [THTreasureDB].[dbo].[RecordDrawScore].[InsertTime] <= '" + endTime + "'"
				+ " and [THTreasureDB].[dbo].[RecordDrawScore].[Score]<>0"
				+ " and [THTreasureDB].[dbo].[RecordDrawScore].[ClientIP] = '" + ip + "'"
				+ " group by THAccountsDB.dbo.AccountsInfo.NickName,THAccountsDB.dbo.AccountsInfo.GameID";
		Statement stmt = sqlserverConfig.GetTreasureConn().createStatement();
		return stmt.executeQuery(sql);
	}

}