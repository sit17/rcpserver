package com.i5i58.util;

public class DeviceCode {
	public final static int NONE = 0;
	public final static int PCLive = 1;
	public final static int WEBLive = 2;
	public final static int IOSLive = 3;
	public final static int AndroidLive = 4;
	public final static int PCGame = 10;
	public final static int IOSGame = 11;
	public final static int AndroidGame = 12;
	public final static int WechatPublic = 13;
	
	public final static int IOSGame_V2 = 21;

	/**
	 * 网吧后台
	 */
	public final static int NetBar = 30;
	
	public final static int Tool_1 = 101;//商务代理
	public final static int Tool_2 = 102;
	public final static int Tool_3 = 103;
	public final static int Tool_4 = 104;
	public final static int Tool_5 = 105;
	public final static int Tool_6 = 106;
	public final static int Tool_7 = 107;
	public final static int Tool_8 = 108;
	public final static int Tool_9 = 109;
	public final static int Tool_10 = 110;
	
	public static boolean contains(int device){
		switch (device) {
		case PCLive:
		case WEBLive:
		case IOSLive:
		case AndroidLive:
		case PCGame:
		case IOSGame:
		case AndroidGame:
		case WechatPublic:
		case IOSGame_V2:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isGame(int device){
		switch (device) {
		case PCGame:
		case IOSGame:
		case AndroidGame:
		case IOSGame_V2:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isLive(int device){
		switch (device) {
		case PCLive:
		case WEBLive:
		case IOSLive:
		case AndroidLive:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isWeb(int device){
		switch (device) {
		case WEBLive:
		case WechatPublic:
		case NetBar:
			return true;
		default:
			return false;
		}
	}
}
