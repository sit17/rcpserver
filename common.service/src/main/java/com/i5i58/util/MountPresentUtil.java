/*
 * 赠送坐骑
 * @author songfl
 * */

package com.i5i58.util;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.account.MountStore;
import com.i5i58.data.channel.HotChannelMount;
import com.i5i58.data.channel.MountPresent;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.primary.dao.account.MountStorePriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.MountPresentPriDao;
import com.i5i58.primary.dao.record.RecordConsumptionPriDao;
import com.i5i58.redis.all.HotChannelMountDao;

@Component
public class MountPresentUtil {
	private Logger logger = Logger.getLogger(getClass());

	public static final int TYPE_PAY = 1;
	public static final int TYPE_OPENGUARD = 2;
	public static final int TYPE_BUYVIP = 3;

	@Autowired
	MountPresentPriDao mountPresentPriDao;

	@Autowired
	MountStorePriDao mountStorePriDao;

	@Autowired
	RecordConsumptionPriDao recordConsumptionPriDao;

	@Autowired
	HotChannelMountDao hotChannelMountDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	public MountPresent getDefaultMount(int type, int level) {
		MountPresent mount = new MountPresent();
		switch (type) {
		case TYPE_PAY:
			mount.setType(TYPE_PAY);
			mount.setMountId(1);
			mount.setMonth(1);
			break;
		case TYPE_OPENGUARD:
			mount.setType(TYPE_OPENGUARD);
			if (level == 1 || level == 2) {
				mount.setMountId(101);
				mount.setMonth(1);
			} else {
				mount.setMountId(102);
				mount.setMonth(1);
			}
			break;
		case TYPE_BUYVIP:
			mount.setType(TYPE_BUYVIP);
			if (level == 1 || level == 2) {
				mount.setMountId(201);
				mount.setMonth(1);
			} else {
				mount.setMountId(202);
				mount.setMonth(1);
			}
			break;
		default:
			mount = null;
			break;
		}

		return mount;
	}

	public boolean presentMountForPay(String accId) {
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(TYPE_PAY, 1);
		if (mountPresent == null) {
			mountPresent = getDefaultMount(TYPE_PAY, 1);
		}
		if (mountPresent == null) {
			logger.error("首充赠送坐骑失败, 没有赠送配置");
			return false;
		}

		HotChannelMount mount = hotChannelMountDao.findOne(mountPresent.getMountId());
		if (mount == null) {
			logger.error("首充赠送坐骑失败, 没有坐骑配置，坐骑 id=" + mountPresent.getMountId());
			return false;
		}

		long date = 0L;
		long deadline = 0L;
		MountStore mountStore = mountStorePriDao.findByAccIdAndMountsId(accId, mountPresent.getMountId());

		try {
			date = DateUtils.getNowDate();
			if (mountStore != null) {
				if (mountStore.getEndTime() >= date) {
					deadline = DateUtils.AddMonth(mountStore.getEndTime(), mountPresent.getMonth());
				} else {
					deadline = DateUtils.AddMonth(date, mountPresent.getMonth());
				}
				mountStore.setEndTime(deadline);
				mountStorePriDao.save(mountStore);
			} else {
				deadline = DateUtils.AddMonth(date, mountPresent.getMonth());
				MountStore newMountstore = new MountStore();
				newMountstore.setId(StringUtils.createUUID());
				newMountstore.setAccId(accId);
				newMountstore.setMountsId(mountPresent.getMountId());
				newMountstore.setStartTime(date);
				newMountstore.setEndTime(deadline);
				mountStorePriDao.save(newMountstore);
			}
		} catch (ParseException e) {
			logger.error("", e);
		}

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId("");
		record.setAmount(0);
		record.setClientIp("");
		record.setDate(DateUtils.getNowTime());
		record.setDeadline(deadline);
		record.setDescribe("首充赠送坐骑");
		record.setGoodsId(String.valueOf(mountPresent.getMountId()));
		record.setGoodsType(GoodsType.BUY_MOUNT.getValue());
		record.setGoodsNumber(1);

		recordConsumptionPriDao.save(record);
		return true;
	}

