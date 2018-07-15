package com.i5i58.service.channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.channel.IZegoChannelPush;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.config.MyThreadPool;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.ZegoClk;
import com.i5i58.data.channel.ZegoReplay;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.GroupAuth;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelStatus;
import com.i5i58.primary.dao.channel.ZegoClkPriDao;
import com.i5i58.primary.dao.channel.ZegoReplayPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.secondary.dao.account.AccountConfigSecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelNoticeSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.service.channel.async.TaskChannelNotice;
import com.i5i58.service.channel.async.TaskYxOpenClosePush;
import com.i5i58.service.channel.async.TaskYxZegoCreateCallback;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.GroupUtils;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.zego.ZegoLive;
import com.i5i58.zego.ZegoResult;
import com.i5i58.zego.utils.ZegoUtils;

@Service(protocol = "dubbo")
public class ZegoChannelPushService implements IZegoChannelPush {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	JedisUtils jedisUtils;
	
	@Autowired
	ChannelPriDao channelPriDao;
	
	@Autowired
	ChannelSecDao channelSecDao;
	
	@Autowired
	AccountSecDao accountSecDao;
	
	@Autowired
	ChannelGroupSecDao channelGroupSecDao;
	
	@Autowired
	GroupUtils groupUtils;
	
	@Autowired
	ZegoClkPriDao zegoClkPriDao;
	
	@Autowired
	AnchorUtils anchorUtils;
	
	@Autowired
	MyThreadPool myThreadPool;
	
	@Autowired
	ChannelNoticeSecDao channelNoticeSecDao;
	
	@Autowired
	AccountConfigSecDao accountConfigSecDao;
	
	@Autowired
	RabbitMqBroadcastSender broadcastSender;
	
	@Autowired
	ZegoReplayPriDao zegoReplayPriDao;
	
	@Autowired
	ChannelUtils channelUtils;
	
	@Autowired
	JsonUtils jsonUtil;
	
	@Autowired
	HotChannelDao	hotChannelDao;
	public String getAccessToken(){
		String access_token = jedisUtils.get(Constant.ZEGO_ACCESS_TOKEN);
		if (StringUtils.StringIsEmptyOrNull(access_token)){
			try {
				int expire = 0;
				ZegoResult result = ZegoLive.getToken();
				HashMap<String, Object> data = result.getData();
				if (result.getCodeInt() == 0 && data != null){
					access_token = (String) data.get("access_token");
					expire = (Integer) data.get("expires_in");
				}
				if (!StringUtils.StringIsEmptyOrNull(access_token) && expire > 0){
					jedisUtils.set(Constant.ZEGO_ACCESS_TOKEN, access_token, "NX", "EX", expire);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return access_token;
	}

	
	@Override
	public ResultDataSet createLive(String accId, String term_type, String net_type) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		String access_token = getAccessToken();
		if (StringUtils.StringIsEmptyOrNull(access_token)){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("获取Zego access_token失败");
			return rds;
		}
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还没有频道");
			return rds;
		}
//		if (!account.getAccId().equals(channel.getOwnerId())){
//			rds.setCode(ResultCode.PARAM_INVALID.getCode());
//			rds.setMsg("只有主播可以开播");
//			return rds;
//		}
		String stream_id = channel.getZegoStreamId();
		if (!StringUtils.StringIsEmptyOrNull(stream_id)){
			ZegoResult result = ZegoLive.closeLive(access_token, stream_id);
			if (result.getCodeInt() == 0){
				channelPriDao.updateZegoStreamId(channel.getcId(), "");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("频道状态错误，请重试");
				return rds;
			}else if (result.getCodeInt() == 41003 || result.getCodeInt() == 41004){
				//直播不存在/流不存在
				channel.setZegoStreamId("");
				channel.setZegoPublishUrl("");
				channelPriDao.save(channel);
			}else {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(ZegoUtils.getErrorString(result.getCodeInt()));
				return rds;
			}
		}
		
		String title 		= channel.getTitle();
		String id_name 		= account.getAccId();
		String nick_name 	= account.getNickName();
		String stream_alias = "OBS_" + channel.getChannelId();
		String live_channel = channel.getcId();
		System.out.println("title : " + title);
		System.out.println("id_name : " + id_name);
		System.out.println("nick_name : " + nick_name);
		System.out.println("stream_alias : " + stream_alias);
		System.out.println("live_channel : " + live_channel);
		ZegoResult result = ZegoLive.createLive(access_token, title, id_name, nick_name, term_type, net_type, stream_alias,
				"", live_channel, "", "", "", "");
		if (result.getCodeInt() == 0){
			HashMap<String, Object> data = result.getData();
			if (data != null){
				String zegoStreamId = (String) data.get("stream_id");
				String zegoPublishUrl = (String) data.get("publish_url");
				if (!StringUtils.StringIsEmptyOrNull(zegoStreamId)){
					channel.setZegoPublishUrl(zegoPublishUrl);
					channel.setZegoStreamId(zegoStreamId);
					channelPriDao.save(channel);
				}
			}
			rds.setData(data);
			rds.setCode(ResultCode.SUCCESS.getCode());
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ZegoUtils.getErrorString(result.getCodeInt()));
		}
		return rds;
	}

