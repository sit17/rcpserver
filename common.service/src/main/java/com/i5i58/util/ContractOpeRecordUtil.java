package com.i5i58.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.record.ContractOpeRecord;
import com.i5i58.data.record.ContractOpeType;
import com.i5i58.primary.dao.record.ContractOpeRecordPriDao;

@Component
public class ContractOpeRecordUtil {
	
	@Autowired
	private ContractOpeRecordPriDao contractOpeRecordPriDao;
	
	public void agreeSign(String anchorId, String gId){
		ContractOpeRecord record = new ContractOpeRecord();
		if (anchorId != null){
			record.setAnchorId(anchorId);
		}
		if (gId != null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		record.setOpeType(ContractOpeType.AgreeSign.getValue());
		contractOpeRecordPriDao.save(record);
	}
	
	public void refuseSign(String anchorId, String gId){
		ContractOpeRecord record = new ContractOpeRecord();
		if (anchorId != null){
			record.setAnchorId(anchorId);
		}
		if (gId != null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		record.setOpeType(ContractOpeType.RefuseSign.getValue());
		contractOpeRecordPriDao.save(record);
	}
	
	public void agreeCancel(String anchorId, String gId){
		ContractOpeRecord record = new ContractOpeRecord();
		if (anchorId != null){
			record.setAnchorId(anchorId);
		}
		if (gId != null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		record.setOpeType(ContractOpeType.AgreeCancel.getValue());
		contractOpeRecordPriDao.save(record);
	}
	
	public void refuseCancel(String anchorId, String gId){
		ContractOpeRecord record = new ContractOpeRecord();
		if (anchorId != null){
			record.setAnchorId(anchorId);
		}
		if (gId != null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		record.setOpeType(ContractOpeType.RefuseCancel.getValue());
		contractOpeRecordPriDao.save(record);
	}
	
	public void forceCancelContract(String anchorId,String gId,int direction){
		ContractOpeRecord record = new ContractOpeRecord();
		if(anchorId !=null){
			record.setAnchorId(anchorId);
		}
		if(gId!=null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		if(direction==1){
			record.setOpeType(ContractOpeType.ForceCancelContractByGroup.getValue());			
		}else{
			record.setOpeType(ContractOpeType.ForceCancelContractByAnchor.getValue());		
		}
		contractOpeRecordPriDao.save(record);	
	}
}