	public boolean presentMountForOpenGuard(String accId, int level, String cId, long deadline) {
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(TYPE_OPENGUARD, level);
		if (mountPresent == null) {
			mountPresent = getDefaultMount(TYPE_OPENGUARD, level);
		}
		if (mountPresent == null) {
			logger.error("开通守护赠送坐骑失败, 没有赠送配置");
			return false;
		}

		HotChannelMount mount = hotChannelMountDao.findOne(mountPresent.getMountId());
		if (mount == null) {
			logger.error("开通守护赠送坐骑失败, 没有坐骑配置，坐骑 id=" + mountPresent.getMountId());
			return false;
		}

		long date = 0L;
		MountStore mountStore = mountStorePriDao.findByAccIdAndMountsIdAndCId(accId, mountPresent.getMountId(), cId);

		try {
			date = DateUtils.getNowDate();
			if (mountStore != null) {
				mountStore.setEndTime(deadline);
				mountStorePriDao.save(mountStore);
			} else {
				deadline = DateUtils.AddMonth(date, mountPresent.getMonth());
				MountStore newMountstore = new MountStore();
				newMountstore.setId(StringUtils.createUUID());
				newMountstore.setAccId(accId);
				newMountstore.setMountsId(mountPresent.getMountId());
				newMountstore.setStartTime(date);
				newMountstore.setEndTime(deadline);
				newMountstore.setcId(cId);
				mountStorePriDao.save(newMountstore);

			}
		} catch (ParseException e) {
			logger.error("", e);
		}

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId("");
		record.setAmount(0);
		record.setClientIp("");
		record.setDate(DateUtils.getNowTime());
		record.setDeadline(deadline);
		record.setDescribe("购买守护赠送坐骑");
		record.setGoodsId(String.valueOf(mountPresent.getMountId()));
		record.setGoodsType(GoodsType.BUY_MOUNT.getValue());
		record.setGoodsNumber(1);

		recordConsumptionPriDao.save(record);
		return true;
	}

	public boolean presentMountForBuyVip(String accId, int level, long deadline) {
		MountPresent mountPresent = mountPresentPriDao.findByTypeAndLevel(TYPE_BUYVIP, level);
		if (mountPresent == null) {
			mountPresent = getDefaultMount(TYPE_BUYVIP, level);
		}
		if (mountPresent == null) {
			logger.error("开通VIP赠送坐骑失败, 没有赠送配置");
			return false;
		}
		HotChannelMount mount = hotChannelMountDao.findOne(mountPresent.getMountId());
		if (mount == null) {
			logger.error("购买vip赠送坐骑失败, 没有坐骑配置，坐骑 id=" + mountPresent.getMountId());
			return false;
		}

		long date = 0L;
		MountStore mountStore = mountStorePriDao.findByAccIdAndMountsId(accId, mountPresent.getMountId());

		try {
			date = DateUtils.getNowDate();
			if (mountStore != null) {
				mountStore.setEndTime(deadline);
				mountStorePriDao.save(mountStore);
			} else {
				deadline = DateUtils.AddMonth(date, mountPresent.getMonth());
				MountStore newMountstore = new MountStore();
				newMountstore.setId(StringUtils.createUUID());
				newMountstore.setAccId(accId);
				newMountstore.setMountsId(mountPresent.getMountId());
				newMountstore.setStartTime(date);
				newMountstore.setEndTime(deadline);
				mountStorePriDao.save(newMountstore);
			}
		} catch (ParseException e) {
			logger.error("", e);
		}

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId("");
		record.setAmount(0);
		record.setClientIp("");
		record.setDate(DateUtils.getNowTime());
		record.setDeadline(deadline);
		record.setDescribe("购买vip赠送坐骑");
		record.setGoodsId(String.valueOf(mountPresent.getMountId()));
		record.setGoodsType(GoodsType.BUY_MOUNT.getValue());
		record.setGoodsNumber(1);

		recordConsumptionPriDao.save(record);
		return true;
	}
}
