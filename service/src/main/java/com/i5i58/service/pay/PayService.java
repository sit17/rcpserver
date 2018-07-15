package com.i5i58.service.pay;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.pay.IPay;
import com.i5i58.config.MyThreadPool;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.config.AppVersionControl;
import com.i5i58.data.config.AppVersionStatus;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.pay.ApplePayItem;
import com.i5i58.data.pay.OnLineOrder;
import com.i5i58.data.pay.OrderShareType;
import com.i5i58.data.pay.OrderStatus;
import com.i5i58.data.pay.PayStatus;
import com.i5i58.data.pay.Product;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.record.RecordPay;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.primary.dao.pay.OnLineOrderPriDao;
import com.i5i58.primary.dao.pay.ProductPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.record.RecordPayPriDao;
import com.i5i58.secondary.dao.config.AppVersionControlSecDao;
import com.i5i58.service.pay.async.TaskYxPay;
import com.i5i58.service.pay.wxpay.HttpXmlUtils;
import com.i5i58.service.pay.wxpay.IPConvert;
import com.i5i58.service.pay.wxpay.ParseXMLUtils;
import com.i5i58.service.pay.wxpay.RandCharsUtils;
import com.i5i58.service.pay.wxpay.Unifiedorder;
import com.i5i58.service.pay.wxpay.WXSignUtils;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MountPresentUtil;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class PayService implements IPay {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	AccountPriDao accountDao;

	@Autowired
	OnLineOrderPriDao onLineOrderDao;

	@Autowired
	PlatformConfigPriDao platformConfigDao;

	@Autowired
	RecordPayPriDao recordPayDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	MountPresentUtil mountPresentUtil;

	@Autowired
	ConfigUtils configUtils;

	@Autowired
	ProductPriDao productDao;

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Autowired
	AppVersionControlSecDao appVersionControlSecDao;

	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;

	@Override
	public ResultDataSet createOrderForWxOffical(String accId, String toAccId, int shareId, long orderAmount,
			long payAmount, String ipAddress, String serialNum, int device, String wxOpenId) throws IOException {

		ResultDataSet rds = new ResultDataSet();
		Account account = accountDao.findOne(accId);
		Account toAccount = accountDao.findOne(toAccId);
		if (account == null || toAccount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (shareId != OrderShareType.PAY_WECHAT_JS.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("支付渠道异常:" + shareId);
			return rds;
		}
		HashMap<String, Object> response = new HashMap<>();
		long iGold = 0;
		long payRate = 100L;
		// if (shareId == OrderShareType.PAY_IOS.getValue()) {
		// try {
		// String payRateStr =
		// configUtils.getPlatformConfig(Constant.APPLE_PAY_RATE);
		// payRate = Long.valueOf(payRateStr);
		// } catch (Exception e) {
		// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		// rds.setMsg("系统配置异常");
		// return rds;
		// }
		// }
		iGold = orderAmount * payRate;
		String oderId = "";
		Date date = new Date();
		try {
			oderId = DateUtils.getTimeString(date) + DateUtils.getTime(date)
					+ String.valueOf((((long) accId.hashCode() + 3000000000L) / 100000));
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		OnLineOrder onLineOrder = new OnLineOrder();
		onLineOrder.setOrderId(oderId);
		onLineOrder.setAccId(accId);
		onLineOrder.setApplyDate(DateUtils.getNowTime());
		onLineOrder.setCompleteDate(DateUtils.getNowTime());
		onLineOrder.setDevice(device);
		onLineOrder.setDiscount(100);
		onLineOrder.setiGold(iGold);
		onLineOrder.setIpAddress(ipAddress);
		onLineOrder.setToAccId(toAccId);
		onLineOrder.setOrderAmount(orderAmount);
		onLineOrder.setOrderStatus(OrderStatus.ORDER_NO_PAY.getValue());
		onLineOrder.setPayAmount(payAmount);
		onLineOrder.setSerialNum(serialNum);
		onLineOrder.setShareId(shareId);
		onLineOrderDao.save(onLineOrder);
		response.put("order", onLineOrder);

		switch (OrderShareType.values()[shareId]) {
		case PAY_WECHAT_JS:
			try {
				createWXPayOrder(onLineOrder, response, wxOpenId);
			} catch (ParseException e) {
				logger.error(e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("parse error");
				return rds;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e);
				String errMsg = e.toString();
				if (errMsg == null || errMsg.isEmpty()) {
					errMsg = "unknown exception";
				}
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(errMsg);
				return rds;
			}
			break;
		default:
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("支付渠道异常:" + shareId);
			return rds;
		}

		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(response);
		return rds;
	}

	@Override
	public ResultDataSet createOrder(String accId, String toAccId, int shareId, long orderAmount, long payAmount,
			String ipAddress, String serialNum, int device) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountDao.findOne(accId);
		Account toAccount = accountDao.findOne(toAccId);
		if (account == null || toAccount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (shareId < 0 || shareId > 12) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("支付渠道异常:" + shareId);
			return rds;
		}
		HashMap<String, Object> response = new HashMap<>();
		long iGold = 0;
		long payRate = 100L;
		if (shareId == OrderShareType.PAY_IOS.getValue()) {
			try {
				String payRateStr = configUtils.getPlatformConfig(Constant.APPLE_PAY_RATE);
				payRate = Long.valueOf(payRateStr);
			} catch (Exception e) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("系统配置异常");
				return rds;
			}
		}
		iGold = orderAmount * payRate;
		String oderId = "";
		Date date = new Date();
		try {
			oderId = DateUtils.getTimeString(date) + DateUtils.getTime(date)
					+ String.valueOf((((long) accId.hashCode() + 3000000000L) / 100000));
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		OnLineOrder onLineOrder = new OnLineOrder();
		onLineOrder.setDiscount(100);
		onLineOrder.setOrderId(oderId);
		onLineOrder.setAccId(accId);
		onLineOrder.setApplyDate(DateUtils.getNowTime());
		onLineOrder.setCompleteDate(DateUtils.getNowTime());
		onLineOrder.setDevice(device);
		onLineOrder.setIpAddress(ipAddress);
		onLineOrder.setToAccId(toAccId);
		onLineOrder.setOrderAmount(orderAmount);
		onLineOrder.setOrderStatus(OrderStatus.ORDER_NO_PAY.getValue());
		onLineOrder.setPayAmount(payAmount);
		onLineOrder.setSerialNum(serialNum);
		onLineOrder.setShareId(shareId);

		NetBarAccount netBarAccount = netBarAccountPriDao.findByNetIp(ipAddress);
		if (netBarAccount != null && !netBarAccount.isNullity()) {
			double natBarRate = 0;
			try {
				String rateStr = configUtils.getPlatformConfig(Constant.NETBAR_PAY_PRESENT_RATE);
				natBarRate = Double.parseDouble(rateStr);
				if (natBarRate > 0) {
					iGold = (long) (iGold + iGold * natBarRate / 100);
				}
				onLineOrder.setDiscount(100 + Integer.parseInt(rateStr));
				onLineOrder.setNetBar(netBarAccount.getnId());
			} catch (Exception e1) {
				logger.error("", e1);
			}
		}

		onLineOrder.setiGold(iGold);
		onLineOrderDao.save(onLineOrder);
		response.put("order", onLineOrder);

		switch (OrderShareType.values()[shareId]) {
		case PAY_DUOBAOTONG_ALIPAY_APP:
		case PAY_DUOBAOTONG_BANK_APP:
		case PAY_DUOBAOTONG_WECHAT_APP:
		case PAY_DUOBAOTONG_ALIPAY_WEB:
		case PAY_DUOBAOTONG_BANK_WEB:
		case PAY_DUOBAOTONG_WECHAT_WEB:

			break;
		case PAY_IOS:

			break;
		case PAY_ALIPAY_APP:
			try {
				String orderString = createAliPayOrder(onLineOrder);
				response.put("orderString", orderString);
			} catch (AlipayApiException e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("支付宝创建app订单异常");
				return rds;
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(e.getMessage());
				return rds;
			}
			break;
		case PAY_ALIPAY_WEB:
			try {
				ResponseData rpAliPay = createAlipayPageOrder(onLineOrder);
				response.put("alipay", rpAliPay);
			} catch (AlipayApiException e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("支付宝创建网页订单异常");
				return rds;
			} catch (Exception e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(e.getMessage());
				return rds;
			}
			break;
		case PAY_WECHAT_APP:
		case PAY_WECHAT_WEB:
			try {
				createWXPayOrder(onLineOrder, response, null);
			} catch (ParseException e) {
				logger.error(e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("parse error");
				return rds;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e);
				String errMsg = e.toString();
				if (errMsg == null || errMsg.isEmpty()) {
					errMsg = "unknown exception";
				}
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(errMsg);
				return rds;
			}
			break;
		case PAY_BANK_APP:
		case PAY_BANK_WEB:

			break;
		default:
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("支付渠道异常:" + shareId);
			return rds;
		}
		if (OrderShareType.values()[shareId] == OrderShareType.PAY_WECHAT_WEB) {
			response.remove("wxpay");
			response.remove("order");
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(response);
		return rds;
	}

	private String getWxChatAppId(int device, int shareId) throws Exception {
		if (DeviceCode.isGame(device)) {
			return configUtils.getPlatformConfig(Constant.GAME_WECHAT_APP_ID);
		} else {
			if (shareId == OrderShareType.PAY_WECHAT_JS.getValue()) {
				return configUtils.getPlatformConfig(Constant.OFFICIAL_WECHAT_APP_ID);
			} else {
				return configUtils.getPlatformConfig(Constant.WECHAT_APP_ID);
			}
		}
	}

	private String getWxChatMerchantId(int device, int shareId) throws Exception {
		if (DeviceCode.isGame(device)) {
			return configUtils.getPlatformConfig(Constant.GAME_WECHAT_MERCHANT_ID);
		} else {
			if (shareId == OrderShareType.PAY_WECHAT_JS.getValue()) {
				return configUtils.getPlatformConfig(Constant.OFFICIAL_WECHAT_MERCHANT_ID);
			} else {
				return configUtils.getPlatformConfig(Constant.WECHAT_MERCHANT_ID);
			}
		}
	}

	private String getWxChatApiKey(int device, int shareId) throws Exception {
		if (DeviceCode.isGame(device)) {
			return configUtils.getPlatformConfig(Constant.GAME_WECHAT_API_KEY);
		} else {
			if (shareId == OrderShareType.PAY_WECHAT_JS.getValue()) {
				return configUtils.getPlatformConfig(Constant.OFFICIAL_WECHAT_API_KEY);
			} else {
				return configUtils.getPlatformConfig(Constant.WECHAT_API_KEY);
			}
		}
	}

	private String createAliPayOrder(OnLineOrder onLineOrder) throws Exception {
		String aliPayCallbackUrl;
		aliPayCallbackUrl = configUtils.getPlatformConfig(Constant.ALI_PAY_CALLBACK_URL);

		String aliPayAppId;
		aliPayAppId = configUtils.getPlatformConfig(Constant.ALI_PAY_APPID);

		String aliPay_app_private_key;
		aliPay_app_private_key = configUtils.getPlatformConfig(Constant.ALI_PAY_APP_PRIVATE_KEY);

		String aliPay_public_key;
		aliPay_public_key = configUtils.getPlatformConfig(Constant.ALI_PAY_PUBLIC_KEY);

		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliPayAppId,
				aliPay_app_private_key, "json", "utf-8", aliPay_public_key, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("虎币充值");
		model.setSubject("胖虎娱乐");
		model.setOutTradeNo(onLineOrder.getOrderId());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(Long.toString(onLineOrder.getOrderAmount()));
		// model.setTotalAmount("0.01");
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(aliPayCallbackUrl);

		// 这里和普通的接口调用不同，使用的是sdkExecute
		AlipayTradeAppPayResponse payResponse = alipayClient.sdkExecute(request);
		String orderString = payResponse.getBody();
		// System.out.println(orderString);// 就是orderString,可以直接给客户端请求，无需再做处理。
		// response.put("orderString", orderString);
		return orderString;
	}

	private ResponseData createAlipayPageOrder(OnLineOrder onLineOrder) throws Exception {
		ResponseData rp = new ResponseData();
		String aliPayCallbackUrl;
		aliPayCallbackUrl = configUtils.getPlatformConfig(Constant.ALI_PAY_CALLBACK_URL);

		// String aliPayReturnUrl =
		// configUtils.getPlatformConfig(Constant.ALI_PAY_RETURN_RUL);

		String aliPayAppId;
		aliPayAppId = configUtils.getPlatformConfig(Constant.ALI_PAY_APPID);

		String aliPay_app_private_key;
		aliPay_app_private_key = configUtils.getPlatformConfig(Constant.ALI_PAY_APP_PRIVATE_KEY);

		String aliPay_public_key;
		aliPay_public_key = configUtils.getPlatformConfig(Constant.ALI_PAY_PUBLIC_KEY);

		rp.put("callback_url", aliPayCallbackUrl);
		rp.put("app_id", aliPayAppId);
		rp.put("private_key", aliPay_app_private_key);
		rp.put("public_key", aliPay_public_key);

		return rp;
	}

	private void createWXPayOrder(OnLineOrder onLineOrder, HashMap<String, Object> response, String wxOpenId)
			throws Exception {
		ResponseData rpWx = new ResponseData();

		String wxPayAppId = getWxChatAppId(onLineOrder.getDevice(), onLineOrder.getShareId());
		String wxPayMCHId = getWxChatMerchantId(onLineOrder.getDevice(), onLineOrder.getShareId());
		String wxApiKey = getWxChatApiKey(onLineOrder.getDevice(), onLineOrder.getShareId());

		int payMoneyFen = (int) (onLineOrder.getOrderAmount() * 100);
		SortedMap<Object, Object> orderResult = weiChatUnifiedorder(wxPayAppId, wxPayMCHId, wxApiKey,
				onLineOrder.getOrderId(), payMoneyFen, onLineOrder.getIpAddress(), onLineOrder.getShareId(), wxOpenId);
		if (orderResult == null) {
			logger.error("creat wxchat unified order failed.");
			Exception exception = new Exception("微信支付创建订单异常");
			throw exception;
		}

		String prePayId, noncestr;
		noncestr = StringUtils.createUUID();
		if (!orderResult.get("return_code").equals("SUCCESS")) {
			logger.error("return_msg : " + orderResult.get("return_msg"));
			Exception exception = new Exception("微信支付创建订单异常");
			throw exception;
		}
		if (!orderResult.get("result_code").equals("SUCCESS")) {
			logger.error("err_code : " + orderResult.get("err_code"));
			logger.error("err_code_des : " + orderResult.get("err_code_des"));
			Exception exception = new Exception("微信支付创建订单异常");
			throw exception;
		}
		prePayId = (String) orderResult.get("prepay_id");
		if (prePayId == null) {
			logger.error("wechat pay orderResult : " + orderResult.toString());
			Exception exception = new Exception("微信支付创建订单异常");
			throw exception;
		}
		Object object = orderResult.get("code_url");
		if (object != null) {
			String code_url = object.toString();
			response.put("code_url", code_url);
		}
		long nowSeconds = DateUtils.getNowDate(Locale.CHINA) / 1000;

		if (onLineOrder.getShareId() == OrderShareType.PAY_WECHAT_JS.getValue()) {
			System.out.println("appId = " + wxPayAppId);
			System.out.println("api key = " + wxApiKey);
			System.out.println("wxPayMCHId = " + wxPayMCHId);
			rpWx.put("appId", wxPayAppId);
			rpWx.put("signType", "MD5");
			rpWx.put("package", "prepay_id=" + prePayId);
			rpWx.put("nonceStr", noncestr);
			rpWx.put("timeStamp", ((Long) nowSeconds).toString());

			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appId", wxPayAppId);
			parameters.put("signType", "MD5");
			parameters.put("package", "prepay_id=" + prePayId);
			parameters.put("nonceStr", noncestr);
			parameters.put("timeStamp", ((Long) nowSeconds).toString());

			String sign = WXSignUtils.createSign("UTF-8", parameters, wxApiKey);
			rpWx.put("paySign", sign);
		} else {
			rpWx.put("appid", wxPayAppId);
			rpWx.put("partnerid", wxPayMCHId);
			rpWx.put("prepayid", prePayId);
			rpWx.put("package", "Sign=WXPay");
			rpWx.put("noncestr", noncestr);
			rpWx.put("timestamp", nowSeconds);

			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", wxPayAppId);
			parameters.put("partnerid", wxPayMCHId);
			parameters.put("prepayid", prePayId);
			parameters.put("package", "Sign=WXPay");
			parameters.put("timestamp", nowSeconds);
			parameters.put("noncestr", noncestr);

			String sign = WXSignUtils.createSign("UTF-8", parameters, wxApiKey);
			rpWx.put("sign", sign);
		}

		response.put("wxpay", rpWx);
	}

	// ==========================alipay===========================
	/**
	 * 支付宝回调
	 */
	@Override
	@Transactional
	public ResultDataSet aliPayCallback(Map<String, String[]> requestParams, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		StringBuffer paramsStr = new StringBuffer();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			paramsStr.append(name);
			paramsStr.append(":");
			paramsStr.append(valueStr);
			paramsStr.append(", ");
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean flag = false;
		String aliPay_public_key;
		try {
			aliPay_public_key = configUtils.getPlatformConfig(Constant.ALI_PAY_PUBLIC_KEY);
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(e.getMessage());
			return rds;
		}
		try {
			flag = AlipaySignature.rsaCheckV1(params, aliPay_public_key, "utf-8", "RSA2");
		} catch (AlipayApiException e) {
			logger.error(paramsStr.toString(), e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!flag) {
			logger.error(paramsStr.toString());
			rds.setData("failure");
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}

		OnLineOrder onLineOrder = onLineOrderDao.findOne(params.get("out_trade_no"));

		if (onLineOrder == null) {
			logger.error("订单不存在:" + paramsStr.toString());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (onLineOrder.getOrderStatus() != OrderStatus.ORDER_NO_PAY.getValue()) {
			logger.error("订单状态错误:" + paramsStr.toString());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		int status = 0;
		String statusStr = params.get("trade_status");
		switch (statusStr) {
		case "TRADE_FINISHED":
		case "TRADE_SUCCESS":
			status = PayStatus.PAY_STATUS_SUCCESS.getValue();
			break;
		case "TRADE_CLOSED":
			status = PayStatus.PAY_STATUS_CLOSED.getValue();
			break;
		}

		onLineOrder.setOrderStatus(OrderStatus.ORDER_COMPLETE_PAY.getValue());
		onLineOrder.setCompleteDate(DateUtils.getNowTime());
		onLineOrderDao.save(onLineOrder);

		// 首冲判断
		Pageable pageable = new PageRequest(0, 1);
		Page<RecordPay> prePayRecords = recordPayDao.findByAccId(onLineOrder.getAccId(), pageable);
		if (prePayRecords == null || prePayRecords.getSize() == 0) {
			mountPresentUtil.presentMountForPay(onLineOrder.getAccId());
		}
		long dateTime = DateUtils.getNowTime();
		RecordPay recordPay = new RecordPay();
		recordPay.setOrderId(onLineOrder.getOrderId());
		recordPay.setAccId(onLineOrder.getAccId());
		recordPay.setAmount(onLineOrder.getPayAmount());
		recordPay.setCreateTime(dateTime);
		recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
		recordPay.setId(StringUtils.createUUID());
		recordPay.setStatus(status);
		recordPay.setStatusDescribe(
				status == 1 ? PayStatus.PAY_STATUS_SUCCESS.getCode() : PayStatus.PAY_STATUS_FAILED.getCode());
		recordPay.setTarget(onLineOrder.getAccId());
		recordPayDao.save(recordPay);

		Wallet wallet = walletPriDao.findByAccId(onLineOrder.getAccId());
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(onLineOrder.getAccId());
		moneyFlow.setDateTime(dateTime);
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.Recharge, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);

		walletPriDao.updateIGold(onLineOrder.getAccId(), onLineOrder.getiGold());

		moneyFlow.setTargetIGold(wallet.getiGold() + onLineOrder.getiGold());
		moneyFlowPriDao.save(moneyFlow);

		TaskYxPay taskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getAccId(), onLineOrder.getAccId(),
				onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(), onLineOrder.getDevice(),
				onLineOrder.getDiscount(), onLineOrder.getiGold(), onLineOrder.getIpAddress(),
				onLineOrder.getOrderAmount(), onLineOrder.getOrderId(), onLineOrder.getOrderStatus(),
				onLineOrder.getPayAmount(), onLineOrder.getSerialNum(), onLineOrder.getShareId(),
				onLineOrder.getToAccId());
		myThreadPool.getYunxinPool().execute(taskYxPay);
		if (!onLineOrder.getAccId().equals(onLineOrder.getToAccId())) {
			TaskYxPay toTaskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getToAccId(),
					onLineOrder.getToAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
					onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
					onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
					onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
					onLineOrder.getShareId(), onLineOrder.getToAccId());
			myThreadPool.getYunxinPool().execute(toTaskYxPay);
		}
		rds.setData("success");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet wxPayCallback(String params, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		if (StringUtils.StringIsEmptyOrNull(params) || params.length() < 5) {
			String res = getWxPayResult("FAIL", "参数格式校验错误");
			rds.setData(res);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		if (params.startsWith("null")) {
			params = params.substring(4);
		}
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		try {
			document = saxBuilder.build(new StringReader(params));
		} catch (JDOMException e1) {
			logger.error(params, e1);
			String res = getWxPayResult("FAIL", "参数格式校验错误");
			rds.setData(res);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (IOException e1) {
			logger.error(params, e1);
			String res = getWxPayResult("FAIL", "参数格式校验错误");
			rds.setData(res);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		String weSign = "";
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<?> list = root.getChildren();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = e.getValue();
			if (k.equals("sign")) {
				weSign = v;
			} else {
				parameters.put(k, v);
			}
		}

		OnLineOrder onLineOrder = onLineOrderDao.findOne(parameters.get("out_trade_no").toString());

		if (onLineOrder == null) {
			logger.error("订单不存在:" + params);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		if (onLineOrder.getOrderStatus() != OrderStatus.ORDER_NO_PAY.getValue()) {
			logger.error("订单状态错误:" + params);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		onLineOrder.setOrderStatus(OrderStatus.ORDER_COMPLETE_PAY.getValue());
		onLineOrder.setCompleteDate(DateUtils.getNowTime());
		onLineOrderDao.save(onLineOrder);

		if (!parameters.get("return_code").toString().equals("SUCCESS")) {
			logger.error("充值失败: " + params);

			RecordPay recordPay = new RecordPay();
			recordPay.setOrderId(onLineOrder.getOrderId());
			recordPay.setAccId(onLineOrder.getAccId());
			recordPay.setAmount(onLineOrder.getPayAmount());
			recordPay.setCreateTime(DateUtils.getNowTime());
			recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
			recordPay.setId(StringUtils.createUUID());
			recordPay.setStatus(PayStatus.PAY_STATUS_FAILED.getValue());
			recordPay.setStatusDescribe(PayStatus.PAY_STATUS_FAILED.getCode());
			recordPay.setTarget(onLineOrder.getAccId());
			recordPayDao.save(recordPay);

			String res = getWxPayResult("FAIL", parameters.get("return_msg").toString());
			rds.setData(res);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}

		String wxApiKey;
		try {
			wxApiKey = getWxChatApiKey(onLineOrder.getDevice(), onLineOrder.getShareId());
		} catch (Exception e) {
			logger.error(e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("api key 不存在, device = " + onLineOrder.getDevice());
			return rds;
		}

		String sign = WXSignUtils.createSign("UTF-8", parameters, wxApiKey);
		if (!sign.equals(weSign)) {
			logger.error("签名失败: " + params);
			String res = getWxPayResult("FAIL", "签名失败");
			rds.setData(res);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}

		// 首冲判断
		Pageable pageable = new PageRequest(0, 1);
		Page<RecordPay> prePayRecords = recordPayDao.findByAccId(onLineOrder.getAccId(), pageable);
		if (prePayRecords == null || prePayRecords.getSize() == 0) {
			mountPresentUtil.presentMountForPay(onLineOrder.getAccId());
		}

		long dateTime = DateUtils.getNowTime();
		RecordPay recordPay = new RecordPay();
		recordPay.setOrderId(onLineOrder.getOrderId());
		recordPay.setAccId(onLineOrder.getAccId());
		recordPay.setAmount(onLineOrder.getPayAmount());
		recordPay.setCreateTime(dateTime);
		recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
		recordPay.setId(StringUtils.createUUID());
		recordPay.setStatus(PayStatus.PAY_STATUS_SUCCESS.getValue());
		recordPay.setStatusDescribe(PayStatus.PAY_STATUS_SUCCESS.getCode());
		recordPay.setTarget(onLineOrder.getAccId());
		recordPayDao.save(recordPay);

		Wallet wallet = walletPriDao.findByAccId(onLineOrder.getAccId());
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}

		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(onLineOrder.getAccId());
		moneyFlow.setDateTime(dateTime);
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.Recharge, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);

		walletPriDao.updateIGold(onLineOrder.getAccId(), onLineOrder.getiGold());

		moneyFlow.setTargetIGold(wallet.getiGold() + onLineOrder.getiGold());
		moneyFlowPriDao.save(moneyFlow);

		TaskYxPay taskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getAccId(), onLineOrder.getAccId(),
				onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(), onLineOrder.getDevice(),
				onLineOrder.getDiscount(), onLineOrder.getiGold(), onLineOrder.getIpAddress(),
				onLineOrder.getOrderAmount(), onLineOrder.getOrderId(), onLineOrder.getOrderStatus(),
				onLineOrder.getPayAmount(), onLineOrder.getSerialNum(), onLineOrder.getShareId(),
				onLineOrder.getToAccId());
		myThreadPool.getYunxinPool().execute(taskYxPay);
		if (!onLineOrder.getAccId().equals(onLineOrder.getToAccId())) {
			TaskYxPay toTaskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getToAccId(),
					onLineOrder.getToAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
					onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
					onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
					onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
					onLineOrder.getShareId(), onLineOrder.getToAccId());
			myThreadPool.getYunxinPool().execute(toTaskYxPay);
		}

		String res = getWxPayResult("SUCCESS", "OK");
		rds.setData(res);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	private String getWxPayResult(String code, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA[" + code + "]]></return_code>");
		sb.append("<return_msg><![CDATA[" + msg + "]]></return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 创建微信支付订单
	 */
	private SortedMap<Object, Object> weiChatUnifiedorder(String wxPayAppId, String wxPayMCHId, String wxApiKey,
			String orderId, int moneyFen, String clientIp, int shareId, String wxOpenId) {
		SortedMap<Object, Object> orderResult = null;

		String wxPayCallbackUrl; // = "http://localhost:6970/pay/wxPayCallback";
		try {
			wxPayCallbackUrl = configUtils.getPlatformConfig(Constant.WECHAT_PAY_CALLBACK_URL);
		} catch (Exception e) {
			logger.error("没有配置微信支付回调地址：", e);
			return orderResult;
			// wxPayCallbackUrl = "http://localhost:6970/pay/wxPayCallback";
		}

		String appid = wxPayAppId;
		String mch_id = wxPayMCHId;
		String nonce_str = StringUtils.createUUID();
		String body = "虎币充值";
		// String detail = "{\"goods_id\":\"iphone6s_16G\"}";
		// String attach = "attach_sample";
		String out_trade_no = orderId; // "2015112500001000811017342394";
		int total_fee = moneyFen;// 单位是分，即是0.01元
		// int total_fee = 1;// 单位是分，即是0.01元
		String spbill_create_ip = IPConvert.ConvertToIpV4(clientIp); // 如果是Ipv4直接返回，否则，转换成ipv4
		String time_start = RandCharsUtils.timeStart();
		String time_expire = RandCharsUtils.timeExpire();
		String notify_url = wxPayCallbackUrl;
		String trade_type = "APP";
		// 扫码支付
		if (OrderShareType.values()[shareId] == OrderShareType.PAY_WECHAT_WEB) {
			trade_type = "NATIVE";
		} else if (OrderShareType.values()[shareId] == OrderShareType.PAY_WECHAT_JS) {
			trade_type = "JSAPI";
		}

		// ipv6转ipv4

		// 参数：开始生成签名
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", appid);
		parameters.put("mch_id", mch_id);
		parameters.put("nonce_str", nonce_str);
		parameters.put("body", body);
		// parameters.put("detail", detail);
		// parameters.put("attach", attach);
		parameters.put("out_trade_no", out_trade_no);
		parameters.put("total_fee", total_fee);
		parameters.put("time_start", time_start);
		parameters.put("time_expire", time_expire);
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", trade_type);
		parameters.put("spbill_create_ip", spbill_create_ip);
		if (!StringUtils.StringIsEmptyOrNull(wxOpenId)) {
			parameters.put("openid", wxOpenId);
		}

		String sign = WXSignUtils.createSign("UTF-8", parameters, wxApiKey);
		System.out.println("签名是：" + sign);

		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(appid);
		unifiedorder.setMch_id(mch_id);
		unifiedorder.setNonce_str(nonce_str);
		unifiedorder.setSign(sign);
		unifiedorder.setBody(body);
		// unifiedorder.setDetail(detail);
		// unifiedorder.setAttach(attach);
		unifiedorder.setOut_trade_no(out_trade_no);
		unifiedorder.setTotal_fee(total_fee);
		unifiedorder.setSpbill_create_ip(spbill_create_ip);
		unifiedorder.setTime_start(time_start);
		unifiedorder.setTime_expire(time_expire);
		unifiedorder.setNotify_url(notify_url);
		unifiedorder.setTrade_type(trade_type);
		unifiedorder.setOpenid(wxOpenId);

		// 构造xml参数
		String xmlInfo = HttpXmlUtils.xmlInfo(unifiedorder);
		System.out.println("xmlInfo : " + xmlInfo);
		String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String method = "POST";
		String weixinPost = HttpXmlUtils.httpsRequest(wxUrl, method, xmlInfo).toString();
		System.out.println(weixinPost);
		orderResult = ParseXMLUtils.jdomParseXml(weixinPost);
		if (orderResult == null) {
			logger.error("wxchat unifiedorder call failed");
			return orderResult;
		}
		if (orderResult.get("sign") == null) {
			logger.error("微信订单没有签名字段，订单结果是: " + orderResult.toString());
			return null;
		}
		String signReturn = orderResult.get("sign").toString();
		String signChecked = WXSignUtils.createSign("UTF-8", orderResult, wxApiKey);
		System.out.println("返回值的签名为：" + signReturn);
		System.out.println("重新计算返回值的签名为：" + signChecked);
		if (!signChecked.equals(signReturn)) {
			orderResult = null;
			logger.error("订单签名不一致, xmlInfo = " + xmlInfo);
			logger.error("订单签名不一致, weixinPost = " + weixinPost);
			return orderResult;
		}
		return orderResult;
	}

	@Override
	@Transactional
	public ResultDataSet payCallBack(int state, String customerId, String sd51no, String sdcustomno, long orderMoney,
			String cardno, String mark, String sign, String des, String clientIp) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		OnLineOrder onLineOrder = onLineOrderDao.findOne(sdcustomno);
		if (onLineOrder != null) {
			if (onLineOrder.getOrderStatus() == 1) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				return rds;
			} else {
				onLineOrder.setOrderStatus(OrderStatus.ORDER_COMPLETE_PAY.getValue());
				onLineOrder.setCompleteDate(DateUtils.getNowTime());
				onLineOrderDao.save(onLineOrder);

				// 首冲判断
				Pageable pageable = new PageRequest(0, 1);
				Page<RecordPay> prePayRecords = recordPayDao.findByAccId(onLineOrder.getAccId(), pageable);
				if (prePayRecords == null || prePayRecords.getSize() == 0) {
					mountPresentUtil.presentMountForPay(onLineOrder.getAccId());
				}

				long dateTime = DateUtils.getNowTime();
				RecordPay recordPay = new RecordPay();
				recordPay.setOrderId(onLineOrder.getOrderId());
				recordPay.setAccId(onLineOrder.getAccId());
				recordPay.setAmount(onLineOrder.getPayAmount());
				recordPay.setCreateTime(dateTime);
				recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
				recordPay.setId(StringUtils.createUUID());
				recordPay.setStatus(
						state == 1 ? PayStatus.PAY_STATUS_SUCCESS.getValue() : PayStatus.PAY_STATUS_FAILED.getValue());
				recordPay.setStatusDescribe(
						state == 1 ? PayStatus.PAY_STATUS_SUCCESS.getCode() : PayStatus.PAY_STATUS_FAILED.getCode());
				recordPay.setTarget(onLineOrder.getAccId());
				recordPay.setCardNo(cardno);
				recordPayDao.save(recordPay);

				Wallet wallet = walletPriDao.findByAccId(onLineOrder.getAccId());
				if (wallet == null) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg(ServerCode.NO_WALLET.getCode());
					return rds;
				}

				MoneyFlow moneyFlow = new MoneyFlow();
				moneyFlow.setAccId(onLineOrder.getAccId());
				moneyFlow.setDateTime(dateTime);
				moneyFlow.setIpAddress(clientIp);
				HelperFunctions.setMoneyFlowType(MoneyFlowType.Recharge, moneyFlow);
				HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
				HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);

				walletPriDao.updateIGold(onLineOrder.getAccId(), onLineOrder.getiGold());

				moneyFlow.setTargetIGold(wallet.getiGold() + onLineOrder.getiGold());
				moneyFlowPriDao.save(moneyFlow);

				TaskYxPay taskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getAccId(),
						onLineOrder.getAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
						onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
						onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
						onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
						onLineOrder.getShareId(), onLineOrder.getToAccId());
				myThreadPool.getYunxinPool().execute(taskYxPay);
				if (!onLineOrder.getAccId().equals(onLineOrder.getToAccId())) {
					TaskYxPay toTaskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getToAccId(),
							onLineOrder.getToAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
							onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
							onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
							onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
							onLineOrder.getShareId(), onLineOrder.getToAccId());
					myThreadPool.getYunxinPool().execute(toTaskYxPay);
				}
			}
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	// ==============apple================================
	@Override
	public ResultDataSet getApplePayList(String accId, String version) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<ApplePayItem> items = new ArrayList<ApplePayItem>();
		AppVersionControl vc = appVersionControlSecDao.findByStatusAndDevice(AppVersionStatus.EXAMINE.getValue(), 3);
		if (vc != null) {
			String ver = vc.getMainVserion() + "." + vc.getSubVersion() + "." + vc.getFuncVersion();
			// 测试版用沙盒
			if (ver.equals(version)) {
				ApplePayItem item0 = new ApplePayItem();
				item0.setName("6元=4.2G币");
				item0.setId("com.i5i58.live.00006");
				item0.setType("消费型项目");
				items.add(item0);

				ApplePayItem item1 = new ApplePayItem();
				item1.setName("18元=12.6G币");
				item1.setId("com.i5i58.live.00018");
				item1.setType("消费型项目");
				items.add(item1);

				ApplePayItem item2 = new ApplePayItem();
				item2.setName("50元=35G币");
				item2.setId("com.i5i58.live.00050");
				item2.setType("消费型项目");
				items.add(item2);

				ApplePayItem item3 = new ApplePayItem();
				item3.setName("128元=89.6G币");
				item3.setId("com.i5i58.live.00128");
				item3.setType("消费型项目");
				items.add(item3);

				ApplePayItem item4 = new ApplePayItem();
				item4.setName("258元=180.6G币");
				item4.setId("com.i5i58.live.00258");
				item4.setType("消费型项目");
				items.add(item4);

				ApplePayItem item5 = new ApplePayItem();
				item5.setName("518元=362.6G币");
				item5.setId("com.i5i58.live.00518");
				item5.setType("消费型项目");
				items.add(item5);
			}
		}
		// List<RecordPay> recordPay =
		// recordPayDao.findByFromSourceAndStatusAndAccId(OrderShareType.PAY_IOS.getCode(),
		// 1,
		// accId);
		// if (recordPay == null || recordPay.size() == 0) {
		// ApplePayItem item0 = new ApplePayItem();
		// item0.setName("6元=4.2G币");
		// item0.setId("com.i5i58.live.00006");
		// item0.setType("消费型项目");
		// items.add(item0);
		// rds.setData(items);
		// rds.setCode(ResultCode.SUCCESS.getCode());
		// return rds;
		// }
		// ApplePayItem item0 = new ApplePayItem();
		// item0.setName("6元=4.2G币");
		// item0.setId("com.i5i58.live.00006");
		// item0.setType("消费型项目");
		// items.add(item0);
		//
		// ApplePayItem item1 = new ApplePayItem();
		// item1.setName("18元=12.6G币");
		// item1.setId("com.i5i58.live.00018");
		// item1.setType("消费型项目");
		// items.add(item1);
		//
		// ApplePayItem item2 = new ApplePayItem();
		// item2.setName("50元=35G币");
		// item2.setId("com.i5i58.live.00050");
		// item2.setType("消费型项目");
		// items.add(item2);
		//
		// ApplePayItem item3 = new ApplePayItem();
		// item3.setName("128元=89.6G币");
		// item3.setId("com.i5i58.live.00128");
		// item3.setType("消费型项目");
		// items.add(item3);
		//
		// ApplePayItem item4 = new ApplePayItem();
		// item4.setName("258元=180.6G币");
		// item4.setId("com.i5i58.live.00258");
		// item4.setType("消费型项目");
		// items.add(item4);
		//
		// ApplePayItem item5 = new ApplePayItem();
		// item5.setName("518元=362.6G币");
		// item5.setId("com.i5i58.live.00518");
		// item5.setType("消费型项目");
		// items.add(item5);

		rds.setData(items);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	// 购买凭证验证地址
	private static final String certificateUrl = "https://buy.itunes.apple.com/verifyReceipt";

	// 测试的购买凭证验证地址
	private static final String certificateUrlTest = "https://sandbox.itunes.apple.com/verifyReceipt";

	/**
	 * 重写X509TrustManager
	 */
	private static TrustManager myX509TrustManager = new X509TrustManager() {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}
	};

	@Override
	public ResultDataSet verifyIapCertificate(String accId, String orderId, String receipt, int device, String version,
			String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		// if (device != DeviceCode.IOSGame){
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("该设备不支持此接口");
		// return rds;
		// }
		AppVersionControl vc = appVersionControlSecDao.findByStatusAndDevice(AppVersionStatus.EXAMINE.getValue(),
				device);

		// 默认验证正式订单
		boolean chooseEnv = true;
		if (vc != null) {
			String ver = vc.getMainVserion() + "." + vc.getSubVersion() + "." + vc.getFuncVersion();
			// 测试版用沙盒
			if (ver.equals(version)) {
				chooseEnv = false;
			}
		}
		if (chooseEnv) {// 正式iap支付
			if (device == 3) {// ios直播不让付，防虚假充值
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("数据异常");
				return rds;
			}
		}
		rds = doIapCertificate(accId, orderId, receipt, chooseEnv, clientIp);
		return rds;
	}

	private ResultDataSet doIapCertificate(String accId, String orderId, String receipt, boolean chooseEnv,
			String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		OnLineOrder onLineOrder = onLineOrderDao.findOne(orderId);
		if (onLineOrder == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("订单不存在");
			return rds;
		}
		if (!accId.equals(onLineOrder.getAccId())) {
			logger.error(
					String.format("accId:%s;orderid:%s;receipt:%s;chooseEnv:%s;", accId, orderId, receipt, chooseEnv));
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("订单不存在!");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(receipt)) {
			logger.error(
					String.format("accId:%s;orderid:%s;receipt:%s;chooseEnv:%s;", accId, orderId, receipt, chooseEnv));
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("订单异常!");
			return rds;
		}
		if (onLineOrder.getOrderStatus() == OrderStatus.ORDER_DONE_PAY.getValue()) {
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} else if (onLineOrder.getOrderStatus() != OrderStatus.ORDER_NO_PAY.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("订单号过期");
			return rds;
		} else {
			String url = null;
			url = chooseEnv == true ? certificateUrl : certificateUrlTest;
			String strReturn = sendHttpsCoon(url, receipt);
			String envStr = "未知";
			if (chooseEnv) {
				envStr = "苹果正式";
			} else {
				envStr = "苹果测试";
			}
			int status;
			try {
				status = jsonUtil.getMapper().readTree(strReturn).get("status").asInt();

				if (status == 0) {
					onLineOrder.setOrderStatus(OrderStatus.ORDER_COMPLETE_PAY.getValue());
					onLineOrder.setCompleteDate(DateUtils.getNowTime());
					onLineOrderDao.save(onLineOrder);

					// 首冲判断
					Pageable pageable = new PageRequest(0, 1);
					Page<RecordPay> prePayRecords = recordPayDao.findByAccId(onLineOrder.getAccId(), pageable);
					if (prePayRecords == null || prePayRecords.getSize() == 0) {
						mountPresentUtil.presentMountForPay(onLineOrder.getAccId());
					}

					long dateTime = DateUtils.getNowTime();
					RecordPay recordPay = new RecordPay();
					recordPay.setOrderId(onLineOrder.getOrderId());
					recordPay.setAccId(onLineOrder.getAccId());
					recordPay.setAmount(onLineOrder.getPayAmount());
					recordPay.setCreateTime(dateTime);
					recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
					recordPay.setId(StringUtils.createUUID());
					recordPay.setStatus(PayStatus.PAY_STATUS_SUCCESS.getValue());
					recordPay.setStatusDescribe(PayStatus.PAY_STATUS_SUCCESS.getCode());
					recordPay.setTarget(onLineOrder.getAccId());
					recordPay.setEnv(envStr);
					recordPayDao.save(recordPay);

					Wallet wallet = walletPriDao.findByAccId(onLineOrder.getAccId());
					if (wallet == null) {
						rds.setCode(ResultCode.PARAM_INVALID.getCode());
						rds.setMsg(ServerCode.NO_WALLET.getCode());
						return rds;
					}

					MoneyFlow moneyFlow = new MoneyFlow();
					moneyFlow.setAccId(onLineOrder.getAccId());
					moneyFlow.setDateTime(dateTime);
					moneyFlow.setIpAddress(clientIP);
					HelperFunctions.setMoneyFlowType(MoneyFlowType.Recharge, moneyFlow);
					HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
					HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);

					walletPriDao.updateIGold(onLineOrder.getAccId(), onLineOrder.getiGold());

					moneyFlow.setTargetIGold(wallet.getiGold() + onLineOrder.getiGold());
					moneyFlowPriDao.save(moneyFlow);

					TaskYxPay taskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getAccId(),
							onLineOrder.getAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
							onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
							onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
							onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
							onLineOrder.getShareId(), onLineOrder.getToAccId());
					myThreadPool.getYunxinPool().execute(taskYxPay);
					if (!onLineOrder.getAccId().equals(onLineOrder.getToAccId())) {
						TaskYxPay toTaskYxPay = new TaskYxPay(onLineOrder.getAccId(), onLineOrder.getToAccId(),
								onLineOrder.getToAccId(), onLineOrder.getApplyDate(), onLineOrder.getCompleteDate(),
								onLineOrder.getDevice(), onLineOrder.getDiscount(), onLineOrder.getiGold(),
								onLineOrder.getIpAddress(), onLineOrder.getOrderAmount(), onLineOrder.getOrderId(),
								onLineOrder.getOrderStatus(), onLineOrder.getPayAmount(), onLineOrder.getSerialNum(),
								onLineOrder.getShareId(), onLineOrder.getToAccId());
						myThreadPool.getYunxinPool().execute(toTaskYxPay);
					}

					// logger.error(String.format("accId:%s;orderid:%s;receipt:%s;chooseEnv:%s;strReturn:%s;",
					// accId,
					// orderId, receipt, chooseEnv, strReturn));

					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setData(strReturn);
				} else {
					RecordPay recordPay = new RecordPay();
					recordPay.setOrderId(onLineOrder.getOrderId());
					recordPay.setAccId(onLineOrder.getAccId());
					recordPay.setAmount(onLineOrder.getPayAmount());
					recordPay.setCreateTime(DateUtils.getNowTime());
					recordPay.setFromSource(OrderShareType.values()[onLineOrder.getShareId()].getCode());
					recordPay.setId(StringUtils.createUUID());
					recordPay.setStatus(PayStatus.PAY_STATUS_FAILED.getValue());
					recordPay.setStatusDescribe(PayStatus.PAY_STATUS_FAILED.getCode());
					recordPay.setTarget(onLineOrder.getAccId());
					recordPay.setEnv(envStr);
					recordPayDao.save(recordPay);

					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("充值失败");

					String failedMsg = "pay failed orderId:" + onLineOrder.getOrderId() + "\r\n";
					failedMsg += "pay failed receipt:" + receipt + "\r\n";
					failedMsg += "pay failed description:" + strReturn;
					logger.error(failedMsg);
				}
			} catch (IOException e) {
				logger.error("", e);
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("票据验证失败");
			}
		}
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet setIapCertificate(String accId, String orderId, String receipt, boolean chooseEnv,
			String clientIp) {
		return doIapCertificate(accId, orderId, receipt, true, clientIp);
	}

	/**
	 * 发送请求
	 * 
	 * @param url
	 * @param strings
	 * @return
	 */
	private String sendHttpsCoon(String url, String code) {
		if (url.isEmpty()) {
			return null;
		}
		try {
			// 设置SSLContext
			SSLContext ssl = SSLContext.getInstance("SSL");
			ssl.init(null, new TrustManager[] { myX509TrustManager }, null);

			// 打开连接
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			// 设置套接工厂
			conn.setSSLSocketFactory(ssl.getSocketFactory());
			// 加入数据
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-type", "application/json");

			String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + code + "\"}");
			BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
			buffOutStr.write(str.getBytes());
			buffOutStr.flush();
			buffOutStr.close();

			// 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();

		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	@Override
	public ResultDataSet queryOrderStatus(String accId, String orderId) {
		ResultDataSet rds = new ResultDataSet();
		OnLineOrder onLineOrder = onLineOrderDao.findOne(orderId);

		if (onLineOrder == null) {
			logger.error("订单不存在:" + orderId);
			rds.setMsg("订单不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!onLineOrder.getAccId().equals(accId)) {
			logger.error("非法操作");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ResponseData rp = new ResponseData();
		rp.put("status", onLineOrder.getOrderStatus());
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getOrderById(String orderId) {
		ResultDataSet rds = new ResultDataSet();
		OnLineOrder onLineOrder = onLineOrderDao.findOne(orderId);

		if (onLineOrder == null) {
			logger.error("订单不存在:" + orderId);
			rds.setMsg("订单不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(onLineOrder);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getProducts(int device) {
		ResultDataSet rds = new ResultDataSet();
		if (!DeviceCode.contains(device)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("无效设备");
			return rds;
		}
		List<Product> products = productDao.findByDeviceOrderByPrice(device);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(products);
		return rds;
	}

	@Override
	public ResultDataSet verifyAlipayTradePagePayResult(Map<String, String[]> requestParams) throws Exception {
		ResultDataSet rds = new ResultDataSet();

		Map<String, String> params = new HashMap<String, String>();
		StringBuffer paramsStr = new StringBuffer();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			paramsStr.append(name);
			paramsStr.append(":");
			paramsStr.append(valueStr);
			paramsStr.append(", ");
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		String aliPay_public_key = configUtils.getPlatformConfig(Constant.ALI_PAY_PUBLIC_KEY);

		boolean flag = AlipaySignature.rsaCheckV1(params, aliPay_public_key, "utf-8", "RSA2");
		if (flag) {
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("订单验证失败");
		}
		return rds;
	}
}
