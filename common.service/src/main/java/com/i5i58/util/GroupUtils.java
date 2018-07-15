package com.i5i58.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.GroupAdminor;
import com.i5i58.data.group.GroupAuth;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.superAdmin.SuperAdmin;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelWatchingRecordPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.GroupAdminorPriDao;
import com.i5i58.primary.dao.profile.GroupProfilePriDao;
import com.i5i58.primary.dao.superAdmin.SuperAdminPriDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;

@Component
public class GroupUtils {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelAdminorPriDao channelAdminorPriDao;

	@Autowired
	AuthVerify<GroupAuth> groupAuthVerify;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	GroupAdminorPriDao groupAdminorPriDao;

	@Autowired
	SuperAdminPriDao superAdminPriDao;

	@Autowired
	GroupProfilePriDao groupProfilePriDao;

	@Autowired
	GroupProfileSecDao groupProfileSecDao;

	@Autowired
	HotChannelMicDao hotChannelMicDao;

	@Autowired
	ChannelWatchingRecordPriDao channelWatchingRecordPriDao;

	public int getPermission(GroupAuth... authortys) {
		return groupAuthVerify.getPermission(authortys);
	}

	public int getGroupAdminPermission() {
		return groupAuthVerify.getPermission(GroupAuth.ASSIGN_GROUP_ADMIN, GroupAuth.ASSIGN_GROUP_OWNER,
				GroupAuth.ACCESS_CONTRACT, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY);
	}

	/**
	 * 验证组权限
	 * 
	 * @param group
	 * @param accId
	 * @param auth
	 * @return
	 */
	public boolean verifyGroupAuth(ChannelGroup group, String accId, GroupAuth auth) {
		if (group != null) {
			if (auth != GroupAuth.ASSIGN_GROUP_OWNER) {
				if (group.getOwnerId().equals(accId)) {
					return true;
				}
				if (auth != GroupAuth.ASSIGN_GROUP_ADMIN) {
					GroupAdminor groupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, group.getgId());
					if (groupAdminor != null) {
						if (groupAuthVerify.Verify(auth, groupAdminor.getAdminRight())) {
							return true;
						}
					}
				}
				if (StringUtils.StringIsEmptyOrNull(group.getProfileId())) {
					GroupProfile groupProfile = groupProfilePriDao.findByFId(group.getProfileId());
					if (groupProfile != null && groupProfile.getAccId().equals(accId)) {
						return true;
					}
				}
			}
			ChannelGroup topGroup = channelGroupPriDao.findByGId(group.getParentId());
			if (topGroup != null) {
				if (topGroup.getOwnerId().equals(accId)) {
					return true;
				}
				GroupAdminor topGroupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, group.getParentId());
				if (topGroupAdminor != null) {
					if (groupAuthVerify.Verify(auth, topGroupAdminor.getAdminRight())) {
						return true;
					}
				}
				if (StringUtils.StringIsEmptyOrNull(group.getProfileId())) {
					GroupProfile groupProfile = groupProfilePriDao.findByFId(group.getProfileId());
					if (groupProfile != null && groupProfile.getAccId().equals(accId)) {
						return true;
					}
				}
			}
		}
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}

	public boolean verifyTopGroupMember(ChannelGroup group, String accId) {
		if (group != null) {
			if (group.getOwnerId().equals(accId)) {
				return true;
			}
			GroupAdminor topGroupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, group.getgId());
			if (topGroupAdminor != null) {
				return true;
			}
		}
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}

	public boolean checkGroupCount(ChannelGroup group) {
		GroupProfile groupProfile = groupProfileSecDao.findOne(group.getProfileId());
		List<ChannelGroup> ChannelGroups = channelGroupSecDao.findByParentId(group.getgId());
		if (groupProfile.getCreateSubGroupCount() > ChannelGroups.size()) {
			return true;
		}
		return false;
	}

	public boolean checkTopGroupCount(GroupProfile groupProfile) {
		List<ChannelGroup> ChannelGroups = channelGroupSecDao.findTopGroupByFId(groupProfile.getfId());
		if (groupProfile.getCreateTopGroupCount() > ChannelGroups.size()) {
			return true;
		}
		return false;
	}

}
