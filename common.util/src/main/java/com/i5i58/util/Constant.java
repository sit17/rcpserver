package com.i5i58.util;

/**
 * @author Administrator
 *
 */
public class Constant {

	/**
	 * 账号token失效时间,7天
	 */
	public static final int ACC_TOKEN_TIME_TO_LIVE = 604800;

	public static final int ACC_WEB_TOKEN_TIME_TO_LIVE = 1200;

	/**
	 * vip在有序队列中的倍率 100
	 */
	public static final double VIP_SCORE_RATE = 100;

	/**
	 * 守护在有序队列中的倍率 10
	 */
	public static final double GUARD_SCORE_RATE = 10;

	/**
	 * 获取贵宾席每页数量
	 */
	public static final int SOFA_PAGE_SIZE = 20;

	/**
	 * redis中token存放set的key
	 */
	public static final String HOT_ACCOUNT_TOKEN_SET_KEY = "HotTokens_";

	public static final String HOT_ACCOUNT_JWTTOKEN_SET_KEY = "JWTToken_";

	/**
	 * redis中频道观众存放set的key
	 */
	public static final String HOT_CHANNEL_VIEWER_SET_KEY = "chl_viewers_";

	/**
	 * redis中频道观众存放set的key
	 */
	public static final String HOT_CHANNEL_RICHER_SET_KEY = "chl_richers_";

	/**
	 * redis中频道消费周榜存放set的key
	 */
	public static final String HOT_CHANNEL_WEEKOFFER_HSET_KEY = "chl_weekoffer_hset_";

	/**
	 * redis中频道消费周榜存放set的key
	 */
	public static final String HOT_CHANNEL_WEEKOFFER_SSET_KEY = "chl_weekoffer_sset_";

	/**
	 * redis中直播通知缓存set的key
	 */
	public static final String NOTICE_LIVE_SET_KEY = "notice_live_";

	/**
	 * redis中短拍广场缓存set的key
	 */
	public static final String HOT_SHORT_FILMS_ZSET_KEY = "short_set_";

	/**
	 * 爱心连续签到hashset头
	 */
	public static final String HOT_DAILY_HEART_CONTINUITY_HSET = "daily_heart_continuity_hset";

	/**
	 * 文件储存地址
	 */
	public static final String FILE_STONE_ADDRESS = "http://dev.i5i58.com:8090";

	/**
	 * redis礼物配置版本key
	 */
	public static final String HOT_GIFT_CONFIG_VERSION_KEY = "giftConfigVersion";

	/**
	 * redis坐骑配置版本key
	 */
	public static final String HOT_MOUNT_CONFIG_VERSION_KEY = "mountConfigVersion";

	/**
	 * redishtml配置版本key
	 */
	public static final String HOT_ANIMATION_CONFIG_VERSION_KEY = "animationConfigVersion";

	/**
	 * 动画zip包url
	 */
	public static final String HOT_ANIMATION_ZIP_CONFIG_KEY = "animationZipConfig";

	/**
	 * 动画zip包url
	 */
	public static final String HOT_ANIMATION_RES_CONFIG_KEY = "animationResConfig";

	/**
	 * I币兑换游戏币比例，1I币=？游戏币
	 */
	public static final String SQL_PLATFORM_CONFIG_GAME_GOLD_EXCHARGE_RATE = "gameGoldRate";

	/**
	 * 游戏币兑换钻石比例，1钻石=？游戏币
	 */
	public static final String SQL_PLATFORM_CONFIG_DIAMOND_EXCHARGE_RATE = "gameDiamondRate";

	/**
	 * 魅力值兑换游戏币比例，1魅力值=？游戏币
	 */
	public static final String SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_RATE = "lovelinessRate";

	/**
	 * 每天最多兑换多少魅力值
	 */
	public static final String SQL_PLATFORM_CONFIG_LOVELINESS_EXCHARGE_DAILYLIMIT = "lovelinessDailyLimit";

