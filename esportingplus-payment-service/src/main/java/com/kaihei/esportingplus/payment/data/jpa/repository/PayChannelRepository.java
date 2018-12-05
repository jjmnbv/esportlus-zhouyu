package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.PayChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 支付渠道数据仓库DAO
 *
 * @author xusisi, haycco
 **/
public interface PayChannelRepository extends JpaRepository<PayChannel, Long>, JpaSpecificationExecutor<PayChannel> {

    PayChannel findOneByTag(String tag);

    PayChannel findOneByTagAndAppSettingsAppId(String tag, String appId);

}
