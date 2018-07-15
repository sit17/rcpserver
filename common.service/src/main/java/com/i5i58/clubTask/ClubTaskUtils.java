package com.i5i58.clubTask;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelWatchingRecord;
import com.i5i58.data.channel.ClubTaskRecord;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotClubTaskConfig;
import com.i5i58.data.channel.HotClubTaskRecord;
import com.i5i58.data.channel.HotClubTaskScoreRecord;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelWatchingRecordPriDao;
import com.i5i58.primary.dao.channel.ClubTaskRecordPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotClubTaskConfigDao;
import com.i5i58.redis.all.HotClubTaskRecordDao;
import com.i5i58.redis.all.HotClubTaskScoreRecordDao;
import com.i5i58.util.DateUtils;

@Component
public class ClubTaskUtils {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	HotChannelDao hotChannelDao;
	
	@Autowired
	ChannelPriDao channelPriDao;
	
	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;
	
	@Autowired
	ClubTaskRecordPriDao clubTaskRecordPriDao;
	
	@Autowired
	HotClubTaskRecordDao hotClubTaskRecordDao;
	
	@Autowired
	HotClubTaskScoreRecordDao hotClubTaskScoreRecordDao;
	
	@Autowired
	HotClubTaskConfigDao hotClubTaskConfigDao;
	
	@Autowired
	ChannelWatchingRecordPriDao channelWatchingRecordPriDao;
	
	public enum TaskType{
		DailyClock(1, 2, "每天给主播打卡且观看5分钟"),
		GiftGiven(2, 20, "每天首次送礼物"),
		SocialShare(3, 5, "每周分享主播直播间");
		
		private TaskType(int taskId, long taskScore, String description){
			this.taskId = taskId;
			this.taskScore = taskScore;
			this.description = description;
		}
		
		private int taskId;
		private long taskScore;
		private String description;
		
		public int getTaskId() {
			return taskId;
		}
		public void setTaskId(int taskId) {
			this.taskId = taskId;
		}
		public long getTaskScore() {
			return taskScore;
		}
		public void setTaskScore(long taskScore) {
			this.taskScore = taskScore;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	}
	
	public int getTaskMultipile(){
		int mul = 1;
		long today;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			logger.error("", e);
			return mul;
		}
		
		if (DateUtils.isWeekend(today)){
			mul *= 2;
		}
		long dayToNextMonth = DateUtils.getDayToNextMonth(today);
		if (dayToNextMonth >= 1 && dayToNextMonth <= 3){
			mul *= 2;
		}
		return mul;
	}
	
	public long getTodayTaskScore(String accId, String cId){
		HotClubTaskScoreRecord scoreRecord = hotClubTaskScoreRecordDao.findOne(cId + "_" + accId);
		if (scoreRecord == null){
			return 0L;
		}else{
			return scoreRecord.getTaskScore();
		}
	}
	
	public void recordTask(String accId, String cId, int taskId, long score, long datetime){
		HotClubTaskRecord hotRecord  = new HotClubTaskRecord();
		hotRecord.setId(cId + "_" + accId + "_" + taskId);
		hotRecord.setAccId(accId);
		hotRecord.setcId(cId);
		hotRecord.setTaskId(taskId);
		hotRecord.setScore(score);
		hotRecord.setCompleteDate(datetime);
		hotClubTaskRecordDao.save(hotRecord);
		
		ClubTaskRecord taskRecord = new ClubTaskRecord();
		taskRecord.setAccId(accId);
		taskRecord.setcId(cId);
		taskRecord.setScore(score);
		taskRecord.setTaskId(taskId);
		taskRecord.setCompleteDate(datetime);
		clubTaskRecordPriDao.save(taskRecord);
		
		long totalScore = 0L;
		HotClubTaskScoreRecord scoreRecord = hotClubTaskScoreRecordDao.findOne(cId + "_" + accId);
		if (scoreRecord == null){
			scoreRecord = new HotClubTaskScoreRecord();
			scoreRecord.setId(cId + "_" + accId);
			scoreRecord.setAccId(accId);
			scoreRecord.setcId(cId);
		}else{
			totalScore = scoreRecord.getTaskScore();
		}
		scoreRecord.setTaskScore(totalScore + score);
		hotClubTaskScoreRecordDao.save(scoreRecord);
	}
	
