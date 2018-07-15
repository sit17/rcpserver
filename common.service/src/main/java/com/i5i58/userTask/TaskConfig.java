package com.i5i58.userTask;
/**
 * @author songfl
 * 平台任务配置
 */
public class TaskConfig {

//	public TaskConfig.TaskType getTaskTypeByType(int taskType) {
//		// TODO Auto-generated constructor stub
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//		
//		if (TaskType.TWatchChannelEveryHour.getTaskType() == taskType)
//			return TaskType.TWatchChannelEveryHour;
//		
//		if (TaskType.TFirstTimeSetFaceImage.getTaskType() == taskType)
//			return TaskType.TFirstTimeSetFaceImage;
//		
//		if (TaskType.TFirstTimeWatchChannel.getTaskType() == taskType)
//			return TaskType.TFirstTimeWatchChannel;
//		
//		if (TaskType.TFirstTimeFollowAnchor.getTaskType() == taskType)
//			return TaskType.TFirstTimeFollowAnchor;
//		
//		if (TaskType.TFirstTimeUpdateStatus.getTaskType() == taskType)
//			return TaskType.TFirstTimeUpdateStatus;
//		
//		if (TaskType.TFirstTimeUploadScreenshot.getTaskType() == taskType)
//			return TaskType.TFirstTimeUploadScreenshot;
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//		if (TaskType.TLogonEveryDay.getTaskType() == taskType)
//			return TaskType.TLogonEveryDay;
//	}
	
	public enum TaskType{		
		TLogonEveryDay(1, 10, 10, false),//每日首次登录，获得10经验
		TWatchChannelEveryHour(2, 10, 0, false),//每观看一小时，获得10经验
		
		/**
		 * TFirstTime*表示仅在第一次完成的时候会获得积分，无法重复获得; 每完成一项，获得100积分;
		 * */
		TFirstTimeSetFaceImage(3, 100, 100, true),
		TFirstTimeWatchChannel(4, 100, 100, true),
		TFirstTimeFollowAnchor(5, 100, 100, true),
		TFirstTimeUpdateStatus(6, 100, 100, true),
		TFirstTimeUploadScreenshot(7, 100, 100, true),
		TFirstTimeUploadShortFilm(8, 100, 100, true),
		TFirstTimeLeaveMessage(9, 100, 100, true),
		
		/**
		 * 转发直播间链接,每转发1个获得100积分，通过转发获得积分的每日上限是1000积分
		 * */
		TShareChannelLink(10, 100, 1000, false),
		/**
		 * 分别使用手机端，PC端和web端三个端口登陆过后，获得1000积分
		 * */
		TFirstTimeLogonMobile(11, 1000, 1000, true),
		TFirstTimeLogonPC(12, 1000, 1000, true),
		TFirstTimeLogonWeb(13, 1000, 1000, true),
		
		/**
		 * 每添加一名好友，获得10积分，上限是1000
		 * */
		TAddFriend(14, 10, 1000, false);
		
		
		int taskType;	//任务类型
		long taskScore; //完成一次任务可获得的积分
		long maxScore;  //每天最多累计的积分上限,0表示没有上限
		boolean isOnlyFirstTime; //任务只执行一次
		
		TaskType(int taskType, long taskScore, long maxScore, boolean isOnlyFirstTime){
			this.taskType = taskType;
			this.taskScore = taskScore;
			this.maxScore = maxScore;
			this.isOnlyFirstTime = isOnlyFirstTime;
		}
		
		public int getTaskType() {
			return taskType;
		}

		public void setTaskType(int taskType) {
			this.taskType = taskType;
		}

		public long getTaskScore() {
			return taskScore;
		}

		public void setTaskScore(long taskScore) {
			this.taskScore = taskScore;
		}

		public long getMaxScore() {
			return maxScore;
		}

		public void setMaxScore(long maxScore) {
			this.maxScore = maxScore;
		}

		public boolean isOnlyFirstTime() {
			return isOnlyFirstTime;
		}

		public void setOnlyFirstTime(boolean isOnlyFirstTime) {
			this.isOnlyFirstTime = isOnlyFirstTime;
		}
		
	}
}