	/**
	 * 连麦订单过期时间
	 */
	public static final long HOT_CHANNEL_CONNECT_MIC_ORDER_EXPIRE = 60000;

	/**
	 * 频道弹幕价格
	 */
	public static final long DRIFT_COMMENT_PRICE = 10;

	/**
	 * 频道欢迎词
	 */
	public static final String CHANNEL_WELCOME_MESSAGE = "胖虎官方倡导绿色直播，对直播内容24小时在线巡查。任何传播违法、违规、低俗、暴力等不良信息将会封停账号。安全提示：聊天中若有涉及财产安全信息，一定要先核实对方身份，谨防受骗！";

	/**
	 * 支付宝充值
	 */
	public static final int PAY_SHARE_ALIPAY = 1001;

	/**
	 * 微信充值
	 */
	public static final int PAY_SHARE_WECHAT = 1002;

	/**
	 * 网银充值
	 */
	public static final int PAY_SHARE_BANK = 1003;

	/**
	 * payRate充值人民币和I币比率
	 */
	public static final String APPLE_PAY_RATE = "apple_payRate";

	/**
	 * 支付宝回调地址
	 */
	public static final String ALI_PAY_CALLBACK_URL = "alipay_callback_url";

	/**
	 * 支付宝同步跳转地址
	 */
	public static final String ALI_PAY_RETURN_RUL = "alipay_return_url";

	/**
	 * 支付宝appid
	 */
	public static final String ALI_PAY_APPID = "alipay_appId";

	/**
	 * 支付宝私钥
	 */
	public static final String ALI_PAY_APP_PRIVATE_KEY = "alipay_app_private_key";

	/**
	 * 支付宝公钥
	 */
	public static final String ALI_PAY_APP_PUBLIC_KEY = "alipay_app_public_key";

	/**
	 * 支付宝公钥
	 */
	public static final String ALI_PAY_PUBLIC_KEY = "alipay_public_key";

	/**
	 * 微信appId
	 */
	public static final String WECHAT_APP_ID = "wechat_app_id";
	public static final String GAME_WECHAT_APP_ID = "game_wechat_app_id";
	public static final String OFFICIAL_WECHAT_APP_ID = "official_wechat_app_id";

	/**
	 * 微信app密码
	 */
	public static final String WECHAT_APP_SECRET = "wechat_app_secret";
	public static final String GAME_WECHAT_APP_SECRET = "game_wechat_app_secret";
	public static final String OFFICIAL_WECHAT_APP_SECRET = "official_wechat_app_secret";

	/**
	 * 微信商户ID
	 */
	public static final String WECHAT_MERCHANT_ID = "wechat_merchant_id";
	public static final String GAME_WECHAT_MERCHANT_ID = "game_wechat_merchant_id";
	public static final String OFFICIAL_WECHAT_MERCHANT_ID = "official_wechat_merchant_id";

	/**
	 * 微信API key,生成签名时使用
	 */
	public static final String WECHAT_API_KEY = "wechat_api_key";
	public static final String GAME_WECHAT_API_KEY = "game_wechat_api_key";
	public static final String OFFICIAL_WECHAT_API_KEY = "official_wechat_api_key";

	/**
	 * 微信支付回调url
	 */
	public static final String WECHAT_PAY_CALLBACK_URL = "wechat_pay_callback_url";

	/**
	 * 最近观看频道最大保存数量
	 */
	public static final int NEAR_WATCH_CHANNEL_MAX_SIZE = 20;

	/*
	 * windows更新版本,MD5
	 */
	public static final String WINDOWS_UPDATE_LOGIN_VERSION = "windows_update_Login_version";

	/*
	 * windows login更新版本url
	 */
	public static final String WINDOWS_UPDATE_LOGIN_PATH = "windows_update_Login_path";

