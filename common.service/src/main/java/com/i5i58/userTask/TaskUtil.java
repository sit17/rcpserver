package com.i5i58.userTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.UserTaskRecordPriDao;
import com.i5i58.primary.dao.account.UserWatchRecordPriDao;
import com.i5i58.util.DateUtils;

@Component
public class TaskUtil {

	@Autowired
	private UserTaskRecordPriDao userTaskRecordPriDao;

	@Autowired
	private AccountPriDao accountPriDao;

	@Autowired
	private AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	private UserWatchRecordPriDao userWatchRecordPriDao;

	private ExecutorService taskThreadPool = Executors.newFixedThreadPool(1);

	// public static ExecutorService getTaskThreadPool(){
	// return taskThreadPool;
	// }

	/**
	 * 获取响应等级的最高积分
	 * 
	 * @param level
	 * @return
	 */
	public static long getMaxScoreByLevel(int level) {
		long score = 4 * level * level + 8 * level;
		return score;
	}

	/**
	 * 由等级的最高积分，计算等级数
	 * 
	 * @param maxScore
	 * @return
	 */
	public static long getLevelByMaxScore(long maxScore) {
		double sqrttmp = Math.sqrt((maxScore + 4));
		long level = ((long) sqrttmp) / 2 - 1;
		return level;
	}

	/**
	 * 构造任务记录表的主键
	 */
	public static String constructUserTaskRecordId(String accId, long nowdate, int taskType, boolean isDateRelated) {
		String id;
		if (isDateRelated) {
			id = accId + "_" + taskType + "_" + nowdate;
		} else {
			id = accId + "_" + taskType;
		}
		return id;
	}

	/**
	 * @author songfl
	 * @describe 登录时触发任务
	 * @param accId
	 * @param device
	 */
	public void performTaskOnLogon(String accId, int device) {
		UserTaskLogonEveryDay taskLogonEveryDay = new UserTaskLogonEveryDay(accountPropertyPriDao, accountPriDao,
				userTaskRecordPriDao, accId);
		taskThreadPool.execute(taskLogonEveryDay);

		UserTaskLogonMultiPlatform taskLogonMultiPlatform = new UserTaskLogonMultiPlatform(accountPropertyPriDao,
				accountPriDao, userTaskRecordPriDao, accId, device);
		taskThreadPool.execute(taskLogonMultiPlatform);
	}

	/**
	 * @author songfl
	 * @describe 观看直播开始时触发任务
	 * @param accId
	 * @param cId
	 * @param milliSecondWatched
	 */
	public void performTaskOnEnterChannel(String accId, String cId) {
		long curTime = DateUtils.getNowTime();
		UserTaskEnterChannel taskEnterChannel = new UserTaskEnterChannel(userWatchRecordPriDao, accId, cId, curTime);
		taskThreadPool.execute(taskEnterChannel);
	}

	/**
	 * @author songfl
	 * @describe 观看直播结束时触发任务
	 * @param accId
	 * @param cId
	 * @param milliSecondWatched
	 */
	public void performTaskOnExitChannel(String accId, String cId) {
		long curTime = DateUtils.getNowTime();
		UserTaskExitChannel taskExitChannel = new UserTaskExitChannel(accountPropertyPriDao, accountPriDao,
				userTaskRecordPriDao, userWatchRecordPriDao, accId, cId, curTime);
		taskThreadPool.execute(taskExitChannel);
	}

	/**
	 * @author songfl
	 * @describe 设置头像时触发任务
	 * @param accId
	 */
	public void performTaskOnSetFaceImage(String accId) {
		SingleTask task = new SingleTask(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao, accId,
				TaskConfig.TaskType.TFirstTimeSetFaceImage);
		taskThreadPool.execute(task);
	}

	/**
	 * @author songfl
	 * @describe 观看直播时触发任务
	 * @param accId
	 */
	public void performTaskOnWatchChannel(String accId) {
		SingleTask task = new SingleTask(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao, accId,
				TaskConfig.TaskType.TFirstTimeWatchChannel);
		taskThreadPool.execute(task);
	}

	/**
	 * @author songfl
	 * @describe 关注主播时触发任务
	 * @param accId
	 */
	public void performTaskOnFollowAnchor(String accId) {
		SingleTask task = new SingleTask(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao, accId,
				TaskConfig.TaskType.TFirstTimeFollowAnchor);
		taskThreadPool.execute(task);
	}

	/**
	 * @author songfl
	 * @describe 分享直播间链接时触发任务
	 * @param accId
	 */
	public void performTaskOnShareChannel(String accId) {
		SingleTask task = new SingleTask(accountPropertyPriDao, accountPriDao, userTaskRecordPriDao, accId,
				TaskConfig.TaskType.TFirstTimeFollowAnchor);
		taskThreadPool.execute(task);
	}
}
