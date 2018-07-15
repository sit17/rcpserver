package com.i5i58.zego.utils;

public class ZegoUtils {
	
	public static String getErrorString(int code){
		String errorMsg = null;
		switch (code) {
		case 0:
			errorMsg = "请求成功";
			break;
		case 1:
			errorMsg = "请求失败，可能是服务器繁忙，请重试";
			break;
		case 2:
			errorMsg = "输入参数错误";
			break;
		case 150:
			errorMsg = "混流的输入流不存在";
			break;
		case 151:
			errorMsg = "混流失败";
			break;
		case 152:
			errorMsg = "停止混流失败";
			break;
		case 153:
			errorMsg = "输入错误";
			break;
		case 154:
			errorMsg = "输出错误";
			break;
		case 155:
			errorMsg = "输入格式错误";
			break;
		case 156:
			errorMsg = "输出格式错误";
			break;
		case 157:
			errorMsg = "混流功能没开";
			break;
		case 40001:
			errorMsg = "获取access_token时AppSecret错误，或者access_token无效。请开发者认真比对AppSecret的正确性，或查看是否正在为恰当的调用接口";
			break;
		case 40002:
			errorMsg = "不合法的凭证类型";
			break;
		case 40003:
			errorMsg = "appid为空";
			break;
		case 40004:
			errorMsg = "appid错误";
			break;
		case 40005:
			errorMsg = "appsecret错误";
			break;
		case 41001:
			errorMsg = "直播保存失败";
			break;
		case 41002:
			errorMsg = "频道保存失败";
			break;
		case 41003:
			errorMsg = "直播不存在";
			break;
		case 41004:
			errorMsg = "流已不存在";
			break;
		case 42001:
			errorMsg = "禁用直播失败";
			break;
		case 42002:
			errorMsg = "恢复直播失败";
			break;
		case 43001:
			errorMsg = "创建录制索引文件失败";
			break;
		default:
			errorMsg = "未知错误";
			break;
		}
		return errorMsg;
	}
}
