package com.i5i58.service.home;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.PageParams;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.home.ICarousel;
import com.i5i58.apis.home.IHotChannelOperate;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Web Restful Service for Carousel Figures
 * 
 * @author frank
 *
 */
@Api(value = "首页-首页服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class HomePageRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private ICarousel carousel;

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IHotChannelOperate hotChannelOperate;

	/**
	 * Define Restful service
	 * 
	 * @author frank
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "获取首页轮播图", notes = "{0:全部，1:pc，2:web，3:手机}")
	@RequestMapping(value = "/home/getCarousel", method = RequestMethod.GET)
	public ResultDataSet getCarousel(@RequestParam(value = "device") String device) {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		// Valid Type Check
		try {
			rds = carousel.getCarousel(device);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}

	/**
	 * Define Restful service
	 * 
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "分页获取热播频道", notes = "只在缓存中")
	@RequestMapping(value = "/home/getHotChannel", method = RequestMethod.GET)
	public ResultDataSet getHotChannels(@RequestParam(value = "type") int type,
			@RequestParam(value = "pageSize", defaultValue = PageParams.PAGE_SIZE) int pageSize,
			@RequestParam(value = "pageNum", defaultValue = PageParams.PAGE_NUM) int pageNum) {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		try {
			String key = String.format("%d_%d_%d", type, pageSize, pageNum);
			// call service
			rds = hotChannelOperate.getHotChannels(type, pageSize, pageNum, key);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}

	@ApiOperation(value = "获取richscore排行", notes = "")
	@RequestMapping(value = "/home/getRichScoreRanking", method = RequestMethod.GET)
	public ResultDataSet getRichScoreRanking() {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		try {
			// call service
			rds = carousel.getRichScoreRanking();
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}
}
