package com.i5i58.apis.home;

import java.io.IOException;

import com.i5i58.apis.constants.ResultDataSet;

/**
 * 轮播图服务接口
 * @author frank
 *
 */
public interface ICarousel {
	/**
	 * 获取首页轮播图
	 * @author frank
	 * @param device{0:全部，1:pc，2:web，3:手机}
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getCarousel(String device) throws IOException;
	/**
	 * 获取richscore 排行 
	 * @author cw
	 * @return
	 * @throws IOException
	 */
	ResultDataSet getRichScoreRanking() throws IOException;	
}
