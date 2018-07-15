package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountSecurity;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.CheckUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class AccountSecurityService implements IAccountSecurity {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountUtils accountUtils;

	/**
	 * 暂时测试使用手机查询
	 */
	@Override
	public ResultDataSet queueForgotPassword(String type, String account) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Account mAccount = null;
		//
		try {
			if (type.equals("phone")) {
				mAccount = accountPriDao.findByPhoneNo(account);
			} else {
				mAccount = accountPriDao.findByEmailAddress(account);
			}
			if (mAccount == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("账号不存在");
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("事务失败");
		}
		rds.setData(mAccount);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	// @Override
	// public ResultDataSet resetPassword(String type, String account, String
	// verifCode, String password)
	// throws IOException {
	// ResultDataSet rds = new ResultDataSet();
	// password = password.trim();
	// try {
	// if (!CheckUtils.IsPassword(password)) {
	// rds.setCode(ResultCode.PARAM_INVALID.getCode());
	// rds.setMsg("密码只能为字母和数字,且必须同时包含字母和数字");
	// return rds;
	// }
	// if (password.length() < 8 || password.length() > 20) {
	// rds.setCode(ResultCode.PARAM_INVALID.getCode());
	// rds.setMsg("密码长度为8-20个字符");
	// return rds;
	// }
	// if (CheckUtils.IsNumber(password) && password.length() < 10) {
	// rds.setCode(ResultCode.PARAM_INVALID.getCode());
	// rds.setMsg("密码不能为9位一下纯数字");
	// return rds;
	// }
	// password = StringUtils.getMd5(password);
	// YXResultSet rs = YunxinIM.verifySmscode(account, verifCode);
	// if (!rs.getCode().equals("200")) {
	// System.out.println(rs.getError());
	// rds.setCode(ResultCode.PARAM_INVALID.getCode());
	// rds.setMsg("验证码错误");
	// return rds;
	// }
	// Account mAccount = null;
	// if (type.equals("phone")) {
	// mAccount = accountDao.findByPhoneNo(account);
	// } else {
	// mAccount = accountDao.findByEmailAddress(account);
	// }
	// if (mAccount != null) {
	// mAccount.setPassword(password);
	// accountDao.save(mAccount);
	// RefreshTokenResult refreshTokenResult =
	// YunxinIM.refreshTokenAccount(mAccount.getAccId());
	// if (refreshTokenResult == null) {
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("yx_exception:logid null");
	// return rds;
	// }
	// if (!refreshTokenResult.getCode().equals("200")) {
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("yx_exception:" +
	// CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
	// + refreshTokenResult.getString("desc"));
	// return rds;
	// }
	// if (!refreshTokenResult.getInfo().getAccid().equals(mAccount.getAccId()))
	// {
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("yx_exception:accid dif");
	// return rds;
	// }
	// String token = refreshTokenResult.getInfo().getToken();
	// accountUtils.setToken(mAccount.getAccId(), token);
	// accountUtils.loadHotAccount(mAccount);
	// rds.setData(token);
	// rds.setCode(ResultCode.SUCCESS.getCode());
	// }
	// } catch (Exception e) {
	// logger.error("", e);
	// // throw new RuntimeException("事务失败");
	// rds.setMsg(e.getMessage());
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// }
	// return rds;
	// }

	@Override
	public ResultDataSet resetPassword(String type, String account, String verifyCode, String password)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		password = password.trim();
		try {
			if (!CheckUtils.IsPassword(password)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码只能为字母和数字,且必须同时包含字母和数字");
				return rds;
			}
			if (password.length() < 8 || password.length() > 20) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码长度为8-20个字符");
				return rds;
			}
			if (CheckUtils.IsNumber(password) && password.length() < 10) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码不能为9位一下纯数字");
				return rds;
			}

			Account mAccount = null;
			if (type.equals("accId")) {
				mAccount = accountPriDao.findOne(account);
			} else if (type.equals("openId")) {
				mAccount = accountPriDao.findByOpenId(account);
			} else if (type.equals("phoneNo")) {
				mAccount = accountPriDao.findByPhoneNo(account);
			} else if (type.equals("email")) {
				mAccount = accountPriDao.findByEmailAddress(account);
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("未知的账号类型");
				return rds;
			}
			if (mAccount == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}

			YXResultSet rs = YunxinIM.verifySmscode(mAccount.getBindMobile(), verifyCode);
			if (!rs.getCode().equals("200")) {
				System.out.println(rs.getError());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("验证码错误");
				return rds;
			}

			password = StringUtils.getMd5(password);
			mAccount.setPassword(password);
			accountPriDao.save(mAccount);
			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(mAccount.getAccId());
			if (refreshTokenResult == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:logid null");
				return rds;
			}
			if (!refreshTokenResult.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
						+ refreshTokenResult.getString("desc"));
				return rds;
			}
			if (!refreshTokenResult.getInfo().getAccid().equals(mAccount.getAccId())) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:accid dif");
				return rds;
			}
			String token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(mAccount.getAccId(), token);
			//accountUtils.loadHotAccount(mAccount);
			rds.setData(token);
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
			// throw new RuntimeException("事务失败");
			rds.setMsg(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet sendVerifToPhone(String type, String account) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Account acc = null;
		if (type.equals("accId")) {
			acc = accountPriDao.findOne(account);
		} else if (type.equals("openId")) {
			acc = accountPriDao.findByOpenId(account);
		} else if (type.equals("phoneNo")) {
			acc = accountPriDao.findByPhoneNo(account);
		} else if (type.equals("email")) {
			acc = accountPriDao.findByEmailAddress(account);
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未知的账号类型");
			return rds;
		}
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		String bindMobile = acc.getBindMobile();
		if (StringUtils.StringIsEmptyOrNull(bindMobile)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请先到胖虎直播绑定手机号码");
			return rds;
		}
		YXResultSet rs = YunxinIM.sendSmsCode(bindMobile, bindMobile, "3056623");
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		System.out.println("sendid:" + rs.getString("msg") + "; code:" + rs.getString("obj"));
		ResponseData responseData = new ResponseData();
		String maskPhoneNo = StringUtils.addMask(bindMobile, '*', 2, 2);
		responseData.put("phoneNo", maskPhoneNo);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet isValidVerifyCode(String type, String account, String verifyCode) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = null;
		if (type.equals("accId")) {
			acc = accountPriDao.findOne(account);
		} else if (type.equals("openId")) {
			acc = accountPriDao.findByOpenId(account);
		} else if (type.equals("phoneNo")) {
			acc = accountPriDao.findByPhoneNo(account);
		} else if (type.equals("email")) {
			acc = accountPriDao.findByEmailAddress(account);
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未知的账号类型");
			return rds;
		}
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		boolean valid = false;
		YXResultSet rs = YunxinIM.verifySmscode(acc.getBindMobile(), verifyCode);
		if (rs.getCode().equals("200")) {
			valid = true;
		}
		ResponseData responseData = new ResponseData();
		responseData.put("valid", valid);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet checkLoginPassword(String accId, String password) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByAccIdAndPassword(accId, password);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet verifyBindMobile(String accId, String bindMobile) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		String bind = acc.getBindMobile();
		if(StringUtils.StringIsEmptyOrNull(bind)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("改账号未绑定手机号");
			return rds;
		}
		if(!acc.getBindMobile().equals(bindMobile)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确的密保手机");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
