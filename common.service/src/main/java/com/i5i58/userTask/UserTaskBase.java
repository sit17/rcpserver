package com.i5i58.userTask;

import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;

public class UserTaskBase {
	protected AccountPropertyPriDao accountPropertyPriDao;
	protected AccountPriDao accountPriDao;
	protected UserTaskRecordPriDao userTaskRecordPriDao;

	UserTaskBase(AccountPropertyPriDao accountPropertyPriDao, AccountPriDao accountPriDao,
			UserTaskRecordPriDao userTaskRecordPriDao) {
		this.accountPropertyPriDao = accountPropertyPriDao;
		this.accountPriDao = accountPriDao;
		this.userTaskRecordPriDao = userTaskRecordPriDao;
	}

	public void addUserScore(String accId, long score) {
		/**
		 * y ：积分 x : 等级 y=4x*x+8x 等级最高100，达到最高等级后，星级加1，积分归零
		 */
		//

		AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
		if (accountProperty == null) {
			return;
		}
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			return;
		}
		long curScore = accountProperty.getScore() + score;
		accountProperty.setScore(curScore);
		accountPropertyPriDao.save(accountProperty);
	}
}
