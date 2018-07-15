package com.i5i58.service.platform;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Web Restful Service for Platform admin
 * 
 * @author frank
 *
 */

@Api(value = "平台管理服务")
@RestController
public class PlatformRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPlatformAdmin platformAdmin;

	@ApiOperation(value = "插入测试OpenId", notes = "超管权限")
	@RequestMapping(value = "/super/insertTestDataToOpenIds", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet insertTestDataToOpenIds() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.insertTestDataToOpenIds(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加超管", notes = "超管权限")
	@RequestMapping(value = "/super/addSuperAdmin", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet addSuperAdmin(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "brithday") long brithday, @RequestParam(value = "depart") String depart,
			@RequestParam(value = "email") String email, @RequestParam(value = "gender") byte gender,
			@RequestParam(value = "location") String location, @RequestParam(value = "realName") String realName,
			@RequestParam(value = "auth") int auth) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.addSuperAdmin(HttpUtils.getAccIdFromHeader(), accId, brithday, depart, email,
				gender, location, realName, auth);
		return rds;
	}

	@ApiOperation(value = "禁用超管", notes = "超管权限")
	@RequestMapping(value = "/super/disableSuperAdmin", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet disableSuperAdmin(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.disableSuperAdmin(HttpUtils.getAccIdFromHeader(), accId);
		return rds;
	}

	@ApiOperation(value = "上线超管", notes = "超管权限")
	@RequestMapping(value = "/super/updateSuperAdmin", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet updateSuperAdmin() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.updateSuperAdmin(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "超管登录", notes = "")
	@RequestMapping(value = "/super/LoginByOpenId", method = RequestMethod.POST)
	public ResultDataSet superLoginByOpenId(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "accountVersion") int accountVersion) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.superLoginByOpenId(openId, password, accountVersion, HttpUtils.getRealClientIpAddress());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询轮播", notes = "超管权限")
	@RequestMapping(value = "/super/queryCarousel", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryCarousel(@RequestParam(value = "device") int device,
			@RequestParam(value = "startTime") long startTime, @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.queryCarousel(device, startTime, endTime);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加轮播", notes = "超管权限")
	@RequestMapping(value = "/super/addCarousel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addCarousel(@RequestParam(value = "device") int device,
			@RequestParam(value = "index") int index, @RequestParam(value = "imgUrl") String imgUrl,
			@RequestParam(value = "action") String action, @RequestParam(value = "params") String params,
			@RequestParam(value = "isChannel") boolean isChannel, @RequestParam(value = "cId") String cId,
			@RequestParam(value = "startTime") long startTime, @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.addCarousel(HttpUtils.getAccIdFromHeader(), device, index, imgUrl, action, params,
					isChannel, cId, startTime, endTime);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除轮播", notes = "超管权限")
	@RequestMapping(value = "/super/removeCarousel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet removeCarousel(@RequestParam(value = "id") long id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.removeCarousel(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新轮播", notes = "超管权限")
	@RequestMapping(value = "/super/updateCarousel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateCarousel(@RequestParam(value = "id") long id, @RequestParam(value = "device") int device,
			@RequestParam(value = "index") int index, @RequestParam(value = "imgUrl") String imgUrl,
			@RequestParam(value = "action") String action, @RequestParam(value = "params") String params,
			@RequestParam(value = "isChannel") boolean isChannel, @RequestParam(value = "cId") String cId,
			@RequestParam(value = "startTime") long startTime, @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.updateCarousel(HttpUtils.getAccIdFromHeader(), id, device, index, imgUrl, action,
					params, isChannel, cId, startTime, endTime);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取轮播", notes = "超管权限")
	@RequestMapping(value = "/super/getCarousel", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getCarousel(@RequestParam(value = "id") long id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.getCarousel(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询超管列表", notes = "超管权限")
	@RequestMapping(value = "/super/querySuperAdminList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet querySuperAdminList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.querySuperAdminList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	/**
	 * Define Restful service
	 * 
	 * @author frank
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "清除轮播图缓存", notes = "")
	@RequestMapping(value = "/super/refreashCarouselCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet refreashCarouselCache() {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		// Valid Type Check
		try {
			// call service
			rds = platformAdmin.refreashCarouselCache();
		} catch (IOException e) {
			// For Exception, set Response Object
			System.out.println(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}

	@ApiOperation(value = "清除账号缓存", notes = "")
	@RequestMapping(value = "/super/syncHotAccountCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet syncHotAccountCache() {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		// Valid Type Check
		try {
			// call service
			rds = platformAdmin.syncHotAccountCache(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			// For Exception, set Response Object
			System.out.println(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}

	@ApiOperation(value = "清除观众缓存", notes = "")
	@RequestMapping(value = "/super/clearChannelViewer", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet clearChannelViewer() {
		// Define Response Object
		ResultDataSet rds = new ResultDataSet();
		// Valid Type Check
		try {
			// call service
			rds = platformAdmin.clearChannelViewer(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			// For Exception, set Response Object
			System.out.println(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		// Response
		return rds;
	}

	@ApiOperation(value = "清除HotChannel在redis缓存", notes = "")
	@RequestMapping(value = "/super/clearHotChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet clearHotChannel() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.clearHotChannel(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步频道数据到redis缓存", notes = "")
	@RequestMapping(value = "/super/syncHotChannel", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet syncHotChannel() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAdmin.syncHotChannel(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "增加频道id库存", notes = "")
	@RequestMapping(value = "/super/addChannelId", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet addChannelId(@ApiParam("增长量") @RequestParam(value = "count") int count) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.addChannelId(count);
		return rds;
	}

	@ApiOperation(value = "获取一个随机的空闲频道id", notes = "")
	@RequestMapping(value = "/super/getRandomChannelId", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet getRandomChannelId(@RequestParam(value = "consume") boolean consume) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.getRandomChannelId(consume);
		return rds;
	}

//	@ApiOperation(value = "清空频道缓存", notes = "")
//	@RequestMapping(value = "/super/clearHotChannel", method = RequestMethod.POST)
//	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
//	public ResultDataSet clearHotChannelCache() {
//		ResultDataSet rds = new ResultDataSet();
//		try {
//			rds = platformAdmin.clearHotChannelCache(HttpUtils.getAccIdFromHeader());
//		} catch (IOException e) {
//			logger.error("", e);
//			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
//			rds.setMsg("Service Error");
//		}
//		return rds;
//	}
	

	@ApiOperation(value = "查询充值记录", notes = "按照status返回json数据")
	@RequestMapping(value = "/super/getOnlineOrders", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet getOnlineOrders(@RequestParam(value = "status") int status,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAdmin.getOnlineOrders(status,pageNum,pageSize);
		return rds;
	}

}