	/**
	 * @author songfl
	 * 每日打卡任务
	 * */
	public int performDailyClockTask(String accId, String cId){
		try {
			long today = DateUtils.getNowDate();
			int taskId = TaskType.DailyClock.getTaskId();
			HotClubTaskRecord hotRecord = hotClubTaskRecordDao.findOne(cId + "_" + accId + "_" + taskId);
			
			//每天一次
			if (hotRecord  != null){
				if (hotRecord.getCompleteDate() == today){
					return -1;
				}
				hotClubTaskRecordDao.delete(hotRecord);
				hotRecord = null;
			}
			
			ChannelWatchingRecord todayWatchingTime = channelWatchingRecordPriDao.findOne(cId + "_" + accId);
			if (todayWatchingTime == null){
				return -2; //今天没有观看记录
			}
			long preWatchTime = todayWatchingTime.getFinishTime();
			if (DateUtils.getDayInterval(preWatchTime, today) != 0){
				return -2; //今天没有观看记录
			}
			
			if (todayWatchingTime.getDuration() < 5 * 60 * 1000){
				return -3; //观看时间不够
			}
			
			HotClubTaskConfig taskConfig = hotClubTaskConfigDao.findOne(TaskType.DailyClock.getTaskId());
			long baseScore = TaskType.DailyClock.getTaskScore();
			if (taskConfig != null){
				baseScore = taskConfig.getTaskScore();
			}
			int mul = getTaskMultipile();
			long score = baseScore * mul;
			recordTask(accId, cId, taskId, score, today);
		} catch (ParseException e) {
			logger.error("", e);
			return -4;
		}
		
		return 0;
	}
	
	/**
	 * @author songfl
	 * 赠送礼物任务
	 * */
	public int performGiftGivenTask(String accId, String cId) {
		try {
			long today = DateUtils.getNowDate();
			int taskId = TaskType.GiftGiven.getTaskId();
			HotClubTaskRecord hotRecord = hotClubTaskRecordDao.findOne(cId + "_" 
					+ accId + "_" + taskId);
			//每天一次
			if (hotRecord  != null){
				if (DateUtils.getDayInterval(hotRecord.getCompleteDate(), today) <= 0){
					return -1;
				}
				hotClubTaskRecordDao.delete(hotRecord);
				hotRecord = null;
			}
			HotClubTaskConfig taskConfig = hotClubTaskConfigDao.findOne(TaskType.GiftGiven.getTaskId());
			long baseScore = TaskType.GiftGiven.getTaskScore();
			if (taskConfig != null){
				baseScore = taskConfig.getTaskScore();
			}
			int mul = getTaskMultipile();
			long score = baseScore * mul;
			recordTask(accId, cId, taskId, score, today);
		} catch (ParseException e) {
			logger.error("", e);
			return -1;
		}	
		return 0;
	}
	
