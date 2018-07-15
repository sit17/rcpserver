package com.i5i58.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.record.GroupOpeRecord;
import com.i5i58.data.record.GroupOpeType;
import com.i5i58.primary.dao.record.GroupOpeRecordPriDao;

@Component
public class GroupOpeRecordUtil {
	@Autowired
	private GroupOpeRecordPriDao groupOpeRecordPriDao;
	
	public void createTopGroup(String operatorId, String gId){
		GroupOpeRecord record = new GroupOpeRecord();
		record.setOpeType(GroupOpeType.CreateTopGroup.getValue());
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (gId != null){
			record.setgId(gId);
		}
		record.setDatetime(DateUtils.getNowTime());
		groupOpeRecordPriDao.save(record);
	}
	
	public void modifyGroupName(String operatorId, String gId, String oldName, String newName, String parentId){
		GroupOpeRecord record = new GroupOpeRecord();
					
		record.setOpeType(GroupOpeType.ModifyGroupName.getValue());
		if(gId != null){			
			record.setgId(gId);
		}
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (parentId != null){			
			record.setParentId(parentId);
		}
		record.setDatetime(DateUtils.getNowTime());
		if (oldName != null){			
			record.setOldName(oldName);
		}
		if (newName != null){			
			record.setNewName(newName);
		}
		groupOpeRecordPriDao.save(record);
	}
	
	public void modifyGroupDesc(String operatorId, String gId, String desc, String parentId){
		GroupOpeRecord record = new GroupOpeRecord();
		record.setOpeType(GroupOpeType.ModifyGroupDesc.getValue());
		if (gId != null){
			record.setgId(gId);
		}
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (parentId != null){			
			record.setParentId(parentId);
		}
		record.setDatetime(DateUtils.getNowTime());
		groupOpeRecordPriDao.save(record);
	}
	
	public void modifyGroupNotice(String operatorId, String gId, String notice, String parentId){
		GroupOpeRecord record = new GroupOpeRecord();
		record.setOpeType(GroupOpeType.ModifyGroupNotice.getValue());
		if (gId != null){			
			record.setgId(gId);
		}
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if(parentId != null){			
			record.setParentId(parentId);
		}
		record.setDatetime(DateUtils.getNowTime());
		groupOpeRecordPriDao.save(record);
	}
	
	public void createSubGroup(String operatorId, String gId, String parentId){
		GroupOpeRecord record = new GroupOpeRecord();
		record.setOpeType(GroupOpeType.CreateSubGroup.getValue());
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (gId != null){			
			record.setgId(gId);
		}
		if (parentId != null){			
			record.setParentId(parentId);
		}
		record.setDatetime(DateUtils.getNowTime());
		groupOpeRecordPriDao.save(record);
	}
	
	public void deleteSubGroup(String operatorId, String gId, String parentId){
		GroupOpeRecord record = new GroupOpeRecord();
		record.setOpeType(GroupOpeType.DeleteSubGroup.getValue());
		if (operatorId != null){			
			record.setOperatorId(operatorId);
		}
		if (gId != null){			
			record.setgId(gId);
		}
		if (parentId != null){			
			record.setParentId(parentId);
		}
		record.setDatetime(DateUtils.getNowTime());
		groupOpeRecordPriDao.save(record);
	}
}
