package com.i5i58.userTask;

import java.text.ParseException;

import com.i5i58.data.account.UserTaskRecord;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.util.DateUtils;

/**
 * @author songfl 只执行一次的任务
 */
public class SingleTask extends UserTaskBase implements Runnable {
	String accId;
	TaskConfig.TaskType taskType;

	public SingleTask(AccountPropertyPriDao accountPropertyDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao, String accId, TaskConfig.TaskType taskType) {
		super(accountPropertyDao, accountPriDao, userTaskRecordPriDao);
		// TODO Auto-generated constructor stub
		this.accId = accId;
		this.taskType = taskType;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String id = TaskUtil.constructUserTaskRecordId(accId, 0, taskType.getTaskType(), false);
		UserTaskRecord taskRecord = userTaskRecordPriDao.findOne(id);
		if (taskRecord != null) {//
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
		taskRecord.setCompleteDate(nowdate);
		taskRecord.setScore(curScore);
		taskRecord.setTaskType(taskType.getTaskType());
		userTaskRecordPriDao.save(taskRecord);
		addUserScore(accId, curScore);
	}

}
