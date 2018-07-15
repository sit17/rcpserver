package com.i5i58.dao.superAdmin;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.superAdmin.SuperAdmin;

public interface SuperAdminDao extends PagingAndSortingRepository<SuperAdmin, String> {

	public SuperAdmin findByOpenId(String openId);

	public SuperAdmin findByPhoneNo(String phoneNo);

	public List<SuperAdmin> findByNullity(boolean nullity);
}