	/*
	 * windows login更新版本url
	 */
	public static final String WINDOWS_LOGIN_ADV_IMAGE_URL = "windows_Login_Adv_Image_Url";
	/*
	 * windows login更新版本url
	 */
	public static final String WINDOWS_LOGIN_ADV_REDIRECT_URL = "windows_Login_Adv_Redirect_Url";

	/*
	 * windows更新版本,MD5
	 */
	public static final String WINDOWS_UPDATE_JSON_VERSION = "windows_update_json_version";

	/*
	 * windowsboss更新zip包
	 */
	public static final String WINDOWS_BOSS_UPDATE_ZIP_URL = "windows_boss_update_zip_url";

	/*
	 * windows更新版本,MD5
	 */
	public static final String WINDOWS_UPDATE_JSON_PATH = "windows_update_json_path";

	/*
	 * windows游戏更新版本,MD5
	 */
	public static final String WINDOWS_GAME_UPDATE_JSON_VERSION = "windows_game_update_json_version";

	/*
	 * windows游戏更新版本,MD5
	 */
	public static final String WINDOWS_GAME_UPDATE_JSON_PATH = "windows_game_update_json_path";

	/*
	 * windows游戏游戏打包下载url
	 */
	public static final String WINDOWS_GAME_ZIP_PATH = "windows_game_zip_path";

	/**
	 * 苹果版本（apple：审核，mine：运营）
	 */
	public static final String APPLE_RELEASE = "apple_release";

	/**
	 * 分享地址
	 */
	public static final String APP_SOCIAL_SHARE_URL = "app_social_share_url";

	/**
	 * 个人二维码地址
	 */
	public static final String APP_SOCIAL_ACC_URL = "app_social_acc_url";

	/**
	 * 苹果支付类型，作弊
	 */
	public static final String APPLE_PAY_TYPE = "apple_pay_type";

	// /**
	// * android游戏大厅版本号
	// */
	// public static final String Android_Game_Plaza_Version =
	// "Android_Game_Plaza_Version";
	// /**
	// * android游戏大厅下载地址
	// */
	// public static final String Android_Game_Plaza_DownloadUrl =
	// "Android_Game_Plaza_DownloadUrl";

	// /**
	// * ios游戏大厅版本号
	// */
	// public static final String IOS_Game_Plaza_Version =
	// "IOS_Game_Plaza_Version";
	// /**
	// * ios游戏大厅下载地址
	// */
	// public static final String IOS_Game_Plaza_DownloadUrl =
	// "IOS_Game_Plaza_DownloadUrl";

	/**
	 * IOS游戏支付选项 AppStorePay 10 //appstore支付, 默认值 ThirdPay 20 //第三方支付
	 */
	public static final String IOS_Game_Pay_Option = "IOS_Game_Pay_Option";

	/**
	 * 是否可以用胖虎直播登录(true/false)
	 */
	public static final String IOS_Game_PHLive_Logon = "IOS_Game_PHLive_Logon";

	/**
	 * 是否可以对话钻石(true/false)
	 */
	public static final String IOS_Game_DiamondExchanged_Enable = "IOS_Game_DiamondExchanged_Enable";

	/**
	 * 存放accId和virtualChannelId的对应关系
	 */
	public static final String HOT_SESSION_ID_KEY = "hot_session_id_";

	/**
	 * redis 用户数据，哈希表 hot_user_data_123 : key1 value1 key2 value2
	 */
	public static final String HOT_USERDATA = "hot_userdata_";

	/**
	 * redis 用户当前所登录的业务服务器, 是HOT_USER_DATA的子项
	 */
	public static final String SUB_USERDATA_SERVER_LOGGED_IN = "server_logged_in";

	/**
	 * redis 记录业务服务器上的登录用户的虚拟sessionId，这个虚拟的sessionId是该用户在网关上的真实sessionId
	 */
	public static final String SUB_USERDATA_VIRTUAL_SESSION_ID = "virtual_session_id";

