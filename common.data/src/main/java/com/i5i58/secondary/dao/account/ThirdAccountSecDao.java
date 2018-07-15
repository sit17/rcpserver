package com.i5i58.secondary.dao.account;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.ThirdAccount;

public interface ThirdAccountSecDao extends CrudRepository<ThirdAccount, String> {

	ThirdAccount findByThirdTypeAndThirdId(int third, String thirdId);

}
