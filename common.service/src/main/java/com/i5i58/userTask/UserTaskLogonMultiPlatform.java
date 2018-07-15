package com.i5i58.userTask;

import java.text.ParseException;

import com.i5i58.data.account.UserTaskRecord;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;

public class UserTaskLogonMultiPlatform extends UserTaskBase implements Runnable {
	String accId;
	int device;

	UserTaskLogonMultiPlatform(AccountPropertyPriDao accountPropertyPriDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao, String accId, int device) {
		super(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao);
		this.accId = accId;
		this.device = device;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TaskConfig.TaskType taskType;
		if (device == DeviceCode.PCLive) {
			taskType = TaskConfig.TaskType.TFirstTimeLogonPC;
		} else if (device == DeviceCode.AndroidLive || device == DeviceCode.IOSLive) {
			taskType = TaskConfig.TaskType.TFirstTimeLogonMobile;
		} else if (device == DeviceCode.WEBLive) {
			taskType = TaskConfig.TaskType.TFirstTimeLogonWeb;
		} else {
			return;
		}
		String id = TaskUtil.constructUserTaskRecordId(accId, 0, taskType.getTaskType(), false);
		UserTaskRecord taskRecord = userTaskRecordPriDao.findOne(id);
		if (taskRecord != null) {
			return;
		}
		long nowdate;
		try {
			nowdate = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return;
		}
		long curScore = taskType.getTaskScore();
		taskRecord = new UserTaskRecord();
		taskRecord.setId(id);
		taskRecord.setAccId(accId);
		taskRecord.setTaskType(taskType.getTaskType());
		taskRecord.setCompleteDate(nowdate);
		taskRecord.setScore(curScore);
		userTaskRecordPriDao.save(taskRecord);

		addUserScore(accId, curScore);
	}

}
