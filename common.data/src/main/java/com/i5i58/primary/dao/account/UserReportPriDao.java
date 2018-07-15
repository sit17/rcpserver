package com.i5i58.primary.dao.account;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.UserReport;


@Transactional
public interface UserReportPriDao extends PagingAndSortingRepository<UserReport, Integer> {
	List<UserReport> findByReportedAccId(String reportedAccId);
}
