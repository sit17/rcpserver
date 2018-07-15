package com.i5i58.secondary.dao.pay;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.pay.OnLineOrder;
import com.i5i58.data.pay.TotalPay;

/**
 * 
 * @author cw
 *
 */
public interface OnLineOrderSecDao extends PagingAndSortingRepository<OnLineOrder, String> {
	Page<OnLineOrder> findByAccId(String accId, Pageable pageable);

	@Query(value = "select o.accId, sum(o.orderAmount) as total from OnLineOrder o where o.orderStatus=2 group by o.accId having count(o.accId)>0")
	List<HashMap<String, Object>> orderByAmount();

	@Query(value = "select o.accId, sum(o.orderAmount) as total from OnLineOrder o where o.orderStatus=?1 group by o.accId having count(o.accId)>0 order by total desc")
	List<List<Object>> orderByAmount(int status, Pageable pageable);

	Page<OnLineOrder> findByOrderStatus(int status, Pageable pageable);
	
	@Query(value = "select new com.i5i58.data.pay.TotalPay(o.toAccId as accId, sum(o.orderAmount) as totalAmount) from OnLineOrder o where o.orderStatus=?1 group by o.toAccId order by totalAmount desc")	
	Page<TotalPay> findOrderByGroupAndParam(int status, Pageable pageable);
}
