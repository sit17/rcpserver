package com.i5i58.service.social;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.IShortFilms;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author Lee
 *
 */
@Api(value = "社交-短拍广场服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ShortFilmsRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IShortFilms shortFilms;

	@ApiOperation(value = "获取短拍广场内容", notes = "")
	@RequestMapping(value = "/social/getShortFilms", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getShortFilms(@RequestParam(value = "pageSize") int pageSize,
										@RequestParam(value = "pageNum") int pageNum,
										@RequestParam(value = "sortBy") String sortBy,
										@RequestParam(value = "sortDirection") String sortDirection
										) {
		ResultDataSet rds = new ResultDataSet();
		rds = shortFilms.getShortFilms(pageSize, pageNum, sortBy,sortDirection,HttpUtils.getAccIdFromHeader());
		return rds;
	}
	
	
	@ApiOperation(value = "上传短拍", notes = "")
	@RequestMapping(value = "/social/upLoadShortFilms", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet upLoadShortFilms(
			@RequestParam(value = "time") String time,
			@RequestParam(value = "url") String url
			) {
		ResultDataSet rds = new ResultDataSet();
		rds = shortFilms.upLoadShortFilms(HttpUtils.getAccIdFromHeader(),url);
		return rds;
	}
}
