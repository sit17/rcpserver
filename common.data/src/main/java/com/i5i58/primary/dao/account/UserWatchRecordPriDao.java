package com.i5i58.primary.dao.account;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import com.i5i58.data.account.UserWatchRecord;

@Transactional
public interface UserWatchRecordPriDao extends CrudRepository<UserWatchRecord, String> {

}
