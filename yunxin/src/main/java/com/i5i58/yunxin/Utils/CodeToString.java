package com.i5i58.yunxin.Utils;

/**
 * @ClassName: CodeToString
 * @Description: 服务端向网易云信后台发起请求，会得到不同的状态码, 这里把状态码转换成对应的文字描述，方便调试
 * @author songfl
 * @date 2016年8月12日 下午3:18:07
 * 
 */
public class CodeToString {
	
	/**
	 * 返回状态码对应的文字描述 
	 * 
	 * @author songfeilong
	 * @param code
	 * @return
	 */
	public static String getString(int code) {
		String ret;
		switch (code) {
		case 200:
			ret = "操作成功";
			break;
		case 201:
			ret = "客户端版本不对，需升级sdk";
			break;
		case 301:
			ret = "被封禁";
			break;
		case 302:
			ret = "用户名或密码错误";
			break;
		case 315:
			ret = "IP限制";
			break;
		case 403:
			ret = "非法操作或没有权限";
			break;
		case 404:
			ret = "对象不存在";
			break;
		case 405:
			ret = "参数长度过长";
			break;
		case 406:
			ret = "对象只读";
			break;
		case 408:
			ret = "客户端请求超时";
			break;
		case 409:
			ret = "用户登录认证失败";
			break;
		case 413:
			ret = "验证失败(短信服务)";
			break;
		case 414:
			ret = "参数错误";
			break;
		case 415:
			ret = "客户端网络问题";
			break;
		case 416:
			ret = "频率控制";
			break;
		case 417:
			ret = "重复操作";
			break;
		case 418:
			ret = "通道不可用(短信服务)";
			break;
		case 419:
			ret = "数量超过上限";
			break;
		case 422:
			ret = "账号被禁用";
			break;
		case 431:
			ret = "HTTP重复请求";
			break;
		case 500:
			ret = "服务器内部错误";
			break;
		case 501:
			ret = "内部错误";
			break;
		case 503:
			ret = "服务器繁忙";
			break;
		case 514:
			ret = "服务不可用";
			break;
		case 509:
			ret = "无效协议";
			break;
		case 602:
			ret = "查询失败";
			break;
		case 607:
			ret = "用户信息不存在";
			break;
		case 609:
			ret = "频道ID为空";
			break;
		case 613:
			ret = "CheckSum为空";
			break;
		case 614:
			ret = "AppKey为空";
			break;
		case 615:
			ret = "CurTime为空";
			break;
		case 617:
			ret = "频道信息与当前用户不匹配";
			break;
		case 618:
			ret = "频道信息不存在";
			break;
		case 631:
			ret = "请求参数错误";
			break;
		case 998:
			ret = "解包错误";
			break;
		case 999:
			ret = "打包错误";
			break;
		///////////////////
		// 群相关消息错误码
		case 801:
			ret = "群人数达到上限";
			break;
		case 802:
			ret = "没有权限";
			break;
		case 803:
			ret = "群不存在";
			break;
		case 804:
			ret = "用户不在群";
			break;
		case 805:
			ret = "群类型不匹配";
			break;
		case 806:
			ret = "创建群数量达到限制";
			break;
		case 807:
			ret = "群成员状态错误";
			break;
		case 808:
			ret = "申请成功";
			break;
		case 809:
			ret = "已经在群内";
			break;
		case 810:
			ret = "邀请成功";
			break;
		//////////////////
		// 音视频相关错误码
		case 9102:
			ret = "通道失效";
			break;
		case 9103:
			ret = "已经在他端对这个呼叫响应过了";
			break;
		case 11001:
			ret = "通话不可达，对方离线状态";
			break;
		//////////////////
		// 聊天室相关错误码
		case 13001:
			ret = "IM主连接状态异常";
			break;
		case 13002:
			ret = "聊天室状态异常";
			break;
		case 13003:
			ret = "账号在黑名单中,不允许进入聊天室";
			break;
		case 13004:
			ret = "在禁言列表中,不允许发言";
			break;
		//////////////////
		// 特定业务相关错误码
		case 10431:
			ret = "输入email不是邮箱";
			break;
		case 10432:
			ret = "输入mobile不是手机号码";
			break;
		case 10433:
			ret = "注册输入的两次密码不相同";
			break;
		case 10434:
			ret = "企业不存在";
			break;
		case 10435:
			ret = "登陆密码或帐号不对";
			break;
		case 10436:
			ret = "app不存在";
			break;
		case 10437:
			ret = "email已注册";
			break;
		case 10438:
			ret = "手机号已注册";
			break;
		case 10441:
			ret = "app名字已经存在";
			break;
		default:
			ret = "[Code 找不到相应的状态描述]";
			break;
		}
		ret = code + ":" + ret;
		return ret;
	}

	public static String getString(String code)
	{
		return getString(Integer.parseInt(code));
	}

}
