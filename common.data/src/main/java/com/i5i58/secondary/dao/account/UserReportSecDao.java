package com.i5i58.secondary.dao.account;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.UserReport;


@Transactional
public interface UserReportSecDao extends PagingAndSortingRepository<UserReport, Integer> {
	List<UserReport> findByReportedAccId(String reportedAccId);
}
