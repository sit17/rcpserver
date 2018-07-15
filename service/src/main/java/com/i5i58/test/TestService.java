package com.i5i58.test;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.test.ITest;
import com.i5i58.config.MyThreadPool;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.mapper.test.AccountPriMapper;
import com.i5i58.secondary.dao.account.AccountConfigSecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelNoticeSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.mapper.test.AccountSecMapper;
import com.i5i58.service.channel.async.TaskChannelNotice;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;

@Service(protocol = "dubbo")
public class TestService implements ITest {

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	RabbitMqBroadcastSender sender;

	@Autowired
	AccountPriDao accountPriDao;
	
	@Autowired
	AccountSecDao accountSecDao;
	
	@Autowired
	ChannelSecDao channelSecDao;
	
	@Autowired
	ChannelNoticeSecDao channelNoticeSecDao;
	@Autowired
	AccountConfigSecDao accountConfigSecDao;
	
	@Autowired
	private AccountPriMapper accountPriMapper;
	
	@Autowired
	private AccountSecMapper accountSecMapper;
	
	public Account testQuery(){
		Account account = accountSecMapper.selectAccountByPhoneNo("13738049078");
		try {
			System.out.println(new JsonUtils().toJson(account));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accountPriMapper.updateNickName("myBatis primary test", "13738049078");
		
//		accountSecMapper.updateNickName("myBatis secondary test", "13738049078");
		return account;
	}
	
	@Override
	public ResultDataSet test(String name, int age,String clientIp,String clientIpYun) {

//		System.out.println("Start test");
		ResultDataSet rds = new ResultDataSet();
//		ResponseData rp = new ResponseData();
//		rp.put("clientIp", clientIp);
//		rp.put("clientIpYun", clientIpYun);
//		rds.setData(rp);
//		System.out.print("真实ip：" + clientIp);
//		System.out.println("内网Ip" + clientIpYun);
		
//		 TestThread testThread = new TestThread(name, age);
//		 myThreadPool.getHighPriorityPool().execute(testThread);
//		 System.out.println("End test");
//		 jedisUtils.hset("test", name, "" + age);
		
//		 for (int i=0; i<10; i++){
//			 RabbitMessage message = new RabbitMessage();
//			 message.setCmd("test");
//			 message.setData("hello");
//			 sender.sendMessage(message);
//		 }
//				RabbitMessage message = new RabbitMessage();
//				message.setCmd("channelNotice");
//				
//				JSONObject ob = new JSONObject();
//				ob.put("cId", "cf6914773b1e46fbba8ec0357a865eb3");
//				ob.put("channelName", "频道名称");
//				ob.put("owner", "2fb0b0f7b69a4e808f16adbba5f2c084");
//				ob.put("ownerName", "主播名称");
//				ob.put("to", "45f74da49a0e44868d8aea93eeb4f0e8");
//				
//				message.setData(ob.toJSONString());
		Account account = testQuery();
		rds.setData(account);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet testRedis() {
		ResultDataSet rds = new ResultDataSet();
//		String accId = "a3a4900dfb3141a8984650a71a5f38a4";
//		String keyPrefix = "HotAccounts";
//
//		long preMilli = System.currentTimeMillis();
//		// logger time test*******************************************
//		long nowMilli = System.currentTimeMillis();
		// System.out.println("time used 1: " + (nowMilli - preMilli));
		// preMilli = nowMilli;
		//
		// boolean isExistsAccId = jedisUtils.sismember(keyPrefix, accId);
		// System.out.println("isExistsAccId : " + isExistsAccId);
		// nowMilli = System.currentTimeMillis();
		// System.out.println("time used 2: " + (nowMilli - preMilli));
		// preMilli = nowMilli;
		//
		// boolean isExistsDetail = jedisUtils.exist(keyPrefix + ":" + accId);
		// System.out.println("isExistsDetail : " + isExistsDetail);
		// nowMilli = System.currentTimeMillis();
		// System.out.println("time used 3: " + (nowMilli - preMilli));
		// preMilli = nowMilli;
		//
		// String strRichScore = jedisUtils.hget(keyPrefix + ":" + accId,
		// "richScore");
		// //System.out.println("richScore : " + strRichScore);
		// nowMilli = System.currentTimeMillis();
		// System.out.println("time used 4: " + (nowMilli - preMilli));
		// preMilli = nowMilli;
		//
		// Map<String, String> accountDetail = jedisUtils.hgetAll(keyPrefix +
		// ":" + accId);
		// System.out.println("richScore : " + strRichScore);
		// nowMilli = System.currentTimeMillis();
		// System.out.println("time used 5: " + (nowMilli - preMilli));
		// preMilli = nowMilli;
		//
		// strRichScore = accountDetail.get("richScore");
		// long richScore = Long.parseLong(strRichScore) + 100;
		// accountDetail.put("richScore", ""+richScore);
		//
		// System.out.println("hmset ret = " + accountDetail.toString());
		// String ret = jedisUtils.hmset(keyPrefix + ":" + accId,
		// accountDetail);
		// System.out.println("hmset ret = " + ret);
		// nowMilli = System.currentTimeMillis();
		// System.out.println("time used 6: " + (nowMilli - preMilli));
		// preMilli = nowMilli;

//		Account account = accountPriDao.findOne(accId);
//		if (account == null) {
//			return rds;
//		}
//		nowMilli = System.currentTimeMillis();
//		System.out.println("time used 7: " + (nowMilli - preMilli));
//		preMilli = nowMilli;
//
//		// hotAccount.setRichScore(hotAccount.getRichScore() + 100);
//		accountPriDao.save(account);
//		nowMilli = System.currentTimeMillis();
//		System.out.println("time used 8: " + (nowMilli - preMilli));
//		preMilli = nowMilli;

		// String md5s = "";
		// try {
		// md5s = StringUtils.getMd5("huangling123");
		// } catch (NoSuchAlgorithmException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("huangling123 -->md5 = " + md5s);
		return rds;
	}

	@Override
	public ResultDataSet kick() {
		ResultDataSet rds = new ResultDataSet();
		
//		 MsgYxChatIdentity msg = new MsgYxChatIdentity();
//		YxCustomMsg yxChatMsg = new YxCustomMsg();
//		yxChatMsg.setCmd("kick");
//		yxChatMsg.setData(msg);
//		String uuid = StringUtils.createUUID();
//		String roomId = "9600831";
//		String accId = "45f74da49a0e44868d8aea93eeb4f0e8";
//		YXResultSet resultR = null;
//		try {
//			resultR = YunxinIM.sendChatRoomMsg(roomId, uuid, "1b8997342bed4798963c6438fdf343b5", "100", "0", "", new JsonUtils().toJson(yxChatMsg));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (!"200".equals(resultR.getCode())) {
//			System.out.println(CodeToString.getString(resultR.getCode()));
//		}
//		
		return rds;
	}

	@Override
	public ResultDataSet channelNotice(String cId) {
		ResultDataSet rds = new ResultDataSet();
		TaskChannelNotice taskChannelNotice = new TaskChannelNotice(0, accountSecDao, channelSecDao, 
				channelNoticeSecDao,accountConfigSecDao, cId, sender, jedisUtils);
		myThreadPool.getLowPrioritytPool().execute(taskChannelNotice);
		return rds;
	}
}
