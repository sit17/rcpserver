package com.i5i58.userTask;

import com.i5i58.data.account.UserWatchRecord;
import com.i5i58.primary.dao.account.UserWatchRecordPriDao;

public class UserTaskEnterChannel implements Runnable {
	
	private UserWatchRecordPriDao userWatchRecordPriDao;
	String accId;
	String cId;
	long curTime; //毫秒数，当前时间

	UserTaskEnterChannel(UserWatchRecordPriDao userWatchRecordPriDao, String accId, String cId, long curTime){
		this.userWatchRecordPriDao = userWatchRecordPriDao;
		this.accId = accId;
		this.cId = cId;
		this.curTime = curTime;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		UserWatchRecord record = userWatchRecordPriDao.findOne(accId);
		if (record == null){
			record = new UserWatchRecord();
			record.setAccId(accId);
			record.setStartTime(curTime);
		}
		record.setStartTime(curTime);
		userWatchRecordPriDao.save(record);
	}

}
