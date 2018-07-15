package com.i5i58.service.netBar;

import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.netBar.INetBarAssistant;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.ThirdAccount;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.netBar.NetBarAdmin;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.ThirdAccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAdminPriDao;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.SecurityUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;

public class NetBarAssistantService implements INetBarAssistant {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	NetBarAdminPriDao netBarAdminPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	ThirdAccountPriDao thirdAccountPriDao;

	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;

	@Override
	public ResultDataSet adminLoginByPhone(String phoneNo, String password, int device, String clientIP,
			String serialNum) {
		ResultDataSet rds = new ResultDataSet();

		Account acc = accountPriDao.findByPhoneNoAndPassword(phoneNo, password);

		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}

		NetBarAdmin netBarAdmin = netBarAdminPriDao.findByAccIdAndNetIp(acc.getAccId(), clientIP);
		if (netBarAdmin == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧管理员");
			return rds;
		}
		if (netBarAdmin.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("网吧管理员被禁用！");
			return rds;
		}
		// ================JWT================

		ResponseData response = new ResponseData();
		try {
			acc.setLastLoginIp(clientIP);
			String yxToken = acc.getYxToken();
			if (StringUtils.StringIsEmptyOrNull(yxToken)) {
				RefreshTokenResult refreshTokenResult;
				refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
				if (refreshTokenResult == null) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:null");
					return rds;
				}
				if (!refreshTokenResult.getCode().equals("200")) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
							+ refreshTokenResult.getString("desc"));
					return rds;
				}
				logger.info(String.format("db.accId:%s, yxinfo.accId:%s, yxinfo.token:%s", acc.getAccId(),
						refreshTokenResult.getInfo().getAccid(), refreshTokenResult.getInfo().getToken()));
				if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:accid dif");
					return rds;
				}
				yxToken = refreshTokenResult.getInfo().getToken();
				accountUtils.setToken(acc.getAccId(), yxToken);
			}
			acc.setYxToken(yxToken);
			accountPriDao.save(acc);
			String token = SecurityUtils.createJsonWebToken(acc.getAccId(), device, serialNum);
			accountUtils.setJWTToken(acc.getAccId(), device, token);
			acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
			acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
			response.put("acc", acc);
			response.put("yxToken", yxToken);
			if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
				response.put("super", true);
			}
			if (device == DeviceCode.WEBLive) {
				rds.setInnerData(token);
			} else {
				response.put("token", token);
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(e.getMessage());
			return rds;
		}
		rds.setData(response);
		// ================JWT================

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet adminLoginByThird(int third, String unionId, int device, String clientIP, String serialNum) {
		ResultDataSet rds = new ResultDataSet();

		ThirdAccount thirdAcc = thirdAccountPriDao.findByThirdTypeAndThirdId(third, unionId);
		if (thirdAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请先注册胖虎娱乐账号!");
			return rds;
		}
		Account acc = accountPriDao.findOne(thirdAcc.getAccId());

		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}

		NetBarAdmin netBarAdmin = netBarAdminPriDao.findByAccIdAndNetIp(acc.getAccId(), clientIP);
		if (netBarAdmin == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧管理员");
			return rds;
		}
		if (netBarAdmin.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("网吧管理员被禁用！");
			return rds;
		}
		// ================JWT================

		ResponseData response = new ResponseData();
		try {
			acc.setLastLoginIp(clientIP);
			String yxToken = acc.getYxToken();
			if (StringUtils.StringIsEmptyOrNull(yxToken)) {
				RefreshTokenResult refreshTokenResult;
				refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
				if (refreshTokenResult == null) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:null");
					return rds;
				}
				if (!refreshTokenResult.getCode().equals("200")) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
							+ refreshTokenResult.getString("desc"));
					return rds;
				}
				logger.info(String.format("db.accId:%s, yxinfo.accId:%s, yxinfo.token:%s", acc.getAccId(),
						refreshTokenResult.getInfo().getAccid(), refreshTokenResult.getInfo().getToken()));
				if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yx_exception:accid dif");
					return rds;
				}
				yxToken = refreshTokenResult.getInfo().getToken();
				accountUtils.setToken(acc.getAccId(), yxToken);
			}
			acc.setYxToken(yxToken);
			accountPriDao.save(acc);
			String token = SecurityUtils.createJsonWebToken(acc.getAccId(), device, serialNum);
			accountUtils.setJWTToken(acc.getAccId(), device, token);
			acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
			acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
			response.put("acc", acc);
			response.put("yxToken", yxToken);
			if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
				response.put("super", true);
			}
			if (device == DeviceCode.WEBLive) {
				rds.setInnerData(token);
			} else {
				response.put("token", token);
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(e.getMessage());
			return rds;
		}
		rds.setData(response);
		// ================JWT================

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addAdmin(String accId, String adminId, String name, String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		NetBarAccount nba = netBarAccountPriDao.findByNetIpAndOwnerIdAndNullity(clientIP, accId, false);
		if (nba == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧超级管理员或被禁用!");
			return rds;
		}
		Account adAcc = accountPriDao.findOne(adminId);
		if (null == adAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户不存在");
			return rds;
		}
		if (adAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户被禁用");
			return rds;
		}
		NetBarAdmin nbAdmin = new NetBarAdmin();
		nbAdmin.setAccId(adminId);
		nbAdmin.setCreateTime(DateUtils.getNowTime());
		nbAdmin.setName(name);
		nbAdmin.setNetIp(clientIP);
		netBarAdminPriDao.save(nbAdmin);

		rds.setData(nbAdmin);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAdmin(String accId, String adminId, String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		NetBarAccount nba = netBarAccountPriDao.findByNetIpAndOwnerIdAndNullity(clientIP, accId, false);
		if (nba == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧超级管理员或被禁用!");
			return rds;
		}
		Account adAcc = accountPriDao.findOne(adminId);
		if (null == adAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户不存在");
			return rds;
		}
		if (adAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户被禁用");
			return rds;
		}
		netBarAdminPriDao.delete(adminId);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet editAdmin(String accId, String adminId, String name, boolean nullity, String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		NetBarAccount nba = netBarAccountPriDao.findByNetIpAndOwnerIdAndNullity(clientIP, accId, false);
		if (nba == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧超级管理员或被禁用!");
			return rds;
		}
		Account adAcc = accountPriDao.findOne(adminId);
		if (null == adAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户不存在");
			return rds;
		}
		if (adAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标用户被禁用");
			return rds;
		}
		NetBarAdmin nbAdmin = netBarAdminPriDao.findOne(adminId);
		nbAdmin.setName(name);
		nbAdmin.setNullity(nullity);

		rds.setData(nbAdmin);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet exchangePresent(String accId, String clientIP, String[] lanIPs, String serialNum,
			int presentId, int num) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		NetBarAccount nba = netBarAccountPriDao.findByNetIp(clientIP);
		if (nba == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不在网吧内!");
			return rds;
		}
		try {
			if (StringUtils.StringIsEmptyOrNull(nba.getLastAdmin())
					|| DateUtils.addTimeToTime(DateUtils.timeToDate(nba.getLastAdminOnline()),
							Constant.NET_BAR_ADMIN_ONLINE_INTERVAL * 2) < DateUtils.getNowTime()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("老板不在线!");
				return rds;
			}
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(e.getMessage());
			return rds;
		}

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet confirmExchangePresent(String accId, String clientIP, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet bindPc(String accId, String clientIP, String lanIP, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet editBindPc(String accId, String clientIP, String lanIP, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet unBindPc(String accId, String clientIP, String lanIP) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet queryBindPc(String accId, String clientIP, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
