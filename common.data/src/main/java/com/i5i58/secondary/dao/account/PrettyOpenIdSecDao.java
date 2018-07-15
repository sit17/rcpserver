package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.PrettyOpenId;


@Transactional
public interface PrettyOpenIdSecDao extends CrudRepository<PrettyOpenId, String> {

}
