package com.i5i58.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.vendor.IVendor;
import com.i5i58.data.account.Account;
import com.i5i58.data.vendor.VendorUserAction;
import com.i5i58.data.vendor.VendorUserActionType;
import com.i5i58.primary.dao.vendor.VendorUserActionPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.vendor.VendorUserActionSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class VendorService implements IVendor{
	
	@Autowired
	private VendorUserActionPriDao vendorUserActionPriDao;
	
	@Autowired
	private VendorUserActionSecDao vendorUserActionSecDao;
    
    @Autowired
    private AccountSecDao accountSecDao;
    
	@Override
	public ResultDataSet userAction(String vendorId, String accId, String adId, int actionId, String clientIp,
			String macAddress) {
		ResultDataSet rds =new ResultDataSet();

		
		if (actionId == VendorUserActionType.Register.getValue()){
			if (StringUtils.StringIsEmptyOrNull(accId)){
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("注册用户id不能为空");
				return rds;
			}
			Account vendorAccount = accountSecDao.findOne(accId);
			if (vendorAccount == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("用户不存在");
				return rds;
			}
		}
		
//		Vendor vendor = vendorSecDao.findOne(vendorId);
//		if (vendor == null){
//			rds.setCode(ResultCode.PARAM_INVALID.getCode());
//			rds.setMsg("运营商不存在");
//			return rds;
//		}
//		if (vendor.isNullity()){
//			rds.setCode(ResultCode.PARAM_INVALID.getCode());
//			rds.setMsg("运营商被禁用");
//			return rds;
//		}
		
		VendorUserAction vendorUserAction = new VendorUserAction();
		vendorUserAction.setVendorId(vendorId);
		vendorUserAction.setAccId(accId);
		vendorUserAction.setAdId(adId);
		vendorUserAction.setActionId(actionId);
		vendorUserAction.setClientIp(clientIp);
		vendorUserAction.setMacAddress(macAddress);
		vendorUserAction.setDateTime(DateUtils.getNowTime());
		vendorUserActionPriDao.save(vendorUserAction);
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet searchUserActions(String param, String type, int pageNum, int pageSize) {
		ResultDataSet rds=new ResultDataSet();
		Sort sort = new Sort(Direction.ASC, type);
		Pageable pageable =new PageRequest(pageNum, pageSize, sort);
		//vendorId adId actionId
		Page<VendorUserAction> vendorUserActions = null;
		if (type.equals("vendorId")){
			vendorUserActions = vendorUserActionSecDao.findByVendorId(param,pageable);
		}else if (type.equals("adId")){
			vendorUserActions = vendorUserActionSecDao.findByAdId(param,pageable);
		}else if (type.equals("actionId")){
			int actionId2 = Integer.parseInt(param);
			vendorUserActions = vendorUserActionSecDao.findByActionId(actionId2, pageable);
		}else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("类型不支持");
			return rds;
		}
		rds.setData(MyPageUtils.getMyPage(vendorUserActions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet searchUserActionsByTime(long from, long to, int pageNum, int pageSize) {
		ResultDataSet rds=new ResultDataSet();
		Sort sort = new Sort(Direction.DESC, "dateTime");
		Pageable pageable =new PageRequest(pageNum, pageSize, sort);
		Page<VendorUserAction> actions = vendorUserActionSecDao.findByTime(from, to, pageable);
		rds.setData(MyPageUtils.getMyPage(actions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet countUserActions(String param, String type) {
		ResultDataSet rds=new ResultDataSet();
		//vendorId adId actionId
		int count = 0;
		if (type.equals("vendorId")){
			count = vendorUserActionSecDao.countByVendorId(param);
		}else if (type.equals("adId")){
			count = vendorUserActionSecDao.countByAdId(param);
		}else if (type.equals("actionId")){
			int actionId2 = Integer.parseInt(param);
			count = vendorUserActionSecDao.countByActionId(actionId2);
		}else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("类型不支持");
			return rds;
		}
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet countUserActionsByTime(long from, long to) {
		ResultDataSet rds=new ResultDataSet();
		int count = vendorUserActionSecDao.countByTime(from, to);
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

}
