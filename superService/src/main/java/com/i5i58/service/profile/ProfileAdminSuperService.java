package com.i5i58.service.profile;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.profile.GroupProfileStatus;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.profile.GroupProfilePriDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.SuperAdminUtils;

@Service(protocol = "dubbo")
public class ProfileAdminSuperService implements com.i5i58.apis.platform.IPlatformProfileAdmin {

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	GroupProfilePriDao groupProfilePriDao;

	@Autowired
	GroupProfileSecDao groupProfileSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Override
	public ResultDataSet verifyGroupProfile(String superAccId, String fId, int topGroupCount, int subGroupCount,
			int channelCount, boolean agree) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GroupProfile groupProfile = groupProfilePriDao.findOne(fId);
		if (groupProfile == null) {

			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该档案");
			return rds;
		}
		if (agree) {
			groupProfile.setCreateTopGroupCount(topGroupCount);
			groupProfile.setCreateSubGroupCount(subGroupCount);
			groupProfile.setCreateChannelCount(channelCount);
			groupProfile.setNullity(false);
			groupProfile.setStatus(GroupProfileStatus.NORMAL.getValue());
			groupProfilePriDao.save(groupProfile);
		} else {
			groupProfile.setStatus(GroupProfileStatus.DISABLE.getValue());
			groupProfilePriDao.save(groupProfile);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateGroupAndChannelCount(String superAccid, String fId, int topGroupCount, int subGroupCount,
			int channelCount) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GroupProfile groupProfile = groupProfilePriDao.findOne(fId);
		if (groupProfile == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该档案");
			return rds;
		}
		groupProfile.setCreateTopGroupCount(topGroupCount);
		groupProfile.setCreateSubGroupCount(subGroupCount);
		groupProfile.setCreateChannelCount(channelCount);
		groupProfilePriDao.save(groupProfile);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryProfileList(String param, int status, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "createDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<GroupProfile> data;
		if (status == 0) {
			data = groupProfileSecDao.findByParam(param, pageable);
		} else {
			data = groupProfileSecDao.findByParamWithStatus(param, status, pageable);
		}
		rds.setData(MyPageUtils.getMyPage(data));
		return rds;
	}

	@Override
	public ResultDataSet queryTopGroupsInProfile(String fId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ChannelGroup> groups = channelGroupSecDao.findTopGroupByFId(fId);
		rds.setData(groups);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getProfile(String fId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GroupProfile profile = groupProfileSecDao.findByFId(fId);
		rds.setData(profile);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet enableProfile(String superAccId, String fId, boolean enable) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GroupProfile profile = groupProfilePriDao.findByFId(fId);
		profile.setNullity(!enable);
		groupProfilePriDao.save(profile);
		superAdminUtils.superAdminRecord(superAccId, "禁用/开启经纪公司{fId:%s,enable:%s}", fId, enable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
