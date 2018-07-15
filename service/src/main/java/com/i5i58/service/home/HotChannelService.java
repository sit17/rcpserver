package com.i5i58.service.home;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.PageParams;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.home.IHotChannelOperate;
import com.i5i58.data.channel.Channel;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPage;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

/**
 * Srevice port for call
 * 
 * @author Ivan Dong
 *
 */
@Service(protocol = "dubbo")
public class HotChannelService implements IHotChannelOperate {

//	private Logger logger = Logger.getLogger(getClass());

	// Declare use of JsonUtils
	@Autowired
	JsonUtils jsonUtil;
	// Declare use of CFDao
	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	ChannelPriDao channelPriDao;
	
	@Autowired
	JedisUtils jedisUtils;

	/**
	 * get room by type
	 * 
	 * cache with name roomcache
	 */

	@Override
//	@Cacheable(value = "hotchannelcache", key = "'hotchannelcache_'+#key")
	public ResultDataSet getHotChannels(int type, int pageSize, int pageNum, String key) throws IOException {
		// System.out.println("If u see this, then i am reading DB!!!!!!");
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		if (pageSize < 10 && pageSize > 50) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.ILLEGAL.getCode());
			return rds;
		}
		String cacheKey = "channelrankcache_" + key;
		
		if (jedisUtils.exist(cacheKey)){
			String value = jedisUtils.get(cacheKey);
			if (!StringUtils.StringIsEmptyOrNull(value)){
				MyPage data = new JsonUtils().toObject(value, MyPage.class);
				// set code and message
				if(data != null){					
					rds.setData(data);
					rds.setCode(ResultCode.SUCCESS.getCode());
					rds.setMsg("Server Suc");
					return rds;
				}
			}
//			jedisUtils.del(cacheKey);
		}
		// double check
		// call DB operations
		if (1024 == type) {
			Sort sort = new Sort(Direction.fromString(PageParams.SORT_ASC), "status");
			Sort sort1 = new Sort(Direction.fromString(PageParams.SORT_DESC), "weekOffer");
			Sort sort2 = new Sort(Direction.fromString(PageParams.SORT_DESC), "brightness");
			Sort sort3 = new Sort(Direction.fromString(PageParams.SORT_DESC), "playerCount");
			Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1).and(sort2).and(sort3));
			Page<Channel> data = channelPriDao.findByStatusNotAndTypeNotAndTypeNotAndNullityAndOwnerIdNot(0, 0, 1000, false, "",
					pageable);

			// format data into JSON format
			if (null != data) {
				rds.setData(MyPageUtils.getMyPage(data));
			}
		} else {
			Sort sort = new Sort(Direction.fromString(PageParams.SORT_ASC), "status");
			Sort sort1 = new Sort(Direction.fromString(PageParams.SORT_DESC), "weekOffer");
			Sort sort2 = new Sort(Direction.fromString(PageParams.SORT_DESC), "brightness");
			Sort sort3 = new Sort(Direction.fromString(PageParams.SORT_DESC), "playerCount");
			Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1).and(sort2).and(sort3));
			Page<Channel> data = channelPriDao.findByStatusNotAndTypeAndNullityAndOwnerIdNot(0, type, false, "",
					pageable);

			// format data into JSON format
			if (null != data) {
				rds.setData(MyPageUtils.getMyPage(data));
			}
		}
		if (rds.getData() != null){
			String cacheValue = new JsonUtils().toJson(rds.getData());
			jedisUtils.set(cacheKey, cacheValue);
			jedisUtils.expire(cacheKey, 10);
//			jedisUtils.set(cacheKey, cacheValue, "NX", "EX", 10);
		}
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");

		return rds;
	}

//	@Cacheable(value = "hot_sorted_channel_ids_cache")
//	public List<String> getRankedChannels() {
//		List<String> channelIds = new ArrayList<String>();
//		List<HotChannel> openedChannel = hotChannelDao.findByStatus(ChannelStatus.OPEN.getValue());
//
//		return channelIds;
//	}
}
