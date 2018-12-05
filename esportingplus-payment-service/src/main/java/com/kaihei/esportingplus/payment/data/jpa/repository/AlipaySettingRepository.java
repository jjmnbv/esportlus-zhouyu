package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.AlipaySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: esportingplus
 * @description: 支付宝支付相关配置信息
 * @author: xusisi
 * @create: 2018-11-01 16:28
 **/
public interface AlipaySettingRepository extends JpaRepository<AlipaySetting, Long>, JpaSpecificationExecutor<AlipaySetting> {

    public AlipaySetting findOneByChannelId(Long channelId);
}
