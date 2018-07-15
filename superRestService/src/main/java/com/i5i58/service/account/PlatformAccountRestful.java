package com.i5i58.service.account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAccount;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "平台賬號管理服务")
@RestController
public class PlatformAccountRestful {

	@Reference
	private IPlatformAccount platformAccount;

	@ApiOperation(value = "查詢賬號列表", notes = "")
	@RequestMapping(value = "/super/queryAccountList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAccountList(@RequestParam(value = "param") String param,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountList(param, pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查詢賬號", notes = "")
	@RequestMapping(value = "/super/getAccountInfo", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAccountInfo(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.getAccountInfo(accId);
		return rds;
	}

	@ApiOperation(value = "查询用户订单", notes = "")
	@RequestMapping(value = "/super/queryOnlineOrder", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryOnlineOrder(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryOnlineOrder(accId, pageSize, pageNum);
		return rds;
	}
	
	@ApiOperation(value = "查询用户现金流向", notes = "")
	@RequestMapping(value = "/super/queryMoneyFlow", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryMoneyFlow(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryMoneyFlow(accId, pageSize, pageNum);
		return rds;
	}
	
	@ApiOperation(value = "查詢賬號充值", notes = "")
	@RequestMapping(value = "/super/queryAccountPay", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAccountPay(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountPay(accId, pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查詢賬號消费", notes = "")
	@RequestMapping(value = "/super/queryAccountConsume", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAccountConsume(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountConsume(accId, pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "开启/禁封账号", notes = "")
	@RequestMapping(value = "/super/enableAccount", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet enableAccount(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "nullity") boolean nullity, @RequestParam(value = "needKick") boolean needKick) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.enableAccount(HttpUtils.getAccIdFromHeader(), accId, nullity, needKick);
		return rds;
	}

	@ApiOperation(value = "回复申诉", notes = "")
	@RequestMapping(value = "/super/answerAppeal", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet answerAppeal(@RequestParam(value = "appealId") String appealId,
			@RequestParam(value = "agree") boolean agree, @RequestParam(value = "reason") String reason) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.answerAppeal(HttpUtils.getAccIdFromHeader(), appealId, agree, reason);
		return rds;
	}

	@ApiOperation(value = "更新礼物券数量", notes = "ope 0 增加; 1 减少; 2 重置")
	@RequestMapping(value = "/super/updateGiftTicket", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateGiftTicket(@ApiParam(value="被操作者") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value="操作类型 ：0 增加; 1 减少; 2 重置") @RequestParam(value = "ope") byte ope, 
			@ApiParam(value="操作类型增量,或设置的新数量，不能为负数") @RequestParam(value = "giftTickets") long giftTickets) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.updateGiftTicket(HttpUtils.getAccIdFromHeader(), targetAccId, ope, giftTickets, HttpUtils.getRealClientIpAddress());
		return rds;
	}
	

	@ApiOperation(value = "更新I币数量", notes = "ope 0 增加; 1 减少; 2 重置")
	@RequestMapping(value = "/super/updateIGold", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet updateIGold(@ApiParam(value="被操作者") @RequestParam(value = "targetAccId") String targetAccId,
			@ApiParam(value="操作类型 ：0 增加; 1 减少; 2 重置") @RequestParam(value = "ope") byte ope, 
			@ApiParam(value="操作类型增量,或设置的新数量，不能为负数") @RequestParam(value = "iGolds") long iGolds) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.updateIGold(HttpUtils.getAccIdFromHeader(), targetAccId, ope, iGolds);
		return rds;
	}
	
	@ApiOperation(value = "查询实名信息", notes = "")
	@RequestMapping(value = "/super/queryAccountAuthInfo", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet queryAccountAuthInfo(@ApiParam(value="被操作者") @RequestParam(value = "targetAccId") String targetAccId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountAuthInfo(HttpUtils.getAccIdFromHeader(), targetAccId);
		return rds;
	}
	
	@ApiOperation(value = "查询实名信息", notes = "")
	@RequestMapping(value = "/super/queryAccountGiftRecord", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet queryAccountGiftRecord(@ApiParam(value="查询对象") @RequestParam(value = "accId") String accId,
			@RequestParam(value = "pageSize") int pageSize, 
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountGiftRecord(accId, pageSize, pageNum);
		return rds;
	}
	
	@ApiOperation(value = "查询个人钱包", notes = "")
	@RequestMapping(value = "/super/queryAccountWallet", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet queryAccountWallet(@ApiParam(value="查询对象") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountWallet(accId);
		return rds;
	}
	
	@ApiOperation(value = "通知用户上传app log", notes = "")
	@RequestMapping(value = "/super/appLogRequirement", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet appLogRequirement(@ApiParam(value="通知对象") @RequestParam(value = "accId") String accId,
			@ApiParam(value="上传路径") @RequestParam(value = "bucketName") String bucketName) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.appLogRequirement(HttpUtils.getAccIdFromHeader(),	accId, bucketName);
		return rds;
	}
	
	@ApiOperation(value = "用户充值排行", notes = "")
	@RequestMapping(value = "/super/queryAccountPayList", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet queryAccountPayList(
			@ApiParam(value="未付款 0, 已付款待处理1, 处理完成2, 交易关闭3") @RequestParam(value = "status") int status,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAccount.queryAccountPayList(status, pageSize, pageNum);
		return rds;
	}
}
