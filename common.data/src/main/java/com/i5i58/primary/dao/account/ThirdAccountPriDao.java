package com.i5i58.primary.dao.account;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.ThirdAccount;

public interface ThirdAccountPriDao extends CrudRepository<ThirdAccount, String> {

	ThirdAccount findByThirdTypeAndThirdId(int third, String thirdId);

}
