package com.i5i58.userTask;

import java.text.ParseException;

import com.i5i58.data.account.UserTaskRecord;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.util.DateUtils;

public class UserTaskShareChannel extends UserTaskBase implements Runnable {
	private String accId;

	public UserTaskShareChannel(AccountPropertyPriDao accountPropertyPriDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao, String accId) {
		super(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao);
		// TODO Auto-generated constructor stub
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
		TaskConfig.TaskType taskType = TaskConfig.TaskType.TShareChannelLink;
		String id = TaskUtil.constructUserTaskRecordId(accId, nowdate, taskType.getTaskType(), true);
		UserTaskRecord taskRecord = userTaskRecordPriDao.findOne(id);
		if (taskRecord != null) { // 当天有任务记录
			if (taskRecord.getScore() >= taskType.getMaxScore()) {// 积分超了
				return;
			}
		} else {
			taskRecord = new UserTaskRecord();
			taskRecord.setId(id);
			taskRecord.setAccId(accId);
			taskRecord.setTaskType(taskType.getTaskType());
		}
		long curScore = taskType.getTaskScore();
		taskRecord.setCompleteDate(nowdate);
		taskRecord.setScore(taskRecord.getScore() + curScore);
		userTaskRecordPriDao.save(taskRecord);
		addUserScore(accId, curScore);
	}

}
