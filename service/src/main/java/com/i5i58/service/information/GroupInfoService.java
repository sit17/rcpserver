package com.i5i58.service.information;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.information.IGroupInfo;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.util.MyPageUtils;

@Service(protocol = "dubbo")
public class GroupInfoService implements IGroupInfo {

	@Autowired
	ChannelGroupPriDao channelGroupDao;

	@Override
	public ResultDataSet queryTopGroupList(String param, String sortStr, String dir, int pageSize, int pageNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString(dir), sortStr);
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGroup> cgs = channelGroupDao.findTopGroupByParam(param, pageable);
		rds.setData(MyPageUtils.getMyPage(cgs));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

}
