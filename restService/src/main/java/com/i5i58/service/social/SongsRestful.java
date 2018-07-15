package com.i5i58.service.social;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.ISongs;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author Lee
 *
 */
@Api(value = "社交-神曲服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class SongsRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private ISongs songs;

	@ApiOperation(value = "分类获取神曲分页列表", notes = "")
	@RequestMapping(value = "/social/getSongs", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getSongs(@RequestParam(value = "type") String type,
									@RequestParam(value = "pageSize") int pageSize,
									@RequestParam(value = "pageNum") int pageNum,
									@RequestParam(value = "sortBy") String sortBy,
									@RequestParam(value = "sortDirection") String sortDirection
										) {
		ResultDataSet rds = new ResultDataSet();
		rds = songs.getSongs(HttpUtils.getAccIdFromHeader(),type,pageSize, pageNum, sortBy,sortDirection);
		return rds;
	}
	
	
	@ApiOperation(value = "上传神曲", notes = "")
	@RequestMapping(value = "/social/upLoadSongs", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet upLoadSongs(
			@RequestParam(value = "accId") String accId,
			@RequestParam(value = "url") String url
			) {
		ResultDataSet rds = new ResultDataSet();
		rds = songs.upLoadSongs(HttpUtils.getAccIdFromHeader(),url);
		return rds;
	}
}
