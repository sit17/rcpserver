package com.i5i58.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.record.ChannelOpeRecord;
import com.i5i58.data.record.ChannelOpeType;
import com.i5i58.primary.dao.record.ChannelOpeRecordPriDao;

@Component
public class ChannelOpeRecordUtil {
	
	@Autowired
	private ChannelOpeRecordPriDao channelOpeRecordPriDao;
	
	public void createChannel(String cId, String topGId, String operatorId, boolean bySuper){
		ChannelOpeRecord record = new ChannelOpeRecord();
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (topGId != null){			
			record.setTopGId(topGId);
		}
		record.setSourceGId("");
		record.setTargetGId("");
		record.setDatetime(DateUtils.getNowTime());
		record.setAnchorId("");
		if (cId != null){			
			record.setcId(cId);
		}
		if (bySuper){
			record.setOpeType(ChannelOpeType.SuperCreateChannel.getValue());
		}else{
			record.setOpeType(ChannelOpeType.CreateChannel.getValue());
		}
		
		channelOpeRecordPriDao.save(record);
	}
	
	public void assignOwner(String cId, String anchorId, String operatorId, String topGId, boolean bySuper){
		ChannelOpeRecord record = new ChannelOpeRecord();
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (topGId != null){			
			record.setTopGId(topGId);
		}
		record.setSourceGId("");
		record.setTargetGId("");
		record.setDatetime(DateUtils.getNowTime());
		if (anchorId != null){			
			record.setAnchorId(anchorId);
		}
		if (cId != null){			
			record.setcId(cId);
		}
		if(bySuper){
			record.setOpeType(ChannelOpeType.SuperAssignOwner.getValue());
		}else{			
			record.setOpeType(ChannelOpeType.AssignOwner.getValue());
		}
		channelOpeRecordPriDao.save(record);
	}
	
//	public void superAssignOwner(String cId, String anchorId, String operatorId, String topGId){
//		ChannelOpeRecord record = new ChannelOpeRecord();
//		if (operatorId != null){			
//			record.setOperatorId(operatorId);
//		}
//		if (topGId != null){			
//			record.setTopGId(topGId);
//		}
//		record.setSourceGId("");
//		record.setTargetGId("");
//		record.setDatetime(DateUtils.getNowTime());
//		if (anchorId != null){			
//			record.setAnchorId(anchorId);
//		}
//		if (cId != null){			
//			record.setcId(cId);
//		}
//		record.setOpeType(ChannelOpeType.SuperAssignOwner.getValue());
//		channelOpeRecordPriDao.save(record);
//	}
	
	
	public void removeOwner(String cId, String oldAnchorId, String operatorId, String topGId){
		ChannelOpeRecord record = new ChannelOpeRecord();
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (topGId != null){			
			record.setTopGId(topGId);
		}
		if (cId != null){			
			record.setcId(cId);
		}
		record.setSourceGId("");
		record.setTargetGId("");
		record.setDatetime(DateUtils.getNowTime());
		if (oldAnchorId != null){			
			record.setAnchorId(oldAnchorId);
		}
		
		record.setOpeType(ChannelOpeType.RemoveOwner.getValue());
		channelOpeRecordPriDao.save(record);
	}
	
	public void changeGroup(String cId, String sourceGId, String targetGId, String operatorId, String topGId){
		ChannelOpeRecord record = new ChannelOpeRecord();
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (topGId != null){			
			record.setTopGId(topGId);
		}
		if (cId != null){			
			record.setcId(cId);
		}
		if (sourceGId != null){			
			record.setSourceGId(sourceGId);
		}
		if (targetGId != null){			
			record.setTargetGId(targetGId);
		}
		record.setDatetime(DateUtils.getNowTime());
		record.setAnchorId("");
		record.setOpeType(ChannelOpeType.ChangeGroup.getValue());
		channelOpeRecordPriDao.save(record);	
	}
}
