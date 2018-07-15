package com.i5i58.service.im;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.im.IIMp2p;
import com.i5i58.util.JsonUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.GetUserResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class IMp2pService implements IIMp2p {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	JsonUtils jsonUtil;

	@Override
	public ResultDataSet addFriendRequest(String accId, String faccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String type = "2"; // 1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
			String msg = "";
			YXResultSet result = null;
			try {
				result = YunxinIM.addFriend(accId, faccid, type, msg);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:add friend null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM add friend failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet agreeFriendRequest(String accId, String faccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String type = "3"; // 1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
			String msg = "";
			YXResultSet result = null;
			try {
				result = YunxinIM.addFriend(accId, faccid, type, msg);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:agree friend null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM agree friend failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet disagreeFriendRequest(String accId, String faccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String type = "4"; // 1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
			String msg = "";
			YXResultSet result = null;
			try {
				result = YunxinIM.addFriend(accId, faccid, type, msg);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:disagree friend null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM disagree friend failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet pullBlackRequest(String accId, String targetAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String relationType = "1"; // 1: 黑名单操作, 2：静音操作
			String value = "1"; // 0:取消黑名单或静音，1:加入黑名单或静音
			YXResultSet result = null;
			try {
				result = YunxinIM.setSpecialRelation(accId, targetAccId, relationType, value);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:pull black null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM pull black failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet CancelPullBlackRequest(String accId, String targetAccId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			String relationType = "1"; // 1: 黑名单操作, 2：静音操作
			String value = "0"; // 0:取消黑名单或静音，1:加入黑名单或静音
			YXResultSet result = null;
			try {
				result = YunxinIM.setSpecialRelation(accId, targetAccId, relationType, value);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:cancel pull black null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM cancel pull black failed, msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet getAccount(String queryAccIds) {
		ResultDataSet rds = new ResultDataSet();
		try {
			GetUserResult result = null;
			try {
				result = YunxinIM.getUser(queryAccIds);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:get account null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:get account failed, msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			String data = jsonUtil.toJson(result.getUinfos());
			rds.setData(data);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet deleteFriendRequest(String accId, String faccid) {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet result = null;
			try {
				result = YunxinIM.deleteFirend(accId, faccid);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:delete friend null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:IM delete friend failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet updateFriendRequest(String accId, String faccid, String alias) {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet result = null;
			try {
				result = YunxinIM.updateFriend(accId, faccid, alias);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException : update friend null");
				return rds;
			}
			String code = result.getCode();
			if (!code.equals("200")) {
				String response = CodeToString.getString(code);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException : IM update friend failed msg : " + response);
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet getFriendRequest(String accId, String createTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			String result = null;
			try {
				result = YunxinIM.getFirend(accId, createTime);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException : update friend null");
				return rds;
			}

			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(result);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}

	@Override
	public ResultDataSet listBlackAndMuteListRequest(String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			String result = null;
			try {
				result = YunxinIM.listBlackAndMuteList(accId);
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			if (result == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException : update friend null");
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setData(result);
			return rds;
		} catch (Exception e) {
			throw new RuntimeException("事务失败");
		}
	}
}
