package com.i5i58.service.pay;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.pay.IPay;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Web Restful Service for Account Authentication
 * 
 * @author cw
 *
 */

@Api(value = "充值-充值服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class PayRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPay pay;

	@ApiOperation(value = "生成充值订单", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/createOrder", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createOrder(@ApiParam(value = "平台ID") @RequestParam(value = "shareId") int shareId,
			@ApiParam(value = "目标AccId") @RequestParam(value = "toAccId") String toAccId,
			@ApiParam(value = "订单金额") @RequestParam(value = "orderAmount") long orderAmount,
			@ApiParam(value = "实付金额") @RequestParam(value = "payAmount") long payAmount,
			@ApiParam(value = "设备串码") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "设备") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();

		try {
			String clientIp = HttpUtils.getRealClientIpAddress();
			rds = pay.createOrder(HttpUtils.getAccIdFromHeader(), toAccId, shareId, orderAmount, payAmount, clientIp,
					serialNum, device);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "生成充值订单", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/createOrderForWxOffical", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createOrderForWxOffical(@ApiParam(value = "平台ID") @RequestParam(value = "shareId") int shareId,
			@ApiParam(value = "目标AccId") @RequestParam(value = "toAccId") String toAccId,
			@ApiParam(value = "订单金额") @RequestParam(value = "orderAmount") long orderAmount,
			@ApiParam(value = "实付金额") @RequestParam(value = "payAmount") long payAmount,
			@ApiParam(value = "设备串码") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "微信端openId") @RequestParam(value = "wxOpenId") String wxOpenId) {
		ResultDataSet rds = new ResultDataSet();

		try {
			String clientIp = HttpUtils.getRealClientIpAddress();
			rds = pay.createOrderForWxOffical(HttpUtils.getAccIdFromHeader(), toAccId, shareId, orderAmount, payAmount,
					clientIp, serialNum, device, wxOpenId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "充值回调", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/payCallBack", method = RequestMethod.POST)
	public ResultDataSet payCallBack(@ApiParam(value = "订单状态") @RequestParam(value = "state") int state,
			@ApiParam(value = "商户ID") @RequestParam(value = "customerId") String customerId,
			@ApiParam(value = "订单在网关的订单号") @RequestParam(value = "sd51no") String sd51no,
			@ApiParam(value = "商户订单号") @RequestParam(value = "sdcustomno") String sdcustomno,
			@ApiParam(value = "订单实际金额") @RequestParam(value = "orderMoney") long orderMoney,
			@ApiParam(value = "支付类型") @RequestParam(value = "cardno") String cardno,
			@ApiParam(value = "商户自定义信息") @RequestParam(value = "mark") String mark,
			@ApiParam(value = "签名") @RequestParam(value = "sign") String sign,
			@ApiParam(value = "支付备注") @RequestParam(value = "des") String des) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = pay.payCallBack(state, customerId, sd51no, sdcustomno, orderMoney, cardno, mark, sign, des,HttpUtils.getRealClientIpAddress());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取Apple Pay 货物列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/getApplePayList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getApplePayList(@ApiParam(value = "app版本号") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = pay.getApplePayList(HttpUtils.getAccIdFromHeader(), version);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "验证苹果票据有效性", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/setIapCertificate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setIapCertificate(@ApiParam(value = "平台订单ID") @RequestParam(value = "orderId") String orderId,
			@ApiParam(value = "苹果支付票据") @RequestParam(value = "receipt") String receipt,
			@ApiParam(value = "选择调用验证的方式") @RequestParam(value = "chooseEnv") Boolean chooseEnv) {
		ResultDataSet rds = new ResultDataSet();
		// rds = pay.setIapCertificate(HttpUtils.getAccIdFromHeader(), orderId,
		// receipt, chooseEnv);
		rds = pay.setIapCertificate(HttpUtils.getAccIdFromHeader(), orderId, receipt, true,HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "验证苹果票据有效性", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/verifyIapCertificate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet verifyIapCertificate(
			@ApiParam(value = "平台订单ID") @RequestParam(value = "orderId") String orderId,
			@ApiParam(value = "苹果支付票据") @RequestParam(value = "receipt") String receipt,
			@ApiParam(value = "设备号") @RequestParam(value = "device") int device,
			@ApiParam(value = "app版本号") @RequestParam(value = "version") String version) {
		ResultDataSet rds = new ResultDataSet();
		rds = pay.verifyIapCertificate(HttpUtils.getAccIdFromHeader(), orderId, receipt, device, version,HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "支付宝回调", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/aliPayCallback", method = RequestMethod.POST)
	public String aliPayCallback() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		ResultDataSet rds = new ResultDataSet();
		rds = pay.aliPayCallback(request.getParameterMap(),HttpUtils.getRealClientIpAddress());
		if (rds != null) {
			if (rds.getCode().equals("success")) {
				return rds.getData().toString();
			}
		}
		return "";
	}

	@ApiOperation(value = "微信支付回调", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/wxPayCallback", method = RequestMethod.POST)
	public String wxPayCallback() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		ResultDataSet rds = new ResultDataSet();
		String inputLine = "";
		String notityXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			logger.error("", e);
		}
		rds = pay.wxPayCallback(notityXml,HttpUtils.getRealClientIpAddress());
		if (rds != null) {
			if (rds.getCode().equals("success")) {
				return rds.getData().toString();
			}
		}
		return "";
	}

	@ApiOperation(value = "查询订单状态", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/queryOrderStatus", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryOrderStatus(@ApiParam(value = "平台订单ID") @RequestParam(value = "orderId") String orderId) {
		ResultDataSet rds = new ResultDataSet();
		rds = pay.queryOrderStatus(HttpUtils.getAccIdFromHeader(), orderId);
		return rds;
	}

	@ApiOperation(value = "获取订单", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/getOrderById", method = RequestMethod.GET)
	public ResultDataSet getOrderById(@ApiParam(value = "平台订单ID") @RequestParam(value = "orderId") String orderId) {
		ResultDataSet rds = new ResultDataSet();
		rds = pay.getOrderById(orderId);
		return rds;
	}

	@ApiOperation(value = "获取商品列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/getProducts", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getProducts(
			@ApiParam(value = "{0:none，1:pc，2:web，3:ios, 4:android} {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = pay.getProducts(device);
		return rds;
	}

	@ApiOperation(value = "验证支付宝网页支付同步结果", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/pay/verifyAlipayTradePagePayResult", method = RequestMethod.GET)
	@Authorization
	public ResultDataSet verifyAliPagePayResult() {
		ResultDataSet rds = new ResultDataSet();
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();

			rds = pay.verifyAlipayTradePagePayResult(request.getParameterMap());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rds;
	}
}
