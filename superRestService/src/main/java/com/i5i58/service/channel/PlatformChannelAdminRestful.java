package com.i5i58.service.channel;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformChannelAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "平台频道管理服务")
@RestController
public class PlatformChannelAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPlatformChannelAdmin platformChannelAdmin;

	@ApiOperation(value = "开启/禁用频道", notes = "")
	@RequestMapping(value = "/super/enableChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet enableChannel(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "enable") boolean enable) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.enableChannel(HttpUtils.getAccIdFromHeader(), cId, enable);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取频道信息", notes = "")
	@RequestMapping(value = "/super/getChannelInfo", method = RequestMethod.GET)
	@SuperAuthorization
	public ResultDataSet getChannelInfo(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.getChannelInfo(cId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询频道列表", notes = "")
	@RequestMapping(value = "/super/queryChannelList", method = RequestMethod.GET)
	@SuperAuthorization
	public ResultDataSet queryChannelList(@RequestParam(value = "param") String param,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.queryChannelList(param, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询频道守护列表", notes = "")
	@RequestMapping(value = "/super/getChannelGuardlist", method = RequestMethod.GET)
	@SuperAuthorization
	public ResultDataSet getChannelGuardlist(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.getChannelGuardlist(cId, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加频道右侧信息", notes = "")
	@RequestMapping(value = "/super/addChannelRightInfo", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addChannelRightInfo(@RequestParam(value = "id") int id,
			@RequestParam(value = "action") String action, @RequestParam(value = "target") String target,
			@RequestParam(value = "imgUrl") String imgUrl, @RequestParam(value = "name") String name,
			@RequestParam(value = "params") String params) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.addChannelRightInfo(HttpUtils.getAccIdFromHeader(), id, action, target, imgUrl,
					name, params);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新频道右侧信息", notes = "")
	@RequestMapping(value = "/super/updateChannelRightInfo", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateChannelRightInfo(@RequestParam(value = "id") int id,
			@RequestParam(value = "action") String action, @RequestParam(value = "target") String target,
			@RequestParam(value = "imgUrl") String imgUrl, @RequestParam(value = "name") String name,
			@RequestParam(value = "params") String params) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.updateChannelRightInfo(HttpUtils.getAccIdFromHeader(), id, action, target,
					imgUrl, name, params);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除频道右侧信息", notes = "")
	@RequestMapping(value = "/super/deleteChannelRightInfo", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteChannelRightInfo(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.deleteChannelRightInfo(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取频道右侧信息", notes = "")
	@RequestMapping(value = "/super/getChannelRightInfo", method = RequestMethod.GET)
	public ResultDataSet getChannelRightInfo(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.getChannelRightInfo(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取所有频道右侧信息", notes = "")
	@RequestMapping(value = "/super/queryChannelRightInfo", method = RequestMethod.GET)
	public ResultDataSet queryChannelRightInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.queryChannelRightInfo();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步频道右侧信息到redis缓存", notes = "")
	@RequestMapping(value = "/super/syncChannelRightInfo", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet syncChannelRightInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.syncChannelRightInfo(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取新秀/大家都在看频道列表", notes = "")
	@RequestMapping(value = "/super/getNewLotChannelList", method = RequestMethod.GET)
	public ResultDataSet getNewLotChannelList(@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.getNewLotChannelList(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加新秀/大家都在看频道列表", notes = "")
	@RequestMapping(value = "/super/addNewLotChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addNewLotChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "排序") @RequestParam(value = "sortId") int sortId,
			@ApiParam(value = "新秀：1，大家都在看：2") @RequestParam(value = "newLot") int newLot) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.addNewLotChannel(HttpUtils.getAccIdFromHeader(), cId, sortId, newLot);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除新秀/大家都在看频道列表", notes = "")
	@RequestMapping(value = "/super/deleteNewLotChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteNewLotChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.deleteNewLotChannel(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步新秀/大家都在看频道列表到redis缓存", notes = "")
	@RequestMapping(value = "/super/syncNewLotChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet syncNewLotChannel() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.syncNewLotChannel(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "修改频道类型（db与缓存）", notes = "")
	@RequestMapping(value = "/super/editChannelType", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet editChannelType(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "频道类型，1潮音乐，2燃舞蹈，3脱口秀") @RequestParam(value = "type") int type) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.editChannelType(HttpUtils.getAccIdFromHeader(), cId, type);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空首页热门缓存", notes = "")
	@RequestMapping(value = "/super/clearHotPageCache", method = RequestMethod.POST)
	public ResultDataSet clearHotPageCache() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.clearHotPageCache();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步缓存频道数据到mysql", notes = "")
	@RequestMapping(value = "/super/syncChannelData", method = RequestMethod.POST)
	public ResultDataSet syncChannelData() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformChannelAdmin.syncChannelData();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询主播守护列表", notes = "")
	@RequestMapping(value = "/super/queryAchorGuardList", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet queryAchorGuardList(@ApiParam(value="查询对象") @RequestParam(value = "accId") String accId,
			@ApiParam(value="返回页码数，从0开始") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value="每页大小") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformChannelAdmin.queryAchorGuardList(accId, pageNum, pageSize);
		return rds;
	}
	
	@ApiOperation(value = "频道禁言/解禁", notes = "")
	@RequestMapping(value = "/super/setMute", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet setMute(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId,
			@ApiParam(value = "操作对象的accId") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "禁言(true),解禁(false)") @RequestParam(value = "optValue") String optValue) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformChannelAdmin.setMute(HttpUtils.getAccIdFromHeader(), cId, targetAccId, optValue);
		return rds;
	}
	
	@ApiOperation(value = "频道临时禁言/解禁", notes = "")
	@RequestMapping(value = "/super/setTempMute", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet setTempMute(
			@ApiParam(value = "频道cId") 
			@RequestParam(value = "cId") String cId,
			@ApiParam(value = "操作对象的accId") 
			@RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value = "=0时解禁，>0时禁言,最大24小时，单位秒") 
			@RequestParam(value = "duration") long duration) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformChannelAdmin.setTempMute(HttpUtils.getAccIdFromHeader(), cId, targetAccId, duration);
		return rds;
	}
}
