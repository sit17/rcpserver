package com.i5i58.service.information;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.information.ISearch;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotNewLotAnchorChannel;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.redis.all.HotNewLotAnchorChannelDao;
import com.i5i58.util.MyPageUtils;

@Service(protocol = "dubbo")
public class SearchService implements ISearch {

	@Autowired
	AccountPriDao accountDao;

	@Autowired
	ChannelPriDao channelDao;

	@Autowired
	HotNewLotAnchorChannelDao hotNewLotAnchorChannelDao;

	@Override
	public ResultDataSet queryAccount(String param, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<Account> accounts = accountDao.findByParamWithOpenIdOrNickName(param, pageable);
		for (Account acc : accounts) {
			acc.setPhoneNo(null);
			acc.setEmailAddress(null);
			acc.setFaceOrgUrl(null);
			acc.setAuthed(false);
			acc.setVersion(0);
		}
		rds.setData(MyPageUtils.getMyPage(accounts));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryTeam(String param, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet queryChannel(String param, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<Channel> channels = channelDao.findByParamWithChannelIdOrChannelName(param, pageable);
//		for (Channel ch : channels) {
//			ch.setYunXinCId(null);
//			ch.setYunXinRId(null);
//		}
		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchor(String param, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<Account> accounts = accountDao.findAnchorByParamWithOpenIdOrNickNameOrStageName(param, pageable);
		rds.setData(MyPageUtils.getMyPage(accounts));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNewAnchor(int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotNewLotAnchorChannel> channels = hotNewLotAnchorChannelDao.findByNewLot(1, pageable);

		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryLotWatchAnchor(int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotNewLotAnchorChannel> channels = hotNewLotAnchorChannelDao.findByNewLot(2, pageable);

		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNewLotWatchAnchor(int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotNewLotAnchorChannel> channels = hotNewLotAnchorChannelDao.findAll(pageable);

		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}