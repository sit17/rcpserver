package com.i5i58.primary.dao.account;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.UserTaskRecord;

@Transactional
public interface UserTaskRecordPriDao extends CrudRepository<UserTaskRecord, String> {
	List<UserTaskRecord> findByTaskTypeAndAccIdAndCompleteDate(int taskType, String accId, long completeDate);
}
