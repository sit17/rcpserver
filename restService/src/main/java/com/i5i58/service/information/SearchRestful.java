package com.i5i58.service.information;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.information.ISearch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "信息-查询")
@RestController
@RequestMapping("${rpc.controller.version}")
public class SearchRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private ISearch search;

	@ApiOperation(value = "模糊查询账户", notes = "")
	@RequestMapping(value = "/info/queryAccount", method = RequestMethod.GET)
	public ResultDataSet queryAccount(@ApiParam(value = "模糊匹配字符") @RequestParam(value = "param") String param,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryAccount(param, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "模糊查询频道", notes = "")
	@RequestMapping(value = "/info/queryChannel", method = RequestMethod.GET)
	public ResultDataSet queryChannel(@ApiParam(value = "模糊匹配字符") @RequestParam(value = "param") String param,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryChannel(param, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "模糊查询主播", notes = "")
	@RequestMapping(value = "/info/queryAnchor", method = RequestMethod.GET)
	public ResultDataSet queryAnchor(@ApiParam(value = "模糊匹配字符") @RequestParam(value = "param") String param,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryAnchor(param, pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "查询新秀主播", notes = "")
	@RequestMapping(value = "/info/queryNewAnchor", method = RequestMethod.GET)
	public ResultDataSet queryNewAnchor(@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryNewAnchor(pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "查询大家都在看主播", notes = "")
	@RequestMapping(value = "/info/queryLotWatchAnchor", method = RequestMethod.GET)
	public ResultDataSet queryLotWatchAnchor(@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryLotWatchAnchor(pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "查询新秀/大家都在看主播", notes = "")
	@RequestMapping(value = "/info/queryNewLotWatchAnchor", method = RequestMethod.GET)
	public ResultDataSet queryNewLotWatchAnchor(@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = search.queryNewLotWatchAnchor(pageNum, pageSize);
		return rds;
	}
}
