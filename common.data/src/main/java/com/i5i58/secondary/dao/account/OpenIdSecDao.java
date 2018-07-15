package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.OpenId;


/**
 * OpenIdDao DB Operations
 * @author frank
 *
 */
@Transactional
public interface OpenIdSecDao extends CrudRepository<OpenId, String> {


}