	@Override
	public ResultDataSet closeLive(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		String access_token = getAccessToken();
		if (StringUtils.StringIsEmptyOrNull(access_token)){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("获取Zego access_token失败");
			return rds;
		}
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还没有频道");
			return rds;
		}
//		if (!account.getAccId().equals(channel.getOwnerId())){
//			rds.setCode(ResultCode.PARAM_INVALID.getCode());
//			rds.setMsg("只有主播可以关播");
//			return rds;
//		}
		String stream_id = channel.getZegoStreamId();
		if(StringUtils.StringIsEmptyOrNull(stream_id)){
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		ZegoResult result = ZegoLive.closeLive(access_token, stream_id);
		if (result.getCodeInt() == 0){
			channelPriDao.updateZegoStreamId(channel.getcId(), "");
			rds.setCode(ResultCode.SUCCESS.getCode());
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ZegoUtils.getErrorString(result.getCodeInt()));
		}
		return rds;
	}

	@Override
	public ResultDataSet forbidLive(String admin, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		String access_token = getAccessToken();
		if (StringUtils.StringIsEmptyOrNull(access_token)){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("获取Zego access_token失败");
			return rds;
		}
		Account account = accountSecDao.findOne(admin);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		Channel channel = channelPriDao.findOne(cId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道不存在");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(channel.getgId());
		if (!groupUtils.verifyGroupAuth(channelGroup, admin, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		String stream_alias = channel.getcId();
		ZegoResult result = ZegoLive.forbidLive(access_token, stream_alias);
		if (result.getCodeInt() == 0){
			rds.setCode(ResultCode.SUCCESS.getCode());
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ZegoUtils.getErrorString(result.getCodeInt()));
		}
		return rds;
	}

	@Override
	public ResultDataSet resumeLive(String admin, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		String access_token = getAccessToken();
		if (StringUtils.StringIsEmptyOrNull(access_token)){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("获取Zego access_token失败");
			return rds;
		}
		Account account = accountSecDao.findOne(admin);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		Channel channel = channelPriDao.findOne(cId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道不存在");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(channel.getgId());
		if (!groupUtils.verifyGroupAuth(channelGroup, admin, GroupAuth.ASSIGN_EDIT_INFO_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有权限");
			return rds;
		}
		String stream_alias = channel.getcId();
		ZegoResult result = ZegoLive.resumeLive(access_token, stream_alias);
		if (result.getCodeInt() == 0){
			rds.setCode(ResultCode.SUCCESS.getCode());
		}else{
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ZegoUtils.getErrorString(result.getCodeInt()));
		}
		return rds;
	}
	
	public void recordCallback(int clkType, Map<String, String[]> paramMap){
		ZegoClk zc = new ZegoClk();
		String data;
		long dateTime = DateUtils.getNowTime();
		try {
			data = new JsonUtils().toJson(paramMap);
			if (data.length()>1024){
				data = data.substring(0, 1024);
			}
			zc.setClkType(clkType);
			zc.setData(data);
			zc.setDateTime(dateTime);
			zegoClkPriDao.save(zc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String ceateCallback(Map<String, String[]> paramMap) {
		recordCallback(1, paramMap);
		String channel_id 	= paramMap.get("channel_id")[0]; // 对应cId
		String str_live_id 	= paramMap.get("live_id")[0];
		String stream_alias = paramMap.get("stream_alias")[0];
		String rtmp_url 	= paramMap.get("rtmp_url[0]")[0];
		String hls_url 		= paramMap.get("hls_url[0]")[0];
		String hdl_url 		= paramMap.get("hdl_url[0]")[0];
		String create_time 	= paramMap.get("create_time")[0];
		String timestamp 	= paramMap.get("timestamp")[0];
		String nonce 		= paramMap.get("nonce")[0];
		String signature 	= paramMap.get("signature")[0];
		
		String sig = ZegoLive.calcSignature(timestamp, nonce);
		if (sig == null || !sig.equals(signature)){
			logger.error("zego,流创建回调,签名验证失败");
			return "";
		}
		
		Channel channel = channelPriDao.findByCId(channel_id);
		if (channel == null){
			logger.error("zego,流创建回调,频道不存在,cId = " + channel_id);
			return "";
		}
		long time = Long.parseLong(create_time) * 1000; // 转化成毫秒
		if (time > channel.getStatusChanged()){
			int live_id = Integer.parseInt(str_live_id);
			if (live_id != 0){
				channel.setZegoLiveId(live_id);
			}
			if (!StringUtils.StringIsEmptyOrNull(hdl_url)){			
				channel.setZegoHdlUrl(hdl_url);
			}
			if (!StringUtils.StringIsEmptyOrNull(hls_url)){			
				channel.setZegoHlsUrl(hls_url);
			}
			if (!StringUtils.StringIsEmptyOrNull(rtmp_url)){			
				channel.setZegoRtmpUrl(rtmp_url);
			}
			if (!StringUtils.StringIsEmptyOrNull(stream_alias)){
				channel.setZegoStreamAlias(stream_alias);
			}
			channelPriDao.save(channel);
		}
		
		
		//OBS推流需要手动改变状态
		if (stream_alias.indexOf("OBS") == 0){
			return "1";
		}
		

		channelOpened(channel, time);
//		ChannelStatus channelStatus = ChannelStatus.OPEN;
//		anchorUtils.onLiveOpenCallback(channel.getOwnerId(), channel.getcId(), time);
//		
//		if (time > channel.getStatusChanged()) {
//			channel.setStatus(channelStatus.getValue());
//			channel.setStatusChanged(time);
//			channelPriDao.save(channel);
//			TaskChannelNotice taskChannelNotice = new TaskChannelNotice(accountSecDao, channelSecDao,
//					channelNoticeSecDao, accountConfigSecDao, channel.getcId(), broadcastSender, jedisUtils);
//			myThreadPool.getYunxinPool().execute(taskChannelNotice);
//		}
//		channelPriDao.save(channel);
//		
//		TaskYxZegoCreateCallback taskYxCreateCallback = new TaskYxZegoCreateCallback(channel.getYunXinRId(), 
//				channel.getOwnerId(), rtmp_url, hdl_url, hls_url, jsonUtil);
//		myThreadPool.getYunxinPool().execute(taskYxCreateCallback);
		return "1";
	}
	
	public void channelOpened(Channel channel, long time){
		ChannelStatus channelStatus = ChannelStatus.OPEN;
		anchorUtils.onLiveOpenCallback(channel.getOwnerId(), channel.getcId(), time);
		
		if (time > channel.getStatusChanged()) {
			channel.setStatus(channelStatus.getValue());
			channel.setStatusChanged(time);
			channelPriDao.save(channel);
			TaskChannelNotice taskChannelNotice = new TaskChannelNotice(time, accountSecDao, channelSecDao,
					channelNoticeSecDao, accountConfigSecDao, channel.getcId(), broadcastSender, jedisUtils);
			myThreadPool.getYunxinPool().execute(taskChannelNotice);
			channelPriDao.save(channel);
			
			TaskYxZegoCreateCallback taskYxCreateCallback = new TaskYxZegoCreateCallback(channel.getYunXinRId(), 
					channel.getOwnerId(), channel.getZegoRtmpUrl(), channel.getZegoHdlUrl(), 
					channel.getZegoHlsUrl(), channel.getZegoStreamAlias(),jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxCreateCallback);
		}
	}

	@Override
	public String closeCallback(Map<String, String[]> paramMap) {
		recordCallback(2, paramMap);
		String channel_id 	= paramMap.get("channel_id")[0]; // cId
		
		String timestamp 	= paramMap.get("timestamp")[0];
		String nonce 		= paramMap.get("nonce")[0];
		String signature 	= paramMap.get("signature")[0];
		
		String sig = ZegoLive.calcSignature(timestamp, nonce);
		if (sig == null || !sig.equals(signature)){
			logger.error("zego,流关闭回调,签名验证失败");
			return "";
		}
		
		Channel channel = channelPriDao.findByCId(channel_id);
		if (channel == null){
			logger.error("zego,流关闭回调,频道不存在, cId = " + channel_id);
			return "";
		}
		long time = Long.parseLong(timestamp) * 1000; // 转化成毫秒
		ChannelStatus channelStatus = ChannelStatus.Close;
		anchorUtils.onLiveCloseCallback(channel.getOwnerId(), channel.getcId(), time);
		
		if (time > channel.getStatusChanged()) {
			channel.setStatus(channelStatus.getValue());
			channel.setStatusChanged(time);
			channel.setZegoLiveId(0);
			channel.setZegoHdlUrl("");
			channel.setZegoRtmpUrl("");
			channel.setZegoHlsUrl("");
			channel.setZegoStreamAlias("");
			channelPriDao.save(channel);
		}
		return "1";
	}

	@Override
	public String replayCallback(Map<String, String[]> paramMap) {
		recordCallback(3, paramMap);
		
		int live_id 			= Integer.parseInt(paramMap.get("live_id")[0]);
		String channel_id 		= paramMap.get("channel_id")[0];
		String stream_alias 	= paramMap.get("stream_alias")[0];
		String title 			= paramMap.get("title")[0];
		String publish_id 		= paramMap.get("publish_id")[0];
		String publish_name 	= paramMap.get("publish_name")[0];
		int  online_nums 		= Integer.parseInt(paramMap.get("online_nums")[0]);
		int player_count 		= Integer.parseInt(paramMap.get("player_count")[0]);
		String replay_url 		= paramMap.get("replay_url")[0];
		long begin_time 		= Long.parseLong(paramMap.get("begin_time")[0]) * 1000;
		long end_time 			= Long.parseLong(paramMap.get("end_time")[0]) * 1000;
		
		String timestamp 	= paramMap.get("timestamp")[0];
		String nonce 		= paramMap.get("nonce")[0];
		String signature 	= paramMap.get("signature")[0];
		String sig = ZegoLive.calcSignature(timestamp, nonce);
		if (sig == null || !sig.equals(signature)){
			logger.error("zego, 回看地址生成回调, 签名验证失败");
			return "";
		}
		
		ZegoReplay replay = new ZegoReplay();
		replay.setLiveId(live_id);
		replay.setcId(channel_id);
		replay.setStreamAlias(stream_alias);
		replay.setTitle(title);
		replay.setPublishId(publish_id);
		replay.setPublishName(publish_name);
		replay.setOnlineNums(online_nums);
		replay.setPlayerCount(player_count);
		replay.setReplayUrl(replay_url);
		replay.setBeginTime(begin_time);
		replay.setEndTime(end_time);
		zegoReplayPriDao.save(replay);
		
		return "1";
	}
	
	@Override
	public ResultDataSet openPush(String accId, String cId, int device) {
		ResultDataSet rds = new ResultDataSet();
		if (device != DeviceCode.PCLive){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该接口暂时只能在PC端调用");
			return rds;
		}
		if (!channelUtils.checkFirstMic(accId, cId)) {
			rds.setMsg("只有第一号麦序才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		Channel channel = channelSecDao.findByCId(cId);
		if (channel == null || hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(channel.getZegoPublishUrl())
				|| StringUtils.StringIsEmptyOrNull(channel.getZegoStreamId())){
			rds.setMsg("请先在【设置】中创建直播");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		if(StringUtils.StringIsEmptyOrNull(channel.getZegoRtmpUrl())
			|| StringUtils.StringIsEmptyOrNull(channel.getZegoHdlUrl())
			|| StringUtils.StringIsEmptyOrNull(channel.getZegoHlsUrl())
			|| StringUtils.StringIsEmptyOrNull(channel.getZegoStreamAlias())){
			rds.setMsg("直播正在创建中，请稍后开播");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		if (hotChannel.getType() == 0) {
			rds.setMsg("请先分配频道类型");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("只有频道拥有者才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getCoverUrl().isEmpty()) {
			rds.setMsg("请先上传频道封面图");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getTitle().isEmpty()) {
			rds.setMsg("请先设置频道主题");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		hotChannel.setPushDevice(device);
		hotChannelDao.save(hotChannel);
		
		long time = DateUtils.getNowTime();
		channelOpened(channel, time);

		TaskYxOpenClosePush taskYxOpenPush = new TaskYxOpenClosePush(hotChannel.getYunXinRId(), accId, "openPush",
				jsonUtil);
		myThreadPool.getYunxinPool().execute(taskYxOpenPush);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet closePush(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!channelUtils.checkFirstMic(accId, cId)) {
			rds.setMsg("只有第一号麦序才能关闭直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		Channel channel = channelSecDao.findByCId(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道不在缓存中");
			return rds;
		}
		if (!channel.getOwnerId().equals(accId)) {
			rds.setMsg("只有频道拥有者才能关闭直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		//调用即构关闭接口
		rds = closeLive(accId);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode())){
			TaskYxOpenClosePush taskYxOpenPush = new TaskYxOpenClosePush(channel.getYunXinRId(), accId, "closePush",
					jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxOpenPush);			
		}
		return rds;
	}
}
