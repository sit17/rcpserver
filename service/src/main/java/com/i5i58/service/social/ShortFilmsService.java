package com.i5i58.service.social;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.IShortFilms;
import com.i5i58.data.account.Account;
import com.i5i58.data.social.HotShortFilms;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.redis.all.HotShortFilmsDao;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;

/**
 * Login Account Srevice port for call
 * 
 * @author Lee
 *
 */
@Service(protocol = "dubbo")
public class ShortFilmsService implements IShortFilms {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	HotShortFilmsDao hotShortFilmsDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Override
	public ResultDataSet getShortFilms(Integer pageSize, Integer pageNum, String sortBy, String sortDirection,
			String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号有误");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString(sortDirection), sortBy);
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotShortFilms> data = hotShortFilmsDao.findAll(pageable);
		if (null != data) {
			rds.setData(MyPageUtils.getMyPage(data));
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet upLoadShortFilms(String accId, String url) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号有误");
			return rds;
		}
		Date data = new Date();
		HotShortFilms hotShortFilms = new HotShortFilms();
		hotShortFilms.setAccId(accId);
		hotShortFilms.setFilmUrl(url);
		hotShortFilms.setUploadTime(data.getTime());
		hotShortFilmsDao.save(hotShortFilms);
		return rds;
	}
}
