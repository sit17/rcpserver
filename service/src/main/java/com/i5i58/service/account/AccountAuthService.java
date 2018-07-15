package com.i5i58.service.account;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountAuth;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountAuth;
import com.i5i58.data.anchor.AnchorAuth;
import com.i5i58.data.anchor.AuthType;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.profile.GroupProfileStatus;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AuthAnchorPriDao;
import com.i5i58.primary.dao.account.AuthUserPriDao;
import com.i5i58.primary.dao.profile.GroupProfilePriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.AuthAnchorSecDao;
import com.i5i58.secondary.dao.account.AuthUserSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;
import com.i5i58.util.CheckUtils;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class AccountAuthService implements IAccountAuth {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AuthAnchorPriDao authAnchorPriDao;

	@Autowired
	AuthUserPriDao authUserPriDao;
	
	@Autowired
	GroupProfilePriDao groupProfilePriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	AuthAnchorSecDao authAnchorSecDao;

	@Autowired
	AuthUserSecDao authUserSecDao;
	
	@Autowired
	GroupProfileSecDao groupProfileSecDao;


	@Override
	public ResultDataSet anchorAuth(String accId, String certificateId, String realName, String imgCertificateFace,
			String imgcertificateBack, String imgPerson, String bankCardNum, String bankKeepPhone, String location,
			String bankName) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		AnchorAuth authAnchor = null;

		try {
			//验证是否创建过经纪公司
			List<GroupProfile> groupProfiles = groupProfileSecDao.findByAccId(accId);
			if (groupProfiles != null && groupProfiles.size() > 0){
				for(GroupProfile gp : groupProfiles){
					if (gp.getStatus() == GroupProfileStatus.EXAMINING.getValue()
							|| gp.getStatus() == GroupProfileStatus.NORMAL.getValue()){
						rds.setCode(ResultCode.PARAM_INVALID.getCode());
						rds.setMsg("已经创建经纪公司，不能认证主播");
						return rds;
					}
				}
			}
			if (!CheckUtils.checkIdNumber(certificateId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("身份证号码不合法");
				return rds;
			}
			Account account = accountSecDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			if (account.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已经是主播");
				return rds;
			}

			authAnchor = authAnchorPriDao.findOne(accId);
			if (authAnchor != null){
				if (authAnchor.getAuthed() == AuthType.Success.getValue()){
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("已经认证主播");
					return rds;
				} else if (authAnchor.getAuthed() == AuthType.Check.getValue()){
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("已经在审核");
					return rds;
				}
			}else{
				authAnchor = new AnchorAuth();
				authAnchor.setAccId(accId);
			}
			authAnchor.setCertificateId(certificateId);
			authAnchor.setRealName(realName);
			authAnchor.setImgcertificateBack(imgcertificateBack);
			authAnchor.setImgCertificateFace(imgCertificateFace);
			authAnchor.setImgPerson(imgPerson);
			authAnchor.setAuthed(AuthType.Check.getValue());
			authAnchor.setCreateTime(DateUtils.getNowTime());
			authAnchor.setBankCardNum(bankCardNum);
			authAnchor.setBankKeepPhone(bankKeepPhone);
			authAnchor.setLocation(location);
			authAnchor.setBankName(bankName);
			authAnchorPriDao.save(authAnchor);
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
		}
		rds.setData(authAnchor);
		return rds;
	}

	@Override
	public ResultDataSet userAuth(String accId, String realName, String certificateId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AccountAuth authUser = null;
		try {

			if (!CheckUtils.checkIdNumber(certificateId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("身份证号码不合法");
				return rds;
			}
			Account account = accountPriDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			if (account.isAuthed()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已经是认证用户");
				return rds;
			}
			authUser = authUserPriDao.findByAccId(accId);
			if (authUser != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(authUser.isAuthed() ? "已经审核过" : "正在审核中");
				return rds;
			}
			account.setAuthed(true);
			accountPriDao.save(account);
			authUser = new AccountAuth();
			authUser.setAccId(accId);
			authUser.setAuthed(true);
			authUser.setCertificateId(certificateId);
			authUser.setRealName(realName);
			authUserPriDao.save(authUser);
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
		}

		rds.setData(authUser);
		return rds;
	}

	@Override
	public ResultDataSet getMyUserAuthInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth authAnchor = null;
		try {
			authAnchor = authAnchorSecDao.findOne(accId);
			if (authAnchor == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("你没有申请记录");
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
		}		
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(authAnchor);
		return rds;
	}

	@Override
	public ResultDataSet sendAuthVerifyCode(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		String bindMobile = account.getBindMobile();
		if (StringUtils.StringIsEmptyOrNull(bindMobile)){
			rds.setMsg("未绑定手机");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		
		YXResultSet rs = YunxinIM.sendSmsCode(bindMobile, bindMobile, "3058662");
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		System.out.println("sendid:" + rs.getString("msg") + "; code:" + rs.getString("obj"));
		ResponseData responseData = new ResponseData();
		String maskPhoneNo = StringUtils.addMask(bindMobile, '*', 2, 2);
		responseData.put("bindMobile", maskPhoneNo);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryMyAuthInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AccountAuth accountAuth = null;
		try {
			accountAuth = authUserSecDao.findByAccId(accId);
			if (accountAuth == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("你没有申请记录");
				return rds;
			}
		} catch (Exception e) {
			logger.error("", e);
		}		
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(accountAuth);
		return rds;
	}

}
