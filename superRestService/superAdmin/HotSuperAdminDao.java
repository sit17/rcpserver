package com.i5i58.dao.superAdmin;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.superAdmin.HotSuperAdmin;


public interface HotSuperAdminDao extends CrudRepository<HotSuperAdmin, String> {

	public HotSuperAdmin findByOpenId(String openId);

	public HotSuperAdmin findByPhoneNo(String phoneNo);
}
