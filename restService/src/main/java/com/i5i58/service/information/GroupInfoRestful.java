package com.i5i58.service.information;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.information.IGroupInfo;
import com.i5i58.util.web.Authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "信息-公会（组）")
@RestController
@RequestMapping("${rpc.controller.version}")
public class GroupInfoRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IGroupInfo groupInfo;

	@ApiOperation(value = "模糊查询公会", notes = "")
	@RequestMapping(value = "/info/queryTopGroupList", method = RequestMethod.GET)
	public ResultDataSet queryTopGroupList(@ApiParam(value = "模糊匹配字符") @RequestParam(value = "param") String param,
			@ApiParam(value = "排序字段") @RequestParam(value = "sort") String sort,
			@ApiParam(value = "排序方向") @RequestParam(value = "dir") String dir,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = groupInfo.queryTopGroupList(param, sort, dir, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
