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
import com.i5i58.apis.account.IAccountBehavior;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.CollectChannel;
import com.i5i58.data.account.MyGame;
import com.i5i58.data.account.NearWatchChannel;
import com.i5i58.data.account.UserReport;
import com.i5i58.data.channel.Channel;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.CollectChannelPriDao;
import com.i5i58.primary.dao.account.MyGamePriDao;
import com.i5i58.primary.dao.account.NearWatchChannelPriDao;
import com.i5i58.primary.dao.account.UserReportPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.CollectChannelSecDao;
import com.i5i58.secondary.dao.account.MyGameSecDao;
import com.i5i58.secondary.dao.account.NearWatchChannelSecDao;
import com.i5i58.secondary.dao.account.UserReportSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;

@Service(protocol = "dubbo")
public class AccountBehaviorService implements IAccountBehavior {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	CollectChannelPriDao collectChannelPriDao;

	@Autowired
	NearWatchChannelPriDao nearWatchChannelPriDao;

	@Autowired
	MyGamePriDao myGamePriDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	UserReportPriDao userReportPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	CollectChannelSecDao collectChannelSecDao;

	@Autowired
	NearWatchChannelSecDao nearWatchChannelSecDao;

	@Autowired
	MyGameSecDao myGameSecDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	UserReportSecDao userReportSecDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Override
	public ResultDataSet addCollectChannel(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		CollectChannel collectChannel = collectChannelPriDao.findByAccIdAndCId(accId, cId);
		long time = DateUtils.getNowTime();
		if (collectChannel == null) {
			CollectChannel newCollectChannel = new CollectChannel();
			newCollectChannel.setAccId(accId);
			newCollectChannel.setcId(cId);
			newCollectChannel.setCollectTime(time);
			collectChannelPriDao.save(newCollectChannel);
		} else {
			collectChannel.setCollectTime(time);
			collectChannelPriDao.save(collectChannel);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getCollectChannel(String accId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<CollectChannel> CollectChannels = collectChannelSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(CollectChannels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet cancelCollectChannel(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		CollectChannel collectChannel = collectChannelPriDao.findByAccIdAndCId(accId, cId);
		if (collectChannel == null) {
			rds.setMsg("频道未收藏,无法取消");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		collectChannelPriDao.delete(collectChannel);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet isChannelCollected(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ResponseData rp = new ResponseData();
		CollectChannel collectChannel = collectChannelSecDao.findByAccIdAndCId(accId, cId);
		if (collectChannel == null) {
			rp.put("collected", false);
		} else {
			rp.put("collected", true);
		}
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addNearWatchChannel(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		NearWatchChannel nearWatchChannel = nearWatchChannelPriDao.findByAccIdAndCId(accId, cId);
		long time = DateUtils.getNowTime();
		if (nearWatchChannel == null) {
			if (nearWatchChannelPriDao.CountByAccIdAndCId(accId, cId) >= Constant.NEAR_WATCH_CHANNEL_MAX_SIZE) {
				Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
				Pageable pageable = new PageRequest(0, 1, sort);
				Page<NearWatchChannel> farestNearWatchChannel = nearWatchChannelPriDao.findByAccId(accId, pageable);
				nearWatchChannelPriDao.delete(farestNearWatchChannel);
			}
			NearWatchChannel newNearWatchChannel = new NearWatchChannel();
			newNearWatchChannel.setAccId(accId);
			newNearWatchChannel.setcId(cId);
			newNearWatchChannel.setCollectTime(time);
			nearWatchChannelPriDao.save(newNearWatchChannel);
		} else {
			nearWatchChannel.setCollectTime(time);
			nearWatchChannelPriDao.save(nearWatchChannel);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getNearWatchChannel(String accId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<NearWatchChannel> nearWatchChannel = nearWatchChannelSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(nearWatchChannel));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addMyGame(String accId, int kindId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		MyGame myGame = myGamePriDao.findByAccIdAndKindId(accId, kindId);
		long time = DateUtils.getNowTime();
		if (myGame == null) {
			MyGame newMyGame = new MyGame();
			newMyGame.setAccId(accId);
			newMyGame.setKindId(kindId);
			newMyGame.setCollectTime(time);
			myGamePriDao.save(newMyGame);
		} else {
			myGame.setCollectTime(time);
			myGamePriDao.save(myGame);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyGame(String accId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<MyGame> myGames = myGameSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(myGames));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet reportUser(String accId, String reportedUser, String reason) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Account account = accountPriDao.findOne(reportedUser);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if(reason.length() > 300){
			rds.setMsg("举报内容不能超过300字符");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		UserReport userReport = new UserReport();
		userReport.setFromAccId(accId);
		userReport.setReportedAccId(reportedUser);
		userReport.setReason(reason);
		userReport.setReportDate(DateUtils.getNowTime());
		userReportPriDao.save(userReport);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet useFaceInGame(String accId, int useFace) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号");
			return rds;
		}
		account.setFaceUseInGame(useFace == 1);
		accountPriDao.save(account);
		// HotAccount hotAccount = hotAccountDao.findOne(accId);
		// if (hotAccount != null) {
		// hotAccount.setFaceUseInGame(useFace == 1);
		// }
		// hotAccountDao.save(hotAccount);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
