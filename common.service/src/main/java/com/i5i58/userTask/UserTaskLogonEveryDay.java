package com.i5i58.userTask;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.i5i58.data.account.UserTaskRecord;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.util.DateUtils;

public class UserTaskLogonEveryDay extends UserTaskBase implements Runnable {

	String accId;

	UserTaskLogonEveryDay(AccountPropertyPriDao accountPropertyPriDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao, String accId) {
		super(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao);
		this.accId = accId;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long nowdate;
		try {
			nowdate = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return;
		}
		TaskConfig.TaskType taskType = TaskConfig.TaskType.TLogonEveryDay;
		String id = TaskUtil.constructUserTaskRecordId(accId, nowdate, taskType.getTaskType(), true);
		UserTaskRecord taskRecord = userTaskRecordPriDao.findOne(id);
		if (taskRecord != null) { // 当天登录过了
			return;
		}

		long curScore = taskType.getTaskScore();
		taskRecord = new UserTaskRecord();
		taskRecord.setId(id);
		taskRecord.setAccId(accId);
		taskRecord.setCompleteDate(nowdate);
		taskRecord.setTaskType(taskType.getTaskType());
		taskRecord.setScore(curScore);
		userTaskRecordPriDao.save(taskRecord);

		addUserScore(accId, curScore);
	}

}
