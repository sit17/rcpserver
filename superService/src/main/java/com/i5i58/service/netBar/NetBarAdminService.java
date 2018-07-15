package com.i5i58.service.netBar;

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
import com.i5i58.apis.platform.INetBarAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.netBar.NetBarAgent;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAgentPriDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.util.web.IpUtils;

@Service(protocol = "dubbo")
public class NetBarAdminService implements INetBarAdmin {

	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	NetBarAgentPriDao netBarAgentPriDao;

	@Override
	public ResultDataSet addNetBar(String superAccId, String accId, String netBarName, String addr, String netBarIp,
			String agId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!IpUtils.isIP(netBarIp)) {
			rds.setMsg("请填写正确的ipv4地址");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		NetBarAccount nba = netBarAccountPriDao.findByNetIp(netBarIp);
		if (nba != null) {
			rds.setMsg("该网吧已存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		NetBarAccount nbAcc = new NetBarAccount();
		nbAcc.setnId(StringUtils.createUUID());
		nbAcc.setAddr(addr);
		nbAcc.setName(netBarName);
		nbAcc.setNetIp(netBarIp);
		nbAcc.setCreateTime(DateUtils.getNowTime());
		nbAcc.setOwnerId(accId);
		nbAcc.setAgId(agId);
		netBarAccountPriDao.save(nbAcc);
		superAdminUtils.superAdminRecord(superAccId, "添加网吧{accId:%s, netBarName:%s, addr:%s, netBarIp:%s}", accId,
				netBarName, addr, netBarIp);
		rds.setData(nbAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteNetBar(String superAccId, String nId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		NetBarAccount nbAcc = netBarAccountPriDao.findOne(nId);
		if (nbAcc == null) {
			rds.setMsg("该网吧不存在!");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		superAdminUtils.superAdminRecord(superAccId, "删除网吧{accId:%s, netBarName:%s, addr:%s, netBarIp:%s}",
				nbAcc.getOwnerId(), nbAcc.getName(), nbAcc.getAddr(), nbAcc.getNetIp());
		netBarAccountPriDao.delete(nbAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNetBar(int pageNum, int pageSize, boolean viewAll) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "addr");
		Sort sort1 = new Sort(Direction.fromString("desc"), "name");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		if (viewAll) {
			Page<NetBarAccount> data = netBarAccountPriDao.findAll(pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			Page<NetBarAccount> data = netBarAccountPriDao.findByNullity(false, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet nullityNetBar(String superAccId, String nId, boolean nullity) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		NetBarAccount nbAcc = netBarAccountPriDao.findOne(nId);
		if (nbAcc == null) {
			rds.setMsg("该网吧不存在!");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		nbAcc.setNullity(nullity);
		netBarAccountPriDao.save(nbAcc);
		superAdminUtils.superAdminRecord(superAccId, "禁用网吧(%s){accId:%s, netBarName:%s, addr:%s, netBarIp:%s}", nullity,
				nbAcc.getOwnerId(), nbAcc.getName(), nbAcc.getAddr(), nbAcc.getNetIp());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addNetBarAgent(String superAccId, String accId, String agentName, String area)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		NetBarAgent nbaa = netBarAgentPriDao.findByOwnerId(accId);
		if (nbaa != null) {
			rds.setMsg("代理已存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		NetBarAgent nbaAcc = new NetBarAgent();
		nbaAcc.setAgId(StringUtils.createUUID());
		nbaAcc.setName(agentName);
		nbaAcc.setArea(area);
		nbaAcc.setCreateTime(DateUtils.getNowTime());
		nbaAcc.setOwnerId(accId);
		netBarAgentPriDao.save(nbaAcc);
		superAdminUtils.superAdminRecord(superAccId, "添加网吧代理{accId:%s, agentName:%s, area:%s}", accId, agentName, area);
		rds.setData(nbaAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteNetBarAgent(String superAccId, String agId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		NetBarAgent nbaAcc = netBarAgentPriDao.findOne(agId);
		if (nbaAcc == null) {
			rds.setMsg("该网吧代理不存在!");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		superAdminUtils.superAdminRecord(superAccId, "删除网吧代理{accId:%s, agentName:%s, area:%s}", nbaAcc.getOwnerId(),
				nbaAcc.getName(), nbaAcc.getArea());
		netBarAgentPriDao.delete(nbaAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNetBarAgent(int pageNum, int pageSize, boolean viewAll) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "area");
		Sort sort1 = new Sort(Direction.fromString("desc"), "name");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		if (viewAll) {
			Page<NetBarAgent> data = netBarAgentPriDao.findAll(pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			Page<NetBarAgent> data = netBarAgentPriDao.findByNullity(false, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet nullityNetBarAgent(String superAccId, String agId, boolean nullity) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		NetBarAgent nbaAcc = netBarAgentPriDao.findOne(agId);
		if (nbaAcc == null) {
			rds.setMsg("该网吧不存在!");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		nbaAcc.setNullity(nullity);
		netBarAgentPriDao.save(nbaAcc);
		superAdminUtils.superAdminRecord(superAccId, "禁用网吧代理(%s){accId:%s, agentName:%s, area:%s}", nullity,
				nbaAcc.getOwnerId(), nbaAcc.getName(), nbaAcc.getArea());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryNetBarByAgent(String superAccId, String agId, int pageNum, int pageSize, boolean viewAll)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "addr");
		Sort sort1 = new Sort(Direction.fromString("desc"), "name");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		if (viewAll) {
			Page<NetBarAccount> data = netBarAccountPriDao.findByAgId(agId, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			Page<NetBarAccount> data = netBarAccountPriDao.findByNullityAndAgId(false, agId, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

}
