package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IBusinessAgent;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.BusinessAgent;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.BusinessAgentPriDao;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;

@Service(protocol = "dubbo")
public class BusinessAgentService implements IBusinessAgent {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	TaskUtil taskUtil;

	@Autowired
	BusinessAgentPriDao businessAgentPriDao;

	@Override
	public ResultDataSet loginByOpenId(String openId, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
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
		BusinessAgent businessAgent = businessAgentPriDao.findOne(acc.getAccId());
		if (businessAgent == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误:0x01");
			return rds;
		}
		if (businessAgent.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误:0x02");
			return rds;
		}
		// if (device == 1 &&
		// !StringUtils.StringIsEmptyOrNull(acc.getLastPcLoginIp())) {
		// if (!acc.getLastPcLoginIp().equals(clientIP)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// if (!acc.getLastPcLoginSerialNum().equals(serialNum)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// }

		String token = accountUtils.getToken(acc.getAccId());
		if (token == null) {
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
			token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(acc.getAccId(), token);
		}

		if (device == DeviceCode.PCLive) {
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		accountPriDao.save(acc);
		// accountUtils.loadHotAccount(acc);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		ResponseData response = new ResponseData();
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
		response.put("acc", acc);
		response.put("token", token);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addAgent(String agentAdmin, String openId, String name, String phone, String qq,
			String clientIP) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(agentAdmin);
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
		BusinessAgent businessAgent = businessAgentPriDao.findOne(acc.getAccId());
		if (businessAgent == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x01");
			return rds;
		}
		if (businessAgent.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x02");
			return rds;
		}
		if (businessAgent.getAdminRight() < Constant.BUSINESS_AGENT_ADMIN) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		// ***********************
		Account agentAcc = accountPriDao.findByOpenId(openId);
		if (null == agentAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标账户不存在");
			return rds;
		}
		if (agentAcc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标账号被禁用");
			return rds;
		}
		BusinessAgent target = businessAgentPriDao.findOne(agentAcc.getAccId());
		if (target != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标已存在");
			return rds;
		}
		BusinessAgent agent = new BusinessAgent();
		agent.setAccId(agentAcc.getAccId());
		agent.setOpenId(openId);
		agent.setAgentName(name);
		agent.setAgentPhone(phone);
		agent.setAgentQQ(qq);
		agent.setNullity(false);
		agent.setAdminRight(0);
		agent.setCollectTime(DateUtils.getNowTime());
		businessAgentPriDao.save(agent);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAgent(String agentAdmin, String openId, String clientIP) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(agentAdmin);
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
		BusinessAgent businessAgent = businessAgentPriDao.findOne(acc.getAccId());
		if (businessAgent == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x01");
			return rds;
		}
		if (businessAgent.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x02");
			return rds;
		}
		if (businessAgent.getAdminRight() < Constant.BUSINESS_AGENT_ADMIN) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		// ***********************
		Account agentAcc = accountPriDao.findByOpenId(openId);
		if (null == agentAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标账户不存在");
			return rds;
		}
		BusinessAgent target = businessAgentPriDao.findOne(agentAcc.getAccId());
		if (target == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标不存在");
			return rds;
		}
		businessAgentPriDao.delete(target);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet nullityAgent(String agentAdmin, String openId, boolean nullity, String clientIP)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(agentAdmin);
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
		BusinessAgent businessAgent = businessAgentPriDao.findOne(acc.getAccId());
		if (businessAgent == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x01");
			return rds;
		}
		if (businessAgent.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x02");
			return rds;
		}
		if (businessAgent.getAdminRight() < Constant.BUSINESS_AGENT_ADMIN) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		// ***********************
		Account agentAcc = accountPriDao.findByOpenId(openId);
		if (null == agentAcc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标账户不存在");
			return rds;
		}
		BusinessAgent target = businessAgentPriDao.findOne(agentAcc.getAccId());
		if (target == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("目标不存在");
			return rds;
		}
		target.setNullity(nullity);
		businessAgentPriDao.save(target);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet listAgent(String agent, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(agent);
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
		BusinessAgent businessAgent = businessAgentPriDao.findOne(acc.getAccId());
		if (businessAgent == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x01");
			return rds;
		}
		if (businessAgent.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在:0x02");
			return rds;
		}
		if (businessAgent.getAdminRight() < Constant.BUSINESS_AGENT_ADMIN) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		// ***********************
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<BusinessAgent> agents = businessAgentPriDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(agents));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

}
