package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.OrderIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author zhouyu
 * @Date 2018/9/22 15:01
 */
public interface OrderIncomeRepository extends JpaRepository<OrderIncome, Long> {

    OrderIncome findByIncomeOrdernumAndOrderType(String orderId, int orderType);

    OrderIncome  findByIncomeOrdernum(String orderNUm);

    @Query("SELECT SUM(orde.baojiIncome) FROM OrderIncome orde WHERE orde.userId= ?1 AND orde.createDate > ?2 AND orde.createDate < ?3")
    Integer queryLimitTime(String userId, Date date1, Date date2);

    @Query("SELECT  SUM(orin.baojiIncome) FROM OrderIncome orin where orin.userId= ?1 and orin.orderType= ?2 and orin.createDate > ?3 and orin.createDate< ?4 ")
    Integer queryWorkRoomIncome(String userId, int orderType, Date begintime, Date endtime);

}
