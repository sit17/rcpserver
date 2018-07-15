package com.i5i58.service.home;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.home.ICarousel;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.home.Carousel;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.home.CarouselPriDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JsonUtils;

/**
 * Srevice port for call
 * 
 * @author Ivan Dong
 *
 */
@Service(protocol = "dubbo")
public class CarouselService implements ICarousel {

	private Logger logger = Logger.getLogger(getClass());

	// Declare use of JsonUtils
	@Autowired
	JsonUtils jsonUtil;
	// Declare use of CFDao
	@Autowired
	CarouselPriDao cfd;

	@Autowired
	AccountPropertyPriDao accountPropertyDao;

	/**
	 * Data Operation
	 * http://blog.csdn.net/chenleixing/article/details/44815443 @Cacheable讲解
	 */
	@Override
	@Cacheable(value = "Carousel", key = "'Carousel_'+#device")
	public ResultDataSet getCarousel(String device) throws IOException {
		System.out.println("If u see this, then i am reading DB!!!!!!");
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		// double check
		// call DB operations
		List<Carousel> data = cfd.findByDeviceNow(Integer.parseInt(device), DateUtils.getNowTime());
		// format data into JSON format
		if (null != data) {
			rds.setData(data);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getRichScoreRanking() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "richScore");
		List<AccountProperty> myGames = accountPropertyDao.findAll(sort);
		rds.setData(myGames);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;

	}

}
