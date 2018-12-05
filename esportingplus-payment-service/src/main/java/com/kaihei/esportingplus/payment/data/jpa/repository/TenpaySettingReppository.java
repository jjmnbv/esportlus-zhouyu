package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: zhouyu
 * @Date: 2018/10/30 10:38
 * @Description:腾讯支付配置DAO
 */
public interface TenpaySettingReppository extends JpaRepository<TenpaySetting, Long>, JpaSpecificationExecutor<TenpaySetting> {

    TenpaySetting findByAppIdAndChannelId(String appId, String channelId);

    TenpaySetting findOneByChannelId(Long channelId);

}
