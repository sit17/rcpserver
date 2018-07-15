package com.i5i58.service.social;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.ISongs;
import com.i5i58.data.account.Account;
import com.i5i58.data.social.HotSongs;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.redis.all.HotSongsDao;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.JsonUtils;

/**
 * Login Account Srevice port for call
 * 
 * @author Lee
 *
 */
@Service(protocol = "dubbo")
public class SongsService implements ISongs {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	HotSongsDao hotSongsDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	ChannelUtils channelUtils;

	@Override
	public ResultDataSet getSongs(String accId, String type, Integer pageSize, Integer pageNum, String sortBy,
			String sortDirection) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号有误");
			return rds;
		}

		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		// Sort sort = new Sort(Direction.fromString(sortDirection),
		// sortBy);
		// Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		// Page<HotSongs> data =
		// hotSongsDao.findHotChannelByUploadTime(dateString, pageable);

		List<HotSongs> hotSongsList = hotSongsDao.findByUploadTime(dateString);
		for (HotSongs hotSongs : hotSongsList) {
			channelUtils.addSongs(hotSongs, type);
		}
		int start = pageNum * pageSize;
		int end = pageNum * pageSize + pageSize - 1;
		if (end > 99) {
			end = 99;
		}
		Set<String> songsList = channelUtils.getSongs(start, end);
		List<HotSongs> songs = new ArrayList<HotSongs>();
		for (String uuId : songsList) {
			HotSongs song = hotSongsDao.findByUuId(uuId);
			if (song != null) {
				songs.add(song);
			}
		}
		rds.setData(songs);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet upLoadSongs(String accId, String url) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号有误");
			return rds;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HotSongs hotSongs = new HotSongs();
		hotSongs.setAccId(accId);
		hotSongs.setFilmUrl(url);
		hotSongs.setUploadTime(sdf.format(new Date()));
		hotSongsDao.save(hotSongs);
		return rds;
	}
}
