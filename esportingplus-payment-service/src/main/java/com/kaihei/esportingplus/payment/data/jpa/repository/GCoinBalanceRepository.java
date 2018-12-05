package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.GCoinBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GCoinBalanceRepository extends JpaRepository<GCoinBalance, Long>, JpaSpecificationExecutor<GCoinBalance> {
	
	List<GCoinBalance> findByUserId(String userId);

	GCoinBalance findOneByUserId(String userId);

    @Query("SELECT COUNT(*) FROM GCoinBalance gc WHERE gc.userId = :userId")
    int searchCount(@Param("userId") String userId);
	
}
