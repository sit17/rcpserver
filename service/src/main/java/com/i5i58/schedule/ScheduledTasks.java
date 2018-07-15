package com.i5i58.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.i5i58.clubTask.ClubTaskUtils;
import com.i5i58.redis.all.HotChannelDao;

@Component
public class ScheduledTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	HotChannelDao hotChannelRepo;
	
	@Autowired
	ClubTaskUtils clubTaskUtils;
	
    @Scheduled(fixedRate = 3000000)
    public void reportCurrentTime() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
    }
    
//    /**
//     * 每天6点刷新用户的积分
//     * @author songfl
//     * */
//    @Scheduled(cron = "0 0 6 * * ?")
//    public void freshUserTaskScore(){
//    	clubTaskUtils.freshUserTaskScore();
//    }
//    
//    /**
//     * 每月一号6:00刷新粉丝团等级
//     * @author songfl
//     * */
//    @Scheduled(cron = "0 0 6 1 * ?")
//    public void freshFansClubLevel(){
//    	clubTaskUtils.freshFansClubLevel();
//    }
//    
//    /**
//     * 每天刷新粉丝团
//     * @author songfl
//     * */
//    @Scheduled(cron = "0 0 6 * * ?")
//    public void fansClubDailyFresh(){
//    	clubTaskUtils.fansClubDailyFresh();
//    }
    
//    @Scheduled(fixedRate = 10000)
//    public void testDatebase() {
//    	SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
//        System.out.println("The start time is " + df.format(new Date()));
//        
//        df = new SimpleDateFormat("HH:mm:ss:SSS");
//        System.out.println("The end time is " + df.format(new Date()));
//    }
}