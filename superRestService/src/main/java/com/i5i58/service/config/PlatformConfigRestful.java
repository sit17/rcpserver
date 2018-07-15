package com.i5i58.service.config;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformConfig;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "平台配置服务")
@RestController
public class PlatformConfigRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	IPlatformConfig platformConfig;

	@ApiOperation(value = "添加新礼物", notes = "超管权限")
	@RequestMapping(value = "/super/addGift", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addGift(@RequestParam(value = "id") int id, @RequestParam(value = "price") long price,
			@RequestParam(value = "anchorPrice") long anchorPrice, @RequestParam(value = "name") String name,
			@RequestParam(value = "nullity") boolean nullity, @RequestParam(value = "isForGuard") boolean isForGuard,
			@RequestParam(value = "isForVip") boolean isForVip, @RequestParam(value = "unit") String unit,
			@RequestParam(value = "maxCount") int maxCount, @RequestParam(value = "function") String function,
			@RequestParam(value = "condition") int condition, @RequestParam(value = "path") String path,
			@RequestParam(value = "broadcast") boolean broadcast, @RequestParam(value = "sortId") int sortId,
			@RequestParam(value = "node") int node, @RequestParam(value = "flashPath") String flashPath) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addGift(HttpUtils.getAccIdFromHeader(), id, price, anchorPrice, name, nullity,
					isForGuard, isForVip, unit, maxCount, function, condition, path, broadcast, sortId, node,
					flashPath);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新礼物", notes = "超管权限")
	@RequestMapping(value = "/super/updateGift", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateGift(@RequestParam(value = "id") int id, @RequestParam(value = "price") long price,
			@RequestParam(value = "anchorPrice") long anchorPrice, @RequestParam(value = "name") String name,
			@RequestParam(value = "nullity") boolean nullity, @RequestParam(value = "isForGuard") boolean isForGuard,
			@RequestParam(value = "isForVip") boolean isForVip, @RequestParam(value = "unit") String unit,
			@RequestParam(value = "maxCount") int maxCount, @RequestParam(value = "function") String function,
			@RequestParam(value = "condition") int condition, @RequestParam(value = "path") String path,
			@RequestParam(value = "broadcast") boolean broadcast, @RequestParam(value = "sortId") int sortId,
			@RequestParam(value = "node") int node, @RequestParam(value = "flashPath") String flashPath) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateGift(HttpUtils.getAccIdFromHeader(), id, price, anchorPrice, name, nullity,
					isForGuard, isForVip, unit, maxCount, function, condition, path, broadcast, sortId, node,
					flashPath);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取礼物", notes = "超管权限")
	@RequestMapping(value = "/super/getGift", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getGift(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getGift(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除礼物", notes = "超管权限")
	@RequestMapping(value = "/super/deleteGift", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteGift(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteGift(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询礼物列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryGiftList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryGiftList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryGiftList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询礼物列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryGift", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryGift(@RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryGift(name);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空礼物设置", notes = "")
	@RequestMapping(value = "/super/refreashGiftConfigCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet refreashChannelGiftConfigCache() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.refreashChannelGiftConfigCache();
		return rds;
	}

	@ApiOperation(value = "更新礼物版本", notes = "")
	@RequestMapping(value = "/super/newGiftVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet newGiftVersion() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.newGiftVersion();
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			rds = platformConfig.refreashChannelGiftConfigCache();
			try {
				rds = platformConfig.getChannelGiftConfig();
			} catch (IOException e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
		}
		return rds;
	}

	@ApiOperation(value = "生成UUID", notes = "")
	@RequestMapping(value = "/super/createUUID", method = RequestMethod.GET)
	public ResultDataSet createUUID() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.createUUID();
		return rds;
	}

	@ApiOperation(value = "添加新坐骑", notes = "超管权限")
	@RequestMapping(value = "/super/addMount", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addMount(@RequestParam(value = "id") int id, @RequestParam(value = "price") long price,
			@RequestParam(value = "name") String name, @RequestParam(value = "nullity") boolean nullity,
			@RequestParam(value = "isForGuard") boolean isForGuard, @RequestParam(value = "isForVip") boolean isForVip,
			@RequestParam(value = "function") String function, @RequestParam(value = "path") String path,
			@RequestParam(value = "validity") int validity,
			@RequestParam(value = "isForFansClubs") boolean isForFansClubs, @RequestParam(value = "level") int level) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addMount(HttpUtils.getAccIdFromHeader(), id, price, name, nullity, isForGuard,
					isForVip, function, path, validity, isForFansClubs, level);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新坐骑", notes = "超管权限")
	@RequestMapping(value = "/super/updateMount", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateMount(@RequestParam(value = "id") int id, @RequestParam(value = "price") long price,
			@RequestParam(value = "name") String name, @RequestParam(value = "nullity") boolean nullity,
			@RequestParam(value = "isForGuard") boolean isForGuard, @RequestParam(value = "isForVip") boolean isForVip,
			@RequestParam(value = "function") String function, @RequestParam(value = "path") String path,
			@RequestParam(value = "validity") int validity,
			@RequestParam(value = "isForFansClubs") boolean isForFansClubs, @RequestParam(value = "level") int level) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateMount(HttpUtils.getAccIdFromHeader(), id, price, name, nullity, isForGuard,
					isForVip, function, path, validity, isForFansClubs, level);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取坐骑", notes = "超管权限")
	@RequestMapping(value = "/super/getMount", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getMount(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getMount(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除坐骑", notes = "超管权限")
	@RequestMapping(value = "/super/deleteMount", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteMount(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteMount(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询坐骑列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryMountList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryMountList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryMountList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空坐骑设置", notes = "")
	@RequestMapping(value = "/super/refreashMountConfigCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet refreashChannelMountConfigCache() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.refreashChannelMountConfigCache();
		return rds;
	}

	@ApiOperation(value = "更新坐骑版本", notes = "")
	@RequestMapping(value = "/super/newMountVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet newMountVersion() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.newMountVersion();
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			rds = platformConfig.refreashChannelMountConfigCache();
			try {
				rds = platformConfig.getChannelMountConfig();
			} catch (IOException e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
		}
		return rds;
	}

	@ApiOperation(value = "设置移动端最小坐骑显示ID", notes = "")
	@RequestMapping(value = "/super/setMobileMountMin", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet setMobileMountMin(@RequestParam(value = "mobileMountMin") int mobileMountMin) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.setMobileMountMin(mobileMountMin);
		return rds;
	}

	@ApiOperation(value = "获取移动端最小坐骑显示ID", notes = "")
	@RequestMapping(value = "/super/getMobileMountMin", method = RequestMethod.GET)
	public ResultDataSet getMobileMountMin() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getMobileMountMin();
		return rds;
	}

	@ApiOperation(value = "获取频道动画配置", notes = "")
	@RequestMapping(value = "/super/getAnimationConfig", method = RequestMethod.GET)
	public ResultDataSet getAnimationConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getAnimationConfig();
		return rds;
	}

	@ApiOperation(value = "更新频道动画版本", notes = "")
	@RequestMapping(value = "/super/newAnimationVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet newAnimationVersion() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.newAnimationVersion();
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())) {
			rds = platformConfig.refreashAnimationConfigCache();
			rds = platformConfig.getAnimationConfig();
		}
		return rds;
	}

	@ApiOperation(value = "保存频道动画设置", notes = "")
	@RequestMapping(value = "/super/saveAnimationConfig", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet saveAnimationConfig(@RequestParam(value = "zipUrl") String zipUrl,
			@RequestParam(value = "resUrl") String resUrl) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.saveAnimationConfig(zipUrl, resUrl);
		return rds;
	}

	@ApiOperation(value = "清空频道动画配置 缓存", notes = "")
	@RequestMapping(value = "/super/refreashAnimationConfigCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet refreashAnimationConfigCache() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.refreashAnimationConfigCache();
		return rds;
	}

	@ApiOperation(value = "添加react native模块", notes = "")
	@RequestMapping(value = "/super/addRn", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addRn(@ApiParam(value = "唯一id") @RequestParam(value = "id") String id,
			@ApiParam(value = "节点") @RequestParam(value = "node") String node,
			@ApiParam(value = "显示的名称") @RequestParam(value = "name") String name,
			@ApiParam(value = "显示的图标") @RequestParam(value = "icon") String icon,
			@ApiParam(value = "rn模块") @RequestParam(value = "module") String module,
			@ApiParam(value = "rn zip包url(暂不使用)") @RequestParam(value = "rnZip") String rnZip,
			@ApiParam(value = "rn type fixed:固定rn， list:列表rn") @RequestParam(value = "type") String type,
			@ApiParam(value = "list rn 中的段落:每个段落之间有间隔") @RequestParam(value = "section") int section,
			@ApiParam(value = "rn总版本号 ") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addRn(HttpUtils.getAccIdFromHeader(), id, node, name, icon, module, rnZip, type,
					section, version);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取react native模块", notes = "")
	@RequestMapping(value = "/super/getRn", method = RequestMethod.GET)
	public ResultDataSet getRn(@ApiParam(value = "唯一id") @RequestParam(value = "id") String id,
			@ApiParam(value = "唯一id") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getRn(id, version);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新react native模块", notes = "")
	@RequestMapping(value = "/super/updateRn", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateRn(@ApiParam(value = "唯一id") @RequestParam(value = "id") String id,
			@ApiParam(value = "节点") @RequestParam(value = "node") String node,
			@ApiParam(value = "显示的名称") @RequestParam(value = "name") String name,
			@ApiParam(value = "显示的图标") @RequestParam(value = "icon") String icon,
			@ApiParam(value = "rn模块") @RequestParam(value = "module") String module,
			@ApiParam(value = "rn zip包url(暂不使用)") @RequestParam(value = "rnZip") String rnZip,
			@ApiParam(value = "rn type fixed:固定rn， list:列表rn") @RequestParam(value = "type") String type,
			@ApiParam(value = "list rn 中的段落:每个段落之间有间隔") @RequestParam(value = "section") int section,
			@ApiParam(value = "rn总版本号") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateRn(HttpUtils.getAccIdFromHeader(), id, node, name, icon, module, rnZip, type,
					section, version);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除react native模块", notes = "")
	@RequestMapping(value = "/super/deleteRn", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteRn(@ApiParam(value = "唯一id") @RequestParam(value = "id") String id,
			@ApiParam(value = "唯一id") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteRn(HttpUtils.getAccIdFromHeader(), id, version);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空rn缓存", notes = "")
	@RequestMapping(value = "/super/clearRnChache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet clearRnChache() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.clearRnChache();
		return rds;
	}

	@ApiOperation(value = "添加 react native 版本", notes = "")
	@RequestMapping(value = "/super/addRnVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addRnVersion(@ApiParam(value = "rn 版本") @RequestParam(value = "version") String version,
			@ApiParam(value = "react native zip url") @RequestParam(value = "rnZipUrl") String rnZipUrl,
			@ApiParam(value = "react native 描述，需要详细") @RequestParam(value = "rnDescribe") String rnDescribe) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addRnVersion(HttpUtils.getAccIdFromHeader(), version, rnZipUrl, rnDescribe);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除 react native 版本", notes = "")
	@RequestMapping(value = "/super/deleteRnVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteRnVersion(@ApiParam(value = "rn 版本") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteRnVersion(HttpUtils.getAccIdFromHeader(), version);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "分页获取 react native 版本", notes = "")
	@RequestMapping(value = "/super/queryRnVersion", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet queryRnVersion(@ApiParam(value = "rn 版本") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "rn 版本") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryRnVersion(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "模糊查询react native模块", notes = "")
	@RequestMapping(value = "/super/queryRn", method = RequestMethod.POST)
	public ResultDataSet queryRn(@ApiParam(value = "模糊匹配") @RequestParam(value = "params") String params) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryRn(params);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "分页查询react native列表", notes = "")
	@RequestMapping(value = "/super/queryRnList", method = RequestMethod.POST)
	public ResultDataSet queryRnList(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryRnList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加VIP配置", notes = "超管权限")
	@RequestMapping(value = "/super/addAccountVip", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addAccountVip(@RequestParam(value = "id") int id, @RequestParam(value = "level") int level,
			@RequestParam(value = "month") int month, @RequestParam(value = "price") long price) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addAccountVip(HttpUtils.getAccIdFromHeader(), id, level, month, price);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新VIP配置", notes = "超管权限")
	@RequestMapping(value = "/super/updateAccountVip", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateAccountVip(@RequestParam(value = "id") int id, @RequestParam(value = "level") int level,
			@RequestParam(value = "month") int month, @RequestParam(value = "price") long price) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateAccountVip(HttpUtils.getAccIdFromHeader(), id, level, month, price);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除VIP配置", notes = "超管权限")
	@RequestMapping(value = "/super/deleteAccountVip", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteAccountVip(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteAccountVip(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取VIP配置", notes = "超管权限")
	@RequestMapping(value = "/super/getAccountVip", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAccountVip(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getAccountVip(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询VIP配置列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryAccountVipList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAccountVipList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryAccountVipList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空VIP配置设置", notes = "")
	@RequestMapping(value = "/super/refreashAccountVipCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet refreashAccountVipCache() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.refreashAccountVipCache();
		return rds;
	}

	@ApiOperation(value = "获取VIP等级信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/getAccountVipConfig", method = org.springframework.web.bind.annotation.RequestMethod.GET)
	public ResultDataSet getAccountVipConfig() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getAccountVipConfig();
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加骑士/佣金配置", notes = "超管权限")
	@RequestMapping(value = "/super/addGuardCommission", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addGuardCommission(@RequestParam(value = "id") int id,
			@RequestParam(value = "guardLevel") int guardLevel,
			@RequestParam(value = "moneyOneMonth") long moneyOneMonth) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addGuardCommissionConfig(HttpUtils.getAccIdFromHeader(), id, guardLevel,
					moneyOneMonth);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新骑士/佣金配置", notes = "超管权限")
	@RequestMapping(value = "/super/updateGuardCommission", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateGuardCommission(@RequestParam(value = "id") int id,
			@RequestParam(value = "guardLevel") int guardLevel,
			@RequestParam(value = "moneyOneMonth") long moneyOneMonth) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateGuardCommissionConfig(HttpUtils.getAccIdFromHeader(), id, guardLevel,
					moneyOneMonth);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除骑士/佣金配置", notes = "超管权限")
	@RequestMapping(value = "/super/deleteGuardCommission", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteGuardCommission(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteGuardCommissionConfig(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取骑士/佣金配置", notes = "超管权限")
	@RequestMapping(value = "/super/getGuardCommission", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getGuardCommission(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getGuardCommissionConfig(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询骑士/佣金配置列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryGuardCommission", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryGuardCommission(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryGuardCommissionConfigList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取骑士/佣金等级信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/getGuardCommissionList", method = org.springframework.web.bind.annotation.RequestMethod.GET)
	public ResultDataSet getGuardCommissionList() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getGuardCommissionConfigList();
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加守护配置", notes = "超管权限")
	@RequestMapping(value = "/super/addChannelGuard", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addChannelGuard(@RequestParam(value = "id") int id, @RequestParam(value = "level") int level,
			@RequestParam(value = "month") int month, @RequestParam(value = "price") long price) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addChannelGuard(HttpUtils.getAccIdFromHeader(), id, level, month, price);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新守护配置", notes = "超管权限")
	@RequestMapping(value = "/super/updateChannelGuard", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateChannelGuard(@RequestParam(value = "id") int id,
			@RequestParam(value = "level") int level, @RequestParam(value = "month") int month,
			@RequestParam(value = "price") long price) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateChannelGuard(HttpUtils.getAccIdFromHeader(), id, level, month, price);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除守护配置", notes = "超管权限")
	@RequestMapping(value = "/super/deleteChannelGuard", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteChannelGuard(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteChannelGuard(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取守护设置", notes = "超管权限")
	@RequestMapping(value = "/super/getChannelGuard", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getChannelGuard(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getChannelGuard(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询守护设置列表", notes = "超管权限")
	@RequestMapping(value = "/super/queryChannelGuardList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryChannelGuardList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.queryChannelGuardList(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "清空守护配置", notes = "")
	@RequestMapping(value = "/super/refreashChannelGuardCache", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet refreashChannelGuardCache() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.refreashChannelGuardCache();
		return rds;
	}

	@ApiOperation(value = "获取守护配置", notes = "")
	@RequestMapping(value = "/super/getChannelGuardConfig", method = RequestMethod.GET)
	public ResultDataSet getChannelGuardConfig() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getChannelGuardConfig();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加礼物节点", notes = "")
	@RequestMapping(value = "/super/addGiftType", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addGiftType(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addGiftType(id, name);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新礼物节点", notes = "")
	@RequestMapping(value = "/super/updateGiftType", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateGiftType(@RequestParam(value = "id") int id, @RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateGiftType(id, name);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除礼物节点", notes = "")
	@RequestMapping(value = "/super/deleteGiftType", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteGiftType(@RequestParam(value = "id") int id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteGiftType(id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取礼物节点", notes = "")
	@RequestMapping(value = "/super/getGiftTypes", method = RequestMethod.GET)
	public ResultDataSet getGiftTypes() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getGiftTypes();
		return rds;
	}

	@ApiOperation(value = "添加App版本", notes = "")
	@RequestMapping(value = "/super/addAppVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet addAppVersion(@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "主版本") @RequestParam(value = "main") int main,
			@ApiParam(value = "次版本") @RequestParam(value = "sub") int sub,
			@ApiParam(value = "功能版本") @RequestParam(value = "func") int func,
			@ApiParam(value = "更新地址") @RequestParam(value = "updateUrl") String updateUrl,
			@ApiParam(value = "描述") @RequestParam(value = "describe") String describe,
			@ApiParam(value = "设置苹果配置，0:审核 or 1:运营") @RequestParam(value = "appleRelease") int appleRelease,
			@ApiParam(value = "设置苹果支付类型，0:苹果支付，1:第三方支付，2:苹果支付与第三方支付") @RequestParam(value = "applePayType") int applePayType,
			@ApiParam(value = "react native 版本") @RequestParam(value = "rnVersion") String rnVersion) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.addAppVersion(HttpUtils.getAccIdFromHeader(), device, main, sub, func, updateUrl,
					describe, appleRelease, applePayType, rnVersion);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新App版本状态", notes = "")
	@RequestMapping(value = "/super/updateAppVersionStatus", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet updateAppVersionStatus(@ApiParam(value = "app版本id") @RequestParam(value = "id") String id,
			@ApiParam(value = "app版本状态") @RequestParam(value = "status") int status) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.updateAppVersionStatus(HttpUtils.getAccIdFromHeader(), id, status);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除App版本", notes = "")
	@RequestMapping(value = "/super/deleteAppVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SYSTEM_CONTROL_AUTH)
	public ResultDataSet deleteAppVersion(@ApiParam(value = "app版本id") @RequestParam(value = "id") String id) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.deleteAppVersion(HttpUtils.getAccIdFromHeader(), id);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "列出App版本", notes = "")
	@RequestMapping(value = "/super/listAppVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.BASE_AUTH)
	public ResultDataSet listAppVersion(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.listAppVersion(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "设置windows更新用的json文件md5码", notes = "")
	@RequestMapping(value = "/super/setWindowsUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setWindowsUpdateVersion(
			@ApiParam(value = "windows更新登陆程序版本") @RequestParam(value = "loginVersion") String loginVersion,
			@ApiParam(value = "windows更新用的json文件md5码") @RequestParam(value = "jsonMd5") String jsonMd5,
			@ApiParam(value = "windows更新用的json文件下载路径") @RequestParam(value = "jsonPath") String jsonPath,
			@ApiParam(value = "windows更新用的json文件下载路径") @RequestParam(value = "bossZipUrl") String bossZipUrl) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.setWindowsUpdateVersion(loginVersion, jsonMd5, jsonPath, bossZipUrl);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "设置windows更新用的登录更新包下载地址", notes = "")
	@RequestMapping(value = "/super/setWindowsUpdateLoginPath", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setWindowsUpdateLoginPath(
			@ApiParam(value = "windows更新登陆程序版本") @RequestParam(value = "loginPath") String loginPath) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.setWindowsUpdateLoginPath(loginPath);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "设置windows更新用的登录更新包下载地址", notes = "")
	@RequestMapping(value = "/super/setWindowsLoginAdvPath", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setWindowsLoginAdvPath(
			@ApiParam(value = "windows登陆界面广告图") @RequestParam(value = "imagePath") String imagePath,
			@ApiParam(value = "windows登录界面广告图跳转地址") @RequestParam(value = "redirectPath") String redirectPath) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.setWindowsLoginAdvPath(imagePath,redirectPath);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "设置windows客户端首页、内嵌直播间网页版本（触发清空缓存）", notes = "")
	@RequestMapping(value = "/super/setWindowsWebPageVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setWindowsWebPageVersion(
			@ApiParam(value = "windows客户端首页版本") @RequestParam(value = "homePageVersion") String homePageVersion,
			@ApiParam(value = "windows客户端内嵌直播间网页版本") @RequestParam(value = "livePageVersion") String livePageVersion) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.setWindowsWebPageVersion(homePageVersion, livePageVersion);
		return rds;
	}

	@ApiOperation(value = "获取windows客户端首页、内嵌直播间网页版本", notes = "")
	@RequestMapping(value = "/super/getWindowsWebPageVersion", method = RequestMethod.GET)
	public ResultDataSet getWindowsWebPageVersion() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getWindowsWebPageVersion();
		return rds;
	}

	@ApiOperation(value = "同步至redis，windows客户端首页、内嵌直播间网页版本（触发清空缓存）", notes = "")
	@RequestMapping(value = "/super/syncWindowsWebPageVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet syncWindowsWebPageVersion() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.syncWindowsWebPageVersion();
		return rds;
	}

	@ApiOperation(value = "获取windows更新信息md5码", notes = "")
	@RequestMapping(value = "/super/getWindowsUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet getWindowsUpdateVersion() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getWindowsUpdateVersion();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步windows更新信息md5码到redis", notes = "")
	@RequestMapping(value = "/super/syncWindowsUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet syncWindowsUpdateVersion() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.syncWindowsUpdateVersion();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步windows游戏更新信息md5码到redis", notes = "")
	@RequestMapping(value = "/super/setWindowsGameUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setWindowsGameUpdateVersion(
			@ApiParam(value = "windows更新登陆程序版本") @RequestParam(value = "gameJsonMd5") String gameJsonMd5,
			@ApiParam(value = "windows更新用的json文件md5码") @RequestParam(value = "gameJsonPath") String gameJsonPath,
			@ApiParam(value = "windows更新用的json文件下载路径") @RequestParam(value = "gameZipUrl") String gameZipUrl) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.setWindowsGameUpdateVersion(gameJsonMd5, gameJsonPath, gameZipUrl);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取windows游戏更新信息md5码", notes = "")
	@RequestMapping(value = "/super/getWindowsGameUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet getWindowsGameUpdateVersion() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.getWindowsGameUpdateVersion();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同步windows游戏更新信息md5码到redis", notes = "")
	@RequestMapping(value = "/super/syncWindowsGameUpdateVersion", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet syncWindowsGameUpdateVersion() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformConfig.syncWindowsGameUpdateVersion();
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加粉丝团配置项", notes = "")
	@RequestMapping(value = "/super/addFansClubConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addFansClubConfig(@ApiParam(value = "粉丝团的有效时间,按月") @RequestParam(value = "month") int month,
			@ApiParam(value = "该月份享受的折扣") @RequestParam(value = "disacount") float disacount) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addFansClubConfig(month, disacount);
		return rds;
	}

	@ApiOperation(value = "更新粉丝团配置项", notes = "")
	@RequestMapping(value = "/super/updateFansClubConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateFansClubConfig(@ApiParam(value = "粉丝团的有效时间,按月") @RequestParam(value = "month") int month,
			@ApiParam(value = "该月份享受的折扣") @RequestParam(value = "disacount") float disacount) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.updateFansClubConfig(month, disacount);
		return rds;
	}

	@ApiOperation(value = "删除粉丝团配置项", notes = "")
	@RequestMapping(value = "/super/deleteFansClubConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteFansClubConfig(
			@ApiParam(value = "粉丝团的有效时间,按月") @RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteFansClubConfig(month);
		return rds;
	}

	@ApiOperation(value = "查询粉丝团配置项", notes = "")
	@RequestMapping(value = "/super/queryFansClubConfigs", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryFansClubConfigs(
			@ApiParam(value = "粉丝团的有效时间,按月") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "粉丝团的价格") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.queryFansClubConfigs(pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "刷新粉丝团配置项", notes = "")
	@RequestMapping(value = "/super/freshFansClubsConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet freshFansClubsConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.freshFansClubsConfig();
		return rds;
	}

	@ApiOperation(value = "获取粉丝团信息", notes = "")
	@RequestMapping(value = "/super/getFansClubByMonth", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getFansClubByMonth(@RequestParam(value = "month") int month) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getFansClubByMonth(month);
		return rds;
	}

	@ApiOperation(value = "添加赠送坐骑的配置", notes = "")
	@RequestMapping(value = "/super/addPresentMountConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addPresentMountConfig(
			@ApiParam(value = "可以赠送坐骑的操作类型,1:首冲,2 : 购买守护，3：购买vip") @RequestParam(value = "type") int type,
			@ApiParam(value = "守护或vip对应的等级") @RequestParam(value = "level") int level,
			@ApiParam(value = "赠送的坐骑id") @RequestParam(value = "mountId") int mountId,
			@ApiParam(value = "坐骑的有效时间(月分),只对首充有效") @RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addPresentMountConfig(type, level, mountId, month);
		return rds;
	}

	@ApiOperation(value = "删除赠送坐骑的配置", notes = "")
	@RequestMapping(value = "/super/deletePresentMountConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deletePresentMountConfig(
			@ApiParam(value = "可以赠送坐骑的操作类型,1:首冲,2 : 购买守护，3：购买vip") @RequestParam(value = "type") int type,
			@ApiParam(value = "守护或vip对应的等级") @RequestParam(value = "level") int level) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deletePresentMountConfig(type, level);
		return rds;
	}

	@ApiOperation(value = "修改赠送坐骑的配置", notes = "")
	@RequestMapping(value = "/super/updatePresentMountConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updatePresentMountConfig(
			@ApiParam(value = "可以赠送坐骑的操作类型,1:首冲,2 : 购买守护，3：购买vip") @RequestParam(value = "type") int type,
			@ApiParam(value = "守护或vip对应的等级") @RequestParam(value = "level") int level,
			@ApiParam(value = "赠送的坐骑id") @RequestParam(value = "mountId") int mountId,
			@ApiParam(value = "坐骑的有效时间(月分),只对首充有效") @RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.updatePresentMountConfig(type, level, mountId, month);
		return rds;
	}

	@ApiOperation(value = "获取指定的赠送坐骑配置", notes = "")
	@RequestMapping(value = "/super/getPresentMountConfig", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getPresentMountConfig(
			@ApiParam(value = "可以赠送坐骑的操作类型,1:首冲,2 : 购买守护，3：购买vip") @RequestParam(value = "type") int type,
			@ApiParam(value = "守护或vip对应的等级") @RequestParam(value = "level") int level) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getPresentMountConfig(type, level);
		return rds;
	}

	@ApiOperation(value = "查询赠送坐骑配置", notes = "")
	@RequestMapping(value = "/super/queryPresentMountConfig", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryPresentMountConfig(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.queryPresentMountConfig(pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "添加粉丝团任务配置项", notes = "")
	@RequestMapping(value = "/super/addClubTaskConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet addClubTaskConfig(@ApiParam(value = "任务ID") @RequestParam(value = "taskId") int taskId,
			@ApiParam(value = "任务积分") @RequestParam(value = "score") long score,
			@ApiParam(value = "任务描述") @RequestParam(value = "desc") String desc) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addClubTaskConfig(taskId, score, desc);
		return rds;
	}

	@ApiOperation(value = "更新粉丝团任务配置项", notes = "")
	@RequestMapping(value = "/super/updateClubTaskConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateClubTaskConfig(@ApiParam(value = "任务ID") @RequestParam(value = "taskId") int taskId,
			@ApiParam(value = "任务积分") @RequestParam(value = "score") long score,
			@ApiParam(value = "任务描述") @RequestParam(value = "desc") String desc) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.updateClubTaskConfig(taskId, score, desc);
		return rds;
	}

	@ApiOperation(value = "删除粉丝团任务配置项", notes = "")
	@RequestMapping(value = "/super/deleteClubTaskConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteClubTaskConfig(@ApiParam(value = "任务ID") @RequestParam(value = "taskId") int taskId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteClubTaskConfig(taskId);
		return rds;
	}

	@ApiOperation(value = "查询粉丝团任务配置项", notes = "")
	@RequestMapping(value = "/super/queryClubTaskConfigs", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryClubTaskConfigs(
			@ApiParam(value = "粉丝团的有效时间,按月") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "粉丝团的价格") @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.queryClubTaskConfigs(pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "刷新粉丝团任务配置项", notes = "")
	@RequestMapping(value = "/super/freshClubTaskConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet freshClubTaskConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.freshClubTaskConfig();
		return rds;
	}

	@ApiOperation(value = "获取粉丝团任务信息", notes = "")
	@RequestMapping(value = "/super/getClubTask", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getClubTask(@RequestParam(value = "taskId") int taskId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getClubTask(taskId);
		return rds;
	}

	@ApiOperation(value = "更新游戏币兑换比率", notes = "")
	@RequestMapping(value = "/super/updateGameExchangeRate", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet updateGameExchangeRate(
			@ApiParam(value = "游戏币兑换钻石") @RequestParam(value = "gameDiamondRate") String gameDiamondRate,
			@ApiParam(value = "虎币兑换游戏币") @RequestParam(value = "gameGoldRate") String gameGoldRate,
			@ApiParam(value = "魅力值兑换游戏币") @RequestParam(value = "lovelinessRate") String lovelinessRate)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.updateGameExchangeRate(gameDiamondRate, gameGoldRate, lovelinessRate);
		return rds;
	}

	@ApiOperation(value = "获取游戏币兑换比率", notes = "")
	@RequestMapping(value = "/super/queryGameExchangeRate", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryGameExchangeRate() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.queryGameExchangeRate();
		return rds;
	}

	@ApiOperation(value = "设置app分享url", notes = "")
	@RequestMapping(value = "/super/setAppShareUrl", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setAppShareUrl(
			@ApiParam(value = "app分享url") @RequestParam(value = "appShareUrl") String appShareUrl) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.setAppShareUrl(appShareUrl);
		return rds;
	}

	@ApiOperation(value = "获取app分享url", notes = "")
	@RequestMapping(value = "/super/getAppShareUrl", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAppShareUrl() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getAppShareUrl();
		return rds;
	}

	@ApiOperation(value = "添加首页频道类型", notes = "")
	@RequestMapping(value = "/super/addHomeType", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet addHomeType(@ApiParam(value = "名称") @RequestParam(value = "name") String name,
			@ApiParam(value = "值") @RequestParam(value = "value") int value,
			@ApiParam(value = "序号") @RequestParam(value = "sortId") int sortId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addHomeType(name, value, sortId);
		return rds;
	}

	@ApiOperation(value = "删除首页频道类型", notes = "")
	@RequestMapping(value = "/super/deleteHomeType", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet deleteHomeType(@ApiParam(value = "名称") @RequestParam(value = "name") String name)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteHomeType(name);
		return rds;
	}

	@ApiOperation(value = "获取首页频道类型", notes = "")
	@RequestMapping(value = "/super/getHomeType", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getHomeType() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getHomeType();
		return rds;
	}

	@ApiOperation(value = "添加平台配置", notes = "")
	@RequestMapping(value = "/super/addPlatformConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet addPlatformConfig(@ApiParam(value = "名称") @RequestParam(value = "key") String key,
			@ApiParam(value = "描述") @RequestParam(value = "desc") String desc,
			@ApiParam(value = "值") @RequestParam(value = "value") String value) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addPlatformConfig(key, desc, value);
		return rds;
	}

	@ApiOperation(value = "清除平台配置缓存", notes = "")
	@RequestMapping(value = "/super/clearPlatformConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet clearPlatformConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.clearPlatformConfig();
		return rds;
	}

	@ApiOperation(value = "修改平台配置", notes = "")
	@RequestMapping(value = "/super/UpdatePlatformConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet UpdatePlatformConfig(@ApiParam(value = "名称") @RequestParam(value = "key") String key,
			@ApiParam(value = "值") @RequestParam(value = "value") String value) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.UpdatePlatformConfig(key, value);
		return rds;
	}

	@ApiOperation(value = "列出平台配置", notes = "")
	@RequestMapping(value = "/super/ListPlatformConfig", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet ListPlatformConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.ListPlatformConfig();
		return rds;
	}

	@ApiOperation(value = "获取商品列表", notes = "")
	@RequestMapping(value = "/super/getProducts", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getProducts(
			@ApiParam(value = "{0:none，1:pc，2:web，3:ios, 4:android} {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getProducts(device);
		return rds;
	}

	@ApiOperation(value = "添加/修改商品", notes = "")
	@RequestMapping(value = "/super/addProducts", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet addProducts(@ApiParam(value = "指定要修改的商品id，新增商品时id写0") @RequestParam(value = "id") long id,
			@ApiParam(value = "虎币数x100,和数据库保持一致") @RequestParam(value = "iGold") long iGold,
			@ApiParam(value = "商品价格,精确到分") @RequestParam(value = "price") long price,
			@ApiParam(value = "商品id") @RequestParam(value = "productId") String productId,
			@ApiParam(value = "{0:none，1:pc，2:web，3:ios, 4:android} {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addProducts(id, iGold, price, productId, device);
		return rds;
	}

	@ApiOperation(value = "删除商品", notes = "")
	@RequestMapping(value = "/super/deleteProduct", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet deleteProduct(@ApiParam(value = "商品id，新增商品,id写0") @RequestParam(value = "id") long id)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteProduct(id);
		return rds;
	}

	@ApiOperation(value = "添加音效", notes = "")
	@RequestMapping(value = "/super/addSound", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet addSound(@ApiParam(value = "音效名称") @RequestParam(value = "name") String name,
			@ApiParam(value = "音效路径") @RequestParam(value = "path") String path) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.addSound(name, path);
		return rds;
	}

	@ApiOperation(value = "删除音效", notes = "")
	@RequestMapping(value = "/super/deleteSound", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet deleteSound(@ApiParam(value = "音效ID") @RequestParam(value = "guid") String guid)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteSound(guid);
		return rds;
	}

	@ApiOperation(value = "查询音效列表", notes = "")
	@RequestMapping(value = "/super/querySoundList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet querySoundList(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.querySoundList(pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查询同名音效", notes = "")
	@RequestMapping(value = "/super/getSound", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getSound(@ApiParam(value = "") @RequestParam(value = "guid") String guid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getSound(guid);
		return rds;
	}

	@ApiOperation(value = "获取推流配置", notes = "")
	@RequestMapping(value = "/super/getChannelPushConfig", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getChannelPushConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getChannelPushConfig();
		return rds;
	}
	
	
	@ApiOperation(value = "设置推流配置", notes = "")
	@RequestMapping(value = "/super/setChannelPushConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
	public ResultDataSet setChannelPushConfig(
			@ApiParam(value = "视频质量") @RequestParam(value = "quality") Integer quality,
			@ApiParam(value = "视频混合模式") @RequestParam(value = "mode") Integer mode,
			@ApiParam(value = "是否水印") @RequestParam(value = "logoMark") Integer logoMark) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.setChannelPushConfig(quality,mode,logoMark);
		return rds;
	}
	
	@ApiOperation(value = "添加游戏会员配置", notes = "")
	@RequestMapping(value = "/super/setGameVipConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet setGameVipConfig(
			@ApiParam(value = "会员等级") @RequestParam(value = "level") int level,
			@ApiParam(value = "购买时间") @RequestParam(value = "month") int month,
			@ApiParam(value = "价格") @RequestParam(value = "price") int price) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.setGameVipConfig(level, month, price);
		return rds;
	}
	
	@ApiOperation(value = "删除游戏会员配置", notes = "")
	@RequestMapping(value = "/super/deleteGameVipConfig", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet deleteGameVipConfig(
			@ApiParam(value = "会员等级") @RequestParam(value = "level") int level,
			@ApiParam(value = "购买时间") @RequestParam(value = "month") int month) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.deleteGameVipConfig(level, month);
		return rds;
	}
	
	@ApiOperation(value = "获取游戏会员配置", notes = "")
	@RequestMapping(value = "/super/getGameVipConfigs", method = RequestMethod.POST)
	@SuperAuthorization(SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet getGameVipConfigs() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = platformConfig.getGameVipConfigs();
		return rds;
	}
}
