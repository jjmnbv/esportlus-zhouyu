package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.StarlightBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StarlightBalanceRepository extends JpaRepository<StarlightBalance, Integer>, JpaSpecificationExecutor<StarlightBalance> {
	
	List<StarlightBalance> findByUserId(String userId);

	StarlightBalance findOneByUserId(String userId);
	
    @Query("SELECT COUNT(*) FROM StarlightBalance sb WHERE sb.userId = :userId")
    int searchCount(@Param("userId") String userId);

//    @Query(value = "select * from StarlightBalance sb where sb.userId in (:userIds)")
//    List<StarlightBalance> getUserBalacneList(@Param("userIds") List<String> userIds);

    List<StarlightBalance> findByUserIdIn(@Param("userIds") List<String> userIds);

}
