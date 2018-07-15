
package com.i5i58.service.wetchat;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformWechat;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

	@Api(value = "平台微信服务")
	@RestController
	public class AccountWechatRestful {

		private Logger logger = Logger.getLogger(getClass());

		@Reference
		private IPlatformWechat platformWechat;

		@ApiOperation(value = "查询微信已绑定的用户列表", notes = "超管权限")
		@RequestMapping(value = "/super/queryBindedWechatAccount", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
		public ResultDataSet queryBindedWechatAccount(
				@RequestParam(value = "pageSize") int pageSize,
				@RequestParam(value = "pageNum") int pageNum) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.queryWechatAccount(pageSize,pageNum);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "查询已绑定用户", notes = "超管权限")
		@RequestMapping(value = "/super/getBindedWechatAccount", method = RequestMethod.GET)
		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
		public ResultDataSet getBindedWechatAccount(
				@ApiParam(value = "模糊匹配字符")@RequestParam(value = "param") String param,
				@ApiParam(value = "每页数量")@RequestParam(value = "pageSize") int pageSize,
				@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.getBindedWechatAccount(param,pageSize,pageNum);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "添加用户抽奖机会", notes = "超管权限")
		@RequestMapping(value = "/super/setLotteryChance", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
		public ResultDataSet setLotteryChance(@RequestParam(value = "accId")  String accId,
				@RequestParam(value = "lotteryCount") int lotteryCount) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.setLotteryChance(HttpUtils.getAccIdFromHeader(),accId,lotteryCount);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "删除抽奖机会", notes = "超管权限")
		@RequestMapping(value = "/super/deleteLotreryChance", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
		public ResultDataSet deleteLotreryChance(@RequestParam(value = "accId") String accId) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.deleteLotreryChance(HttpUtils.getAccIdFromHeader(),accId);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "查询用户抽奖的机会", notes = "超管权限")
		@RequestMapping(value = "/super/queryLotteryChance", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
		public ResultDataSet queryLotteryChance(@RequestParam(value = "accId") String accId) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.queryLotteryChance(accId);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}

		@ApiOperation(value = "增加奖品", notes = "超管权限")
		@RequestMapping(value = "/super/setAwardConfig", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
		public ResultDataSet setAwardConfig(@RequestParam(value = "id") int id,
				@RequestParam(value = "amount") long amount,
				@ApiParam(value="奖品类型：1欢乐豆,2虎币,3 钻石,4 继续加油,5 就差一点点")@RequestParam(value = "unit") int unit,
				@RequestParam(value = "decription") String decription,
				@RequestParam(value = "rate") int rate,
				@RequestParam(value = "url") String url,
				@RequestParam(value = "nullity") Boolean nullity) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.setAwardConfig(HttpUtils.getAccIdFromHeader(),id,amount,unit,decription,rate,url,nullity);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "删除奖品", notes = "超管权限")
		@RequestMapping(value = "/super/deleteAwardConfig", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.SUPER_ADMIN_CONTROL_AUTH)
		public ResultDataSet deleteAwardConfig(@RequestParam(value = "id") int id) {
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.deleteAwardConfig(HttpUtils.getAccIdFromHeader(),id);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		@ApiOperation(value = "查询奖品", notes = "超管权限")
		@RequestMapping(value = "/super/queryAwardConfig", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
		public ResultDataSet queryAwardConfig(@RequestParam(value = "pageSize") int pageSize,
				@RequestParam(value = "pageNum") int pageNum){
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.queryAwardConfig(pageSize,pageNum);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
		@ApiOperation(value = "查询用户抽奖记录表", notes = "超管权限")
		@RequestMapping(value = "/super/queryAwardOpeRecord", method = RequestMethod.POST)
		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
		public ResultDataSet queryAwardOpeRecord(@RequestParam(value = "accId") String accId){
			ResultDataSet rds = new ResultDataSet();
			try {
				rds = platformWechat.queryAwardOpeRecord(accId);
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			return rds;
		}
		
//		@ApiOperation(value = "添加用户抽奖记录", notes = "超管权限")
//		@RequestMapping(value = "/super/setAwardOpeRecord", method = RequestMethod.POST)
//		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
//		public ResultDataSet setAwardOpeRecord(@RequestParam(value = "id") int id,
//				@RequestParam(value = "accId") String  accId,
//				@RequestParam(value = "awardId")int  awardId,
//				@RequestParam(value = "amount")int  amount,
//				@ApiParam(value="奖品类型：0 欢乐豆,1 虎币,2 钻石")@RequestParam(value = "unit") int  unit,
//				@RequestParam(value = "rewardDateTime") long rewardDateTime,
//				@RequestParam(value = "deliveryDateTime") long deliveryDateTime){
//			ResultDataSet rds = new ResultDataSet();
//			try {
//				rds = platformWechat.setAwardOpeRecord(HttpUtils.getAccIdFromHeader(),id,accId,awardId,amount,unit,rewardDateTime,deliveryDateTime);
//			} catch (Exception e) {
//				logger.error("", e);
//				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
//				rds.setMsg("Service Error");
//			}
//			return rds;
//		}
		
//		
//		@ApiOperation(value = "删除用户抽奖记录", notes = "超管权限")
//		@RequestMapping(value = "/super/deleteAwardOpeRecord", method = RequestMethod.POST)
//		@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
//		public ResultDataSet deleteAwardOpeRecord(@RequestParam(value = "id") int id){
//			ResultDataSet rds = new ResultDataSet();
//			try {
//				rds = platformWechat.deleteAwardOpeRecord(HttpUtils.getAccIdFromHeader(),id);
//			} catch (Exception e) {
//				logger.error("", e);
//				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
//				rds.setMsg("Service Error");
//			}
//			return rds;
//		}
}
