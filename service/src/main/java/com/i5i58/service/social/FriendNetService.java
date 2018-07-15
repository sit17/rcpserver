package com.i5i58.service.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.i5i58.apis.social.IFriendsNet;
import com.i5i58.data.account.Account;
import com.i5i58.data.social.FollowInfo;
import com.i5i58.data.social.FollowItem;
import com.i5i58.data.social.SocialVersion;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.social.AttentionInfoPriDao;
import com.i5i58.primary.dao.social.FansInfoPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.primary.dao.social.SocialVersionPriDao;
import com.i5i58.secondary.dao.social.FollowInfoSecDao;
import com.i5i58.util.MyPage;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class FriendNetService implements IFriendsNet {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AttentionInfoPriDao attentionInfoPriDao;

	@Autowired
	FansInfoPriDao fansInfoPriDao;

	@Autowired
	SocialVersionPriDao socialVersionPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	FollowInfoSecDao followInfoSecDao;

	@Override
	public ResultDataSet getEssays(Integer pageSize, Integer pageNum, String sortBy, String sortDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet postEssay(String accId, String type, String url, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateSocialAttentionVersion(String accId) {
		SocialVersion socialVersion = socialVersionPriDao.findOne(accId);
		if (socialVersion != null) {
			socialVersion.setAttentionVersion(StringUtils.createUUID());
			socialVersionPriDao.save(socialVersion);
		} else {
			SocialVersion newSocialVersion = new SocialVersion();
			newSocialVersion.setAccId(accId);
			newSocialVersion.setAttentionVersion(StringUtils.createUUID());
			socialVersionPriDao.save(newSocialVersion);
		}
	}

	private void updateSocialFansVersion(String accId) {
		SocialVersion socialVersion = socialVersionPriDao.findOne(accId);
		if (socialVersion != null) {
			socialVersion.setFansVersion(StringUtils.createUUID());
			socialVersionPriDao.save(socialVersion);
		} else {
			SocialVersion newSocialVersion = new SocialVersion();
			newSocialVersion.setAccId(accId);
			newSocialVersion.setFansVersion(StringUtils.createUUID());
			socialVersionPriDao.save(newSocialVersion);
		}
	}

	@Override
	public ResultDataSet follow(String accId, String attAccId) {
		ResultDataSet rds = new ResultDataSet();
		if(accId.equals(attAccId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能关注自己");
			return rds;
		}
		Account attAcc = accountPriDao.findOne(attAccId);
		if (attAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		/*
		 * List<AttentionInfo> attention = attentionInfoDao.findByAccId(accId);
		 * if (attention != null && attention.size() > 0) { for (AttentionInfo
		 * att : attention) { if (att.getAttention().split(",").length < 100) {
		 * if (att.getAttention().length() > 0) { if
		 * (att.getAttention().contains(attAccId)) {
		 * rds.setCode(ResultCode.PARAM_INVALID.getCode()); rds.setMsg("已关注");
		 * return rds; } att.setAttention(att.getAttention() + "," + attAccId);
		 * } else { att.setAttention(attAccId); } attentionInfoDao.save(att);
		 * break; } } } else { AttentionInfo att = new AttentionInfo();
		 * att.setAccId(accId); att.setAttention(attAccId);
		 * attentionInfoDao.save(att); } updateSocialAttentionVersion(accId);
		 * 
		 * List<FansInfo> fansInfo = fansInfoDao.findByAccId(attAccId); if
		 * (fansInfo != null && fansInfo.size() > 0) { for (FansInfo fans :
		 * fansInfo) { if (fans.getFans().split(",").length < 100) { if
		 * (fans.getFans().length() > 0) { fans.setFans(fans.getFans() + "," +
		 * accId); } else { fans.setFans(accId); } fansInfoDao.save(fans);
		 * break; } } } else { FansInfo fans = new FansInfo();
		 * fans.setAccId(attAccId); fans.setFans(accId); fansInfoDao.save(fans);
		 * } updateSocialFansVersion(attAccId);
		 */
		if (followInfoPriDao.findByAccIdAndFansId(attAccId, accId) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("已关注");
			return rds;
		}
		FollowInfo followInfo = new FollowInfo();
		followInfo.setAccId(attAccId);
		followInfo.setFansId(accId);
		followInfoPriDao.save(followInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet cancelFollow(String accId, String attAccId) {
		ResultDataSet rds = new ResultDataSet();
		Account attAcc = accountPriDao.findOne(attAccId);
		if (attAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		/*
		 * List<AttentionInfo> attention = attentionInfoDao.findByAccId(accId);
		 * if (attention != null && attention.size() > 0) { for (AttentionInfo
		 * att : attention) { if (att.getAttention().contains(attAccId)) {
		 * String res = att.getAttention().replace(attAccId + ",",
		 * "").replace(attAccId, ""); if (res.endsWith(",")) { res =
		 * res.substring(0, res.length() - 1); } att.setAttention(res);
		 * attentionInfoDao.save(att); updateSocialAttentionVersion(accId);
		 * break; } else { rds.setCode(ResultCode.PARAM_INVALID.getCode());
		 * rds.setMsg("没有关注TA"); return rds; } } }
		 * 
		 * List<FansInfo> fansInfo = fansInfoDao.findByAccId(attAccId); if
		 * (fansInfo != null && fansInfo.size() > 0) { for (FansInfo fans :
		 * fansInfo) { if (fans.getFans().contains(accId)) { String res =
		 * fans.getFans().replace(accId + ",", "").replace(accId, ""); if
		 * (res.endsWith(",")) { res = res.substring(0, res.length() - 1); }
		 * fans.setFans(res); fansInfoDao.save(fans);
		 * updateSocialFansVersion(attAccId); break; } } }
		 */
		FollowInfo followInfo = followInfoPriDao.findByAccIdAndFansId(attAccId, accId);
		if (followInfo == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有关注TA");
			return rds;
		}
		followInfoPriDao.delete(followInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyFollow(String accId, Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "id");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		Page<String> follows = followInfoSecDao.findFollow(accId, pageable);
		List<FollowItem> followItems = new ArrayList<FollowItem>();
		for (String str : follows.getContent()) {
			FollowItem followItem = new FollowItem();
			followItem.setAccId(str);
			byte type = followStatus(accId, str);
			followItem.setType(type);
			followItems.add(followItem);
		}
		MyPage myPage = MyPageUtils.getMyPage(follows);
		myPage.setContent(followItems);
		rds.setData(myPage);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyFans(String accId, Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "id");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		Page<String> fans = followInfoSecDao.findFans(accId, pageable);
		List<FollowItem> followItems = new ArrayList<FollowItem>();
		for (String str : fans.getContent()) {
			FollowItem followItem = new FollowItem();
			followItem.setAccId(str);
			byte type = followStatus(accId, str);
			followItem.setType(type);
			followItems.add(followItem);
		}
		MyPage myPage = MyPageUtils.getMyPage(fans);
		myPage.setContent(followItems);
		rds.setData(myPage);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTaFollow(String accId, String ta, Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		/*
		 * SocialVersion socialVersion = socialVersionDao.findOne(accId); if
		 * (socialVersion != null) { if
		 * (!socialVersion.getAttentionVersion().equals(version)) {
		 * List<AttentionInfo> attention = attentionInfoDao.queryByAccId(accId);
		 * if (attention != null && attention.size() > 0) { AttentionResponse
		 * response = new AttentionResponse();
		 * response.setAccId(attention.get(0).getAccId()); String str = ""; for
		 * (AttentionInfo att : attention) { str += att.getAttention() + ","; }
		 * response.setAttention(str.substring(0, str.length() - 1));
		 * response.setVersion(socialVersion.getAttentionVersion());
		 * rds.setData(response); } } }
		 */
		Sort sort = new Sort(Direction.fromString("desc"), "id");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		Page<String> follows = followInfoSecDao.findFollow(ta, pageable);
		List<FollowItem> followItems = new ArrayList<FollowItem>();
		for (String str : follows.getContent()) {
			FollowItem followItem = new FollowItem();
			followItem.setAccId(str);
			byte type = followStatus(accId, str);
			followItem.setType(type);
			followItems.add(followItem);
		}
		MyPage myPage = MyPageUtils.getMyPage(follows);
		myPage.setContent(followItems);
		rds.setData(myPage);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTaFans(String accId, String ta, Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		/*
		 * SocialVersion socialVersion = socialVersionDao.findOne(accId); if
		 * (socialVersion != null) { if
		 * (!socialVersion.getFansVersion().equals(version)) { List<FansInfo>
		 * fansInfo = fansInfoDao.queryByAccId(accId); if (fansInfo != null &&
		 * fansInfo.size() > 0) { FansResponse response = new FansResponse();
		 * response.setAccId(fansInfo.get(0).getAccId()); String str = ""; for
		 * (FansInfo fans : fansInfo) { str += fans.getFans() + ","; }
		 * response.setFans(str.substring(0, str.length() - 1));
		 * response.setVersion(socialVersion.getFansVersion());
		 * rds.setData(response); } } }
		 */
		Sort sort = new Sort(Direction.fromString("desc"), "id");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		Page<String> fans = followInfoSecDao.findFans(ta, pageable);
		List<FollowItem> followItems = new ArrayList<FollowItem>();
		for (String str : fans.getContent()) {
			FollowItem followItem = new FollowItem();
			followItem.setAccId(str);
			byte type = followStatus(accId, str);
			followItem.setType(type);
			followItems.add(followItem);
		}
		MyPage myPage = MyPageUtils.getMyPage(fans);
		myPage.setContent(followItems);
		rds.setData(myPage);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTaFansCount(String accId, String ta) {
		ResultDataSet rds = new ResultDataSet();
		int count = followInfoSecDao.countFansByAccId(ta);
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	private byte followStatus(String accId, String target) {
		FollowInfo followInfo = followInfoPriDao.findByAccIdAndFansId(target, accId);
		FollowInfo fansInfo = followInfoPriDao.findByAccIdAndFansId(accId, target);
		if (followInfo == null && fansInfo == null) {
			return 0;
		} else if (followInfo != null && fansInfo == null) {
			return 1;
		} else if (followInfo == null && fansInfo != null) {
			return 2;
		} else if (followInfo != null && fansInfo != null) {
			return 3;
		}
		return -1;
	}

	@Override
	public ResultDataSet getFollowStatus(String accId, String target) {
		ResultDataSet rds = new ResultDataSet();
		byte res = followStatus(accId, target);
		rds.setData(res);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet isFollow(String accId, String target) {
		ResultDataSet rds = new ResultDataSet();
		FollowInfo followInfo = followInfoPriDao.findByAccIdAndFansId(target, accId);
		if (followInfo == null) {
			rds.setData(false);
		} else {
			rds.setData(true);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFollowCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		int count = followInfoPriDao.countFollowByAccId(accId);
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFansCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		int count = followInfoPriDao.countFansByAccId(accId);
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFollowFansCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		int followCount = followInfoPriDao.countFollowByAccId(accId);
		int fansCount = followInfoPriDao.countFansByAccId(accId);
		Map<String, Integer> response = new HashMap<String, Integer>();
		response.put("follow", followCount);
		response.put("fans", fansCount);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
