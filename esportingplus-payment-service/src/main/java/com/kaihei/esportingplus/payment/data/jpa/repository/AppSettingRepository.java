package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 * 支付应用列表数据仓库DAO
 *
 * @author haycco
 **/
public interface AppSettingRepository extends JpaRepository<AppSetting, Long>, JpaSpecificationExecutor<AppSetting> {

    //    @Query("SELECT p FROM AppSetting p JOIN FETCH p.payChannels a WHERE p.appId=:appId")
    AppSetting findByAppId(@Param("appId") String appId);

    AppSetting findOneByAppName(String appName);

    AppSetting findOneByAppId(String appId);

}
