package com.i5i58.userTask;

import java.text.ParseException;

import org.apache.log4j.Logger;

import com.i5i58.data.account.UserTaskRecord;
import com.i5i58.data.account.UserWatchRecord;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.primary.dao.account.UserWatchRecordPriDao;
import com.i5i58.util.DateUtils;

public class UserTaskExitChannel extends UserTaskBase implements Runnable {
	private Logger logger = Logger.getLogger(getClass());
	private UserWatchRecordPriDao userWatchRecordPriDao;
	String accId;
	String cId;
	long curTime; // 毫秒数，当前时间

	UserTaskExitChannel(AccountPropertyPriDao accountPropertyPriDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao, UserWatchRecordPriDao userWatchRecordPriDao, String accId,
			String cId, long curTime) {
		super(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao);
		this.userWatchRecordPriDao = userWatchRecordPriDao;
		this.accId = accId;
		this.cId = cId;
		this.curTime = curTime;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		UserWatchRecord record = userWatchRecordPriDao.findOne(accId);
		if (record == null) {
			return;
		}
		long duration = curTime - record.getStartTime();
		if (duration <= 0)
			return;
		duration = duration + record.getDurationFree();
		long freeMilliSecond = duration % (DateUtils.dayMilliSecond);
		long watchedHours = duration / (DateUtils.dayMilliSecond);
		record.setFinsheTime(curTime);
		record.setDurationFree(freeMilliSecond);
		userWatchRecordPriDao.save(record);

		if (watchedHours <= 0)
			return;
		calcScoreOnWatchedHours(watchedHours);
	}

	/**
	 * 根据观看时长计算积分
	 */
	private void calcScoreOnWatchedHours(long watchedHours) {
		if (watchedHours < 0)
			return;
		long nowdate;
		try {
			nowdate = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			return;
		}
		// 执行退出时的任务
		TaskConfig.TaskType taskType = TaskConfig.TaskType.TWatchChannelEveryHour;
		long curScore = watchedHours * taskType.getTaskScore();
		String id = TaskUtil.constructUserTaskRecordId(accId, 0, taskType.getTaskType(), false);
		UserTaskRecord taskRecord = userTaskRecordPriDao.findOne(id);
		if (taskRecord == null) {
			taskRecord = new UserTaskRecord();
			taskRecord.setId(id);
			taskRecord.setAccId(accId);
			taskRecord.setTaskType(taskType.getTaskType());
		}
		taskRecord.setCompleteDate(nowdate);
		taskRecord.setScore(taskRecord.getScore() + curScore);
		userTaskRecordPriDao.save(taskRecord);

		addUserScore(accId, curScore);
	}
}
