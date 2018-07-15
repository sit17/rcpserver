package com.i5i58.service.config;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.config.IConfig;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "配置服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ConfigRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IConfig config;

	@ApiOperation(value = "检查/获取react native", notes = "")
	@RequestMapping(value = "/config/checkRn", method = RequestMethod.GET)
	public ResultDataSet checkRn(@ApiParam(value = "本地react native版本") @RequestParam(value = "version") String version,
			@ApiParam(value = "设备 ") @RequestParam(value = "device") int device,
			@ApiParam(value = "主版本") @RequestParam(value = "main") int main,
			@ApiParam(value = "功能版本") @RequestParam(value = "sub") int sub,
			@ApiParam(value = "主版本号 ") @RequestParam(value = "func") int func) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.checkRn(version, device, main, sub, func, "" + device + "_" + main + "_" + sub + "_" + func,
				version + "_" + device + "_" + main + "_" + sub + "_" + func);
		return rds;
	}

	@ApiOperation(value = "获取平臺配置", notes = "")
	@RequestMapping(value = "/config/getPlatformConfig", method = RequestMethod.GET)
	public ResultDataSet getPlatformConfig(@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "主版本") @RequestParam(value = "main") int main,
			@ApiParam(value = "次版本") @RequestParam(value = "sub") int sub,
			@ApiParam(value = "功能版本") @RequestParam(value = "func") int func) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getPlatformConfig(device, main, sub, func, "" + device + "_" + main + "_" + sub + "_" + func);
		return rds;
	}

	@ApiOperation(value = "获取Windows是否更新版本", notes = "")
	@RequestMapping(value = "/config/getWindowsUpdateConfig", method = RequestMethod.GET)
	public ResultDataSet getWindowsUpdateConfig(
			@ApiParam(value = "登陆程序版本") @RequestParam(value = "loginVersion") String loginVersion,
			@ApiParam(value = "json文件版本") @RequestParam(value = "jsonMd5") String jsonMd5) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsUpdateConfig(loginVersion, jsonMd5, loginVersion + "_" + jsonMd5);
		return rds;
	}

	@ApiOperation(value = "获取Windows是否更新版本", notes = "")
	@RequestMapping(value = "/config/getWindowsUpdateConfig1", method = RequestMethod.POST)
	public ResultDataSet getWindowsUpdateConfig1(
			@ApiParam(value = "登陆程序版本") @RequestParam(value = "loginVersion") String loginVersion,
			@ApiParam(value = "json文件版本") @RequestParam(value = "jsonMd5") String jsonMd5) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsUpdateConfig(loginVersion, jsonMd5, loginVersion + "_" + jsonMd5);
		return rds;
	}

	@ApiOperation(value = "获取Windows游戏是否更新版本", notes = "")
	@RequestMapping(value = "/config/getWindowsGameUpdateConfig", method = RequestMethod.GET)
	public ResultDataSet getWindowsGameUpdateConfig(
			@ApiParam(value = "gameJsonMd5文件版本") @RequestParam(value = "gameJsonMd5") String gameJsonMd5) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsGameUpdateConfig(gameJsonMd5);
		return rds;
	}

	@ApiOperation(value = "获取Windows游戏是否更新版本", notes = "")
	@RequestMapping(value = "/config/getWindowsBossConfig", method = RequestMethod.GET)
	public ResultDataSet getWindowsBossConfig(
			@ApiParam(value = "boss版本(代码硬编译)") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsBossConfig(version);
		return rds;
	}

	@ApiOperation(value = "获取Windows是否更新版本:拼写错误保留一段时间", notes = "")
	@RequestMapping(value = "/config/getWindosUpdateConfig", method = RequestMethod.GET)
	public ResultDataSet getWindosUpdateConfig_over(
			@ApiParam(value = "登陆程序版本") @RequestParam(value = "loginVersion") String loginVersion,
			@ApiParam(value = "json文件版本") @RequestParam(value = "jsonMd5") String jsonMd5) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsUpdateConfig(loginVersion, jsonMd5, loginVersion + "_" + jsonMd5);
		return rds;
	}

	@ApiOperation(value = "获取Windows游戏是否更新版本:拼写错误保留一段时间", notes = "")
	@RequestMapping(value = "/config/getWindosGameUpdateConfig", method = RequestMethod.GET)
	public ResultDataSet getWindowsGameUpdateConfig_over(
			@ApiParam(value = "gameJsonMd5文件版本") @RequestParam(value = "gameJsonMd5") String gameJsonMd5) {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getWindowsGameUpdateConfig(gameJsonMd5);
		return rds;
	}

	@ApiOperation(value = "获取OSS的访问配置", notes = "")
	@RequestMapping(value = "/config/getOssConfig", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getOssConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = config.getOSSConfig();
		return rds;
	}
}