	/**
	 * redis 记录网关信息
	 */
	public static final String HOT_GATESERVER_INFO = "hot_gateServer_info";

	/**
	 * windows客户端首页版本
	 */
	public static final String WINDOWS_BOSS_HOME_PAGE_VERSION = "windows_boss_home_page_version";

	/**
	 * windows客户端内嵌直播间版本
	 */
	public static final String WINDOWS_BOSS_LIVE_PAGE_VERSION = "windows_boss_live_page_version";

	public static final String JWT_SECRET_KEY = "hsbdehsvfjsdvskjdbfj";

	public static final String WINDOWS_BOSS_CONFIG = "windows_boss_config_";

	/**
	 * 推流配置
	 */

	public static final String PUSH_VIDEO_QUALITY = "push_video_quality";
	public static final String PUSH_VIDEO_MIX_MODE = "push_video_mix_mode";
	public static final String PUSH_VIDEO_LOGO_MARK = "push_video_logo_mark";

	public static final String CHANNEL_STATUS_CHECK = "channel_status_check";

	/**
	 * 用户权限-超管权限
	 */
	public static final int USER_RIGHT_SUPER = 65535;

	/**
	 * 商务代理-管理权限
	 */
	public static final int BUSINESS_AGENT_ADMIN = 1024;

	/**
	 * 视频云签名key
	 */
	public static final String VIDEO_CLOUD_SIGN_KEY = "vedio_cloud_sign_key";

	/**
	 * 直播频道状态回调地址
	 */
	public static final String CHANNEL_STATUS_CALLBACK_URL = "channel_status_callback_url";

	/**
	 * 即构access_token
	 */
	public static final String ZEGO_ACCESS_TOKEN = "zego_access_token";

	/**
	 * OSS的endpoint
	 */
	public static final String OSS_ENDPOINT = "oss_endpoint";

	/**
	 * OSS的accessKeyId
	 */
	public static final String OSS_ACCESS_KEY_ID = "oss_access_key_id";

	/**
	 * OSS的accesskeySecret
	 */
	public static final String OSS_ACCESS_KEY_SECRET = "oss_access_key_secret";

	/**
	 * 网吧充值赠送比例
	 */
	public static final String NETBAR_PAY_PRESENT_RATE = "netbar_pay_present_rate";

	/**
	 * 网吧账号兑换游戏币比例
	 */
	public static final String NET_BAR_EXCHANGE_GAME_SCORE_RATE = "net_bar_exchange_game_score_rate";

	/*
	 * 网吧兑换欢乐豆返利比率最大值(百分比)
	 */
	public static final String NETBAR_EXCHANGESCOREREBATEMAX_RATE = "netbar_exchangescorerebatemax_rate";
	
	/*
	 * 网吧兑换欢乐豆返利比率最小值(百分比)
	 */
	public static final String NETBAR_EXCHANGESCOREREBATEMIN_RATE = "netbar_exchangescorerebatemin_rate";
	
	/*
	 * 网吧兑换欢乐豆返利最大最小界限
	 */
	public static final String NETBAR_EXCHANGESCOREREBATENUM = "netbar_exchangescorerebatenum";
	
	/*
	 * 在游戏中消费欢乐豆返利比率（百分比）
	 */
	public static final String NETBAR_INGAMECONSUMEREBATE_RATE = "netbar_ingameconsumerebate_rate";
	
	/*
	 * 网吧内在直播间送礼物返利的比率
	 */
	public static final String NETBAR_INLIVEGIVEGIFTREBATE_RATE = "netbar_inlivegivegiftrebate_rate";
	
	/*
	 * 网吧充值返利比率(百分比)
	 */
	public static final String NETBAR_PAYREBATE_RATE = "netbar_payrebate_rate";

	public static final int NET_BAR_ADMIN_ONLINE_INTERVAL = 60000;
}
