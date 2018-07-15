package com.i5i58.service.netBar;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.netBar.INetBar;
import com.i5i58.data.account.Account;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.netBar.NetBarAgent;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.wechat.WechatAccount;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAgentPriDao;
import com.i5i58.primary.dao.netBar.NetBarIncomeMapper;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.wechat.WechatAccountPriDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.SecurityUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;

@Service(protocol = "dubbo")
public class NetBarService implements INetBar {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	TaskUtil taskUtil;

	@Autowired
	WechatAccountPriDao wechatAccountPriDao;

	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;

	@Autowired
	NetBarAgentPriDao netBarAgentPriDao;

	@Autowired
	NetBarIncomeMapper netBarIncomeMapper;

	@Autowired
	ConfigUtils configUtils;

	private ResultDataSet login(Account acc, String clientIP, int device, String serialNum, boolean byToken)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
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
		NetBarAccount nbAcc = netBarAccountPriDao.findByOwnerId(acc.getAccId());
		if (nbAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧管理员");
			return rds;
		}
		if (nbAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("网吧管理员被禁用！");
			return rds;
		}
		// ================JWT================

		acc.setLastLoginIp(clientIP);
		String yxToken = acc.getYxToken();
		if (StringUtils.StringIsEmptyOrNull(yxToken)) {
			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
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
		ResponseData response = new ResponseData();
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
		rds.setData(response);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		// ================JWT================

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	private ResultDataSet loginForAgent(Account acc, String clientIP, int device, String serialNum, boolean byToken)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
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
		NetBarAgent nbAcc = netBarAgentPriDao.findByOwnerId(acc.getAccId());
		if (nbAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不是网吧代理！");
			return rds;
		}
		if (nbAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("网吧代理被禁用！");
			return rds;
		}
		// ================JWT================

		acc.setLastLoginIp(clientIP);
		String yxToken = acc.getYxToken();
		if (StringUtils.StringIsEmptyOrNull(yxToken)) {
			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
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
		ResponseData response = new ResponseData();
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
		rds.setData(response);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		// ================JWT================

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginByOpenIdForAgent(String openId, int version, String clientIP, int device,
			String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		// Account acc = accountPriDao.findByOpenIdAndPassword(openId,
		// password);
		WechatAccount Waccount = wechatAccountPriDao.findByOpenIdAndSelected(openId, true);
		try {
			if (Waccount != null) {
				Account acc = accountPriDao.findOne(Waccount.getAccId());
				rds = loginForAgent(acc, clientIP, device, serialNum, false);
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(e.getMessage());
		}
		return rds;
	}

	@Override
	public ResultDataSet loginByOpenId(String openId, int version, String clientIP, int device, String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		// Account acc = accountPriDao.findByOpenIdAndPassword(openId,
		// password);
		WechatAccount Waccount = wechatAccountPriDao.findByOpenIdAndSelected(openId, true);
		try {
			if (Waccount != null) {
				Account acc = accountPriDao.findOne(Waccount.getAccId());
				rds = login(acc, clientIP, device, serialNum, false);
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(e.getMessage());
		}
		return rds;
	}

	@Override
	public ResultDataSet queryGiftRecord(String accId, long startDate, long endDate, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGiftIncome(String accId, long startDate, long endDate) {
		ResultDataSet rds = new ResultDataSet();
		NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);
		if (nbAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该网吧账号不存在");
			return rds;
		}
		long income = netBarIncomeMapper.selectSumGiftByIp(nbAcc.getNetIp(), startDate, endDate);
		rds.setData(income);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGameIncome(String accId, long startDate, long endDate) {
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNetBarByAgent(String accId, int pageNum, int pageSize, boolean viewAll) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "addr");
		Sort sort1 = new Sort(Direction.fromString("desc"), "name");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		NetBarAgent ag = netBarAgentPriDao.findByNullityAndOwnerId(false, accId);
		if (ag == null) {
			rds.setMsg("没有这个代理商账号！");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
		}
		if (viewAll) {
			Page<NetBarAccount> data = netBarAccountPriDao.findByAgId(ag.getAgId(), pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			Page<NetBarAccount> data = netBarAccountPriDao.findByNullityAndAgId(false, accId, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet queryAccountKind(String openId) {
		ResultDataSet rds = new ResultDataSet();

		WechatAccount Waccount = wechatAccountPriDao.findByOpenIdAndSelected(openId, true);
		if (Waccount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的微信还未绑定过平台账号，请先绑定！");
		}
		Account acc = accountPriDao.findOne(Waccount.getAccId());
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该账号不存在！");
		}
		NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, acc.getAccId());

		NetBarAgent agAcc = netBarAgentPriDao.findByNullityAndOwnerId(false, acc.getAccId());

		ResponseData response = new ResponseData();
		if (nbAcc != null && agAcc != null) {
			response.put("UserKind", 1);
		} else if (nbAcc == null && agAcc != null) {
			response.put("UserKind", 2);
		} else if (nbAcc != null && agAcc == null) {
			response.put("UserKind", 3);
		} else {
			response.put("UserKind", 0);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryPayRebate(String accId, String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		try {
			String otherAccId = accId;
			NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);
			if (nbAcc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号不存在！");
			}
			if (nbAcc.getNetIp().isEmpty()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号还没绑定IP！");
			}
			String ip = nbAcc.getNetIp();
			NetBarAgent agAcc = netBarAgentPriDao.findByNullityAndOwnerId(false, accId);

			if (agAcc != null) {
				otherAccId = agAcc.getOwnerId();
			}
			long countRebate = netBarIncomeMapper.CountPayRebateByAccIdAndIp(accId, otherAccId, ip, searchMonth);
			String num = configUtils.getPlatformConfig(Constant.NETBAR_PAYREBATE_RATE);
			ResponseData response = new ResponseData();
			response.put("rebate", countRebate);
			response.put("rate", num);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("服务器错误");
			return rds;
		}
	}

	@Override
	public ResultDataSet queryExChangeScoreRebate(String accId, String searchMonth) {

		ResultDataSet rds = new ResultDataSet();
		// String otherAccId = accId;
		try {
			NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);
			if (nbAcc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号不存在！");
			}
			if (nbAcc.getNetIp().isEmpty()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号还没绑定IP！");
			}
			String ip = nbAcc.getNetIp();
			// NetBarAgent agAcc =
			// netBarAgentPriDao.findByNullityAndOwnerId(false,
			// accId);
			//
			// if (agAcc != null) {
			// otherAccId = agAcc.getOwnerId();
			// }
			long countRebate = netBarIncomeMapper.CountExChangeScoreRebateByAccIdAndIp(ip, searchMonth);

			String num;
			num = configUtils.getPlatformConfig(Constant.NETBAR_EXCHANGESCOREREBATENUM);

			long minAndMaxNum = Long.valueOf(num);
			String rate = configUtils.getPlatformConfig(Constant.NETBAR_EXCHANGESCOREREBATEMIN_RATE);
			if (countRebate >= minAndMaxNum) {
				rate = configUtils.getPlatformConfig(Constant.NETBAR_EXCHANGESCOREREBATEMAX_RATE);
			}
			ResponseData response = new ResponseData();
			response.put("rebate", countRebate);
			response.put("rate", rate);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("服务器错误");
			return rds;
		}
	}

	@Override
	public ResultDataSet queryGiveGiftRebate(String accId, String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		try {
			String otherAccId = accId;
			NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);
			if (nbAcc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号不存在！");
			}
			if (nbAcc.getNetIp().isEmpty()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号还没绑定IP！");
			}
			String ip = nbAcc.getNetIp();
			NetBarAgent agAcc = netBarAgentPriDao.findByNullityAndOwnerId(false, accId);

			if (agAcc != null) {
				otherAccId = agAcc.getOwnerId();
			}
			long countRebate = netBarIncomeMapper.CountGivenGiftRebateByAccIdAndIp(accId, otherAccId, ip, searchMonth);

			String num;
			num = configUtils.getPlatformConfig(Constant.NETBAR_INLIVEGIVEGIFTREBATE_RATE);

			ResponseData response = new ResponseData();
			response.put("rebate", countRebate);
			response.put("rate", num);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (Exception e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("服务器错误");
			return rds;
		}
	}

	@Override
	public ResultDataSet loginByOpenIdForWeb(String openId, String password, int version, String clientIP, int device,
			String serialNum) {

		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
		try {
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}
			rds = login(acc, clientIP, device, serialNum, false);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(e.getMessage());
		}
		return rds;
	}

	@Override
	public ResultDataSet loginByOpenIdForAgentForWeb(String openId, String password, int version, String clientIP,
			int device, String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
		try {
			if (acc == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				return rds;
			}
			rds = loginForAgent(acc, clientIP, device, serialNum, false);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(e.getMessage());
		}
		return rds;
	}

}
