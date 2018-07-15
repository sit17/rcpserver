package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import com.i5i58.data.account.UserWatchRecord;

@Transactional
public interface UserWatchRecordSecDao extends CrudRepository<UserWatchRecord, String> {

}
