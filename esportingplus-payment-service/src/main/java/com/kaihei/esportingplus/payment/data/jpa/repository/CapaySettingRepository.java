package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.CapaySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 云账户提现渠道配置表
 * @author: chenzhenjun
 * @create: 2018-11-08 17:01
 **/
public interface CapaySettingRepository extends JpaRepository<CapaySetting, Long>, JpaSpecificationExecutor<CapaySetting> {

    CapaySetting findOneByChannelId(Long channelId);
}
