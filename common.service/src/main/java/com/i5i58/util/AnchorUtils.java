package com.i5i58.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.anchor.AnchorPushRecord;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.primary.dao.anchor.AnchorPushRecordPriDao;
import com.i5i58.secondary.dao.anchor.AnchorPushRecordSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;

@Component
public class AnchorUtils {
	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	@Autowired
	AnchorPushRecordPriDao anchorPushRecordPriDao;
	
	@Autowired
	AnchorPushRecordSecDao anchorPushRecordSecDao;
	
	// 验证是否和其他工会签约了
	public boolean isAnchorInOtherTopGroup(String topGroupId, String accId) {
		List<AnchorContract> anchorContracts = anchorContractSecDao.findByAccId(accId);
		for (AnchorContract ac : anchorContracts) {
			if(ac.getStatus() == AnchorContractStatus.AGREED.getValue()
					|| ac.getStatus() == AnchorContractStatus.REQUEST_CANCEL.getValue()){
				if (!ac.getgId().equals(topGroupId)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 获取主播有效的合约，状态为Agree, 或者RequestCancel
	 * */
	public AnchorContract getAnchorContract(String accId){
		AnchorContract anchor = anchorContractSecDao.findByAccIdAndStatus(accId, AnchorContractStatus.AGREED.getValue());
		if (anchor == null){
			anchor = anchorContractSecDao.findByAccIdAndStatus(accId, AnchorContractStatus.REQUEST_CANCEL.getValue());
		}
		return anchor;
	}
	
	/**
	 * 查询公会下所有有效合约
	 * */
	public List<AnchorContract> getActiveContractByGId(String gId){
		List<AnchorContract> contracts = anchorContractSecDao.findByGIdAndTwoStatus(gId, 
				AnchorContractStatus.AGREED.getValue(), AnchorContractStatus.REQUEST_CANCEL.getValue());;
		return contracts;
	}
	public void anchorStart(String accId, String cId){
		//先关后开
		anchorClose(accId, cId);
		AnchorPushRecord anchorPushRecord = new AnchorPushRecord();
		anchorPushRecord.setAccId(accId);
		anchorPushRecord.setcId(cId);
		anchorPushRecord.setOpenTime(DateUtils.getNowTime());
		anchorPushRecordPriDao.save(anchorPushRecord);
	}
	
	public void anchorClose(String accId, String cId){
		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordPriDao.findUnClosedOpenRecord(accId, cId);
		if (anchorPushRecords == null || anchorPushRecords.size() == 0)
			return;
		long nowTime = DateUtils.getNowTime();
		for (AnchorPushRecord record : anchorPushRecords){
			record.setCloseTime(nowTime);
		}
		anchorPushRecordPriDao.save(anchorPushRecords);
	}
	
	public void onLiveOpenCallback(String accId, String cId, long openTime){
		AnchorPushRecord anchorPushRecord = new AnchorPushRecord();
		anchorPushRecord.setAccId(accId);
		anchorPushRecord.setcId(cId);
		anchorPushRecord.setOpenTime(openTime);
		anchorPushRecordPriDao.save(anchorPushRecord);
	}
	
	public void onLiveCloseCallback(String accId, String cId, long closeTime){
		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordPriDao.findUnClosedOpenRecordBefore(accId, cId, closeTime);
		if (anchorPushRecords == null || anchorPushRecords.size() == 0)
			return;
		//找到最近的一条开播记录
		AnchorPushRecord openRecord = null;
		for (AnchorPushRecord record : anchorPushRecords){
			if (openRecord == null){
				openRecord = record;
			}
			else if (openRecord.getOpenTime() < record.getOpenTime()){
				openRecord = record;
			}
		}
		openRecord.setCloseTime(closeTime);
		anchorPushRecordPriDao.save(openRecord);
	}
}