	/**
	 * @author songfl
	 * 分享任务
	 * */
	public int performSocialShareTask(String accId, String cId){
		try {
			long today = DateUtils.getNowDate();
			int taskId = TaskType.SocialShare.getTaskId();
			HotClubTaskRecord hotRecord = hotClubTaskRecordDao.findOne(cId + "_" + accId + "_" + taskId);
			//每周一次
			if (hotRecord  != null){
				if (DateUtils.isSameWeek(hotRecord.getCompleteDate(), today)){
					return -1;
				}
				hotClubTaskRecordDao.delete(hotRecord);
				hotRecord = null;
			}
			HotClubTaskConfig taskConfig = hotClubTaskConfigDao.findOne(TaskType.SocialShare.getTaskId());
			long baseScore = TaskType.SocialShare.getTaskScore();
			if (taskConfig != null){
				baseScore = taskConfig.getTaskScore();
			}
			int mul = getTaskMultipile();
			long score = baseScore * mul;
			recordTask(accId, cId, taskId, score, today);
		
		} catch (ParseException e) {
			logger.error("", e);
			return -1;
		}	
		return 0;
	}
	
	
	/**
	 * @author songfl
	 * 清除上一天的任务记录，任务积分记录到用户的粉丝团积分中，更新粉丝团积分
	 * */
	public void freshClubUserTaskScore(){
		long nowDateTime = 0L;
		try {
			nowDateTime = DateUtils.getNowDate();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//统计之前10天（不包括今天）的积分，作为用户的有效积分;
		long firstDateTime = DateUtils.addDay(nowDateTime, -11);
		long lastDateTime = DateUtils.addDay(nowDateTime, -1);
		
		Map<String, Long> channelScore = new HashMap<String, Long>();
		List<HotClubTaskScoreRecord> scoreRecords = (List<HotClubTaskScoreRecord>) hotClubTaskScoreRecordDao.findAll();
		for(HotClubTaskScoreRecord sr : scoreRecords){
			Long chScore = channelScore.getOrDefault(sr.getcId(), 0L);
			chScore += sr.getTaskScore();
			channelScore.put(sr.getcId(), chScore);
			//个人积分增长
			ChannelFansClub fansClub = channelFansClubPriDao.findByCIdAndAccIdAndEndDateGreaterThan(
					sr.getcId(), sr.getAccId(), nowDateTime);
			if (fansClub == null)
				continue;
			
//			long personalScore = fansClub.getPersonalScore() + sr.getTaskScore();
//			if (personalScore > ClubLevelUtils.MaxUserScore){
//				personalScore = ClubLevelUtils.MaxUserScore;
//			}
//			fansClub.setPersonalScore(personalScore);	

			//统计之前10天（不包括今天）的积分，作为用户的有效积分;
			List<ClubTaskRecord> taskRecords = 
					clubTaskRecordPriDao.findByCIdAndAccIdAndCompleteDateGreaterThanAndCompleteDateLessThan(sr.getcId(), 
					sr.getAccId(), firstDateTime, lastDateTime);
			if(taskRecords == null)
			{
				fansClub.setPersonalScore(0L);
				continue;
			}
			long myScore = 0L;
			for(ClubTaskRecord tr : taskRecords){
				myScore += tr.getScore();
			}
			fansClub.setPersonalScore(myScore);
			channelFansClubPriDao.save(fansClub);
		}
		
		//统计频道积分
		for (Map.Entry<String, Long> entry : channelScore.entrySet())
		{
			Channel channel = channelPriDao.findByCId(entry.getKey());
			if (channel == null)
				continue;
			long clubScore = channel.getClubScore() + entry.getValue().longValue();
			if (clubScore > ClubLevelUtils.ClubScore.LEVELSCORE6.getMaxScore()){
				clubScore = ClubLevelUtils.ClubScore.LEVELSCORE6.getMaxScore();
			}
			channel.setClubScore(clubScore);
			channelPriDao.save(channel);
		}
		hotClubTaskScoreRecordDao.deleteAll();
	}
	/**
	 * @author songfl
	 * 刷新粉丝团等级
	 * */
	public void freshFansClubLevel(){
		List<Channel> channels = (List<Channel>)channelPriDao.findAll();
		for(Channel ch : channels){
			long newScore = ch.getClubScore();
			int newLevel = ClubLevelUtils.getClubLevel(newScore);
			String title = ClubLevelUtils.getClubTitleByLevel(newLevel);
			ch.setClubScore(newScore);
			ch.setClubLevel(newLevel);
			ch.setClubTitle(title);
			HotChannel hotChannel = hotChannelDao.findOne(ch.getcId());
			if (hotChannel != null){
				hotChannel.setClubScore(ch.getClubScore());
				hotChannel.setClubLevel(ch.getClubLevel());
				hotChannel.setClubTitle(ch.getClubTitle());
				hotChannelDao.save(hotChannel);
			}
		}
		channelPriDao.save(channels);
	}
	
	
	/**
	 * @author songfl
	 * 刷新粉丝团人数
	 */
	public void freshFansClubMemberCount(){
		List<Channel> channels = (List<Channel>)channelPriDao.findAll();
		long nowDate = 0;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Channel ch : channels){
			int count = channelFansClubPriDao.findCountByCIdAndEndDate(ch.getcId(), nowDate);
			ch.setClubMemberCount(count);
			HotChannel hotChannel = hotChannelDao.findOne(ch.getcId());
			if (hotChannel != null){
				hotChannel.setClubMemberCount(ch.getClubMemberCount());
				hotChannelDao.save(hotChannel);
			}
		}
		channelPriDao.save(channels);
	}
}
