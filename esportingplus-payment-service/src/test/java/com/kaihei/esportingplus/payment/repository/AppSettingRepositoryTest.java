package com.kaihei.esportingplus.payment.repository;

import com.kaihei.esportingplus.payment.BaseTest;
import com.kaihei.esportingplus.payment.api.enums.AppSettingStatus;
import com.kaihei.esportingplus.payment.api.enums.PayChannelStatus;
import com.kaihei.esportingplus.payment.data.jpa.repository.AppSettingRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.PayChannelRepository;
import com.kaihei.esportingplus.payment.domain.entity.AppSetting;
import com.kaihei.esportingplus.payment.domain.entity.PayChannel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 应用列表管理及支付渠道管理数据仓库DAO测试
 *
 * @author haycco
 */
public class AppSettingRepositoryTest extends BaseTest {

    @Autowired
    private AppSettingRepository appSettingRepository;

    @Autowired
    private PayChannelRepository payChannelRepository;

    @Test
    public void testFindByAppId() {
        //新增应用
        AppSetting app1 = new AppSetting();
        app1.setAppId("app_01");
        app1.setAppName("APP 1");
        app1.setState(AppSettingStatus.OPEN.toString());
        app1 = appSettingRepository.save(app1);

        assertNotNull(app1.getId());

        //新增渠道
        PayChannel channel1 = new PayChannel();
        channel1.setName("Channel 1");
        channel1.setTag("TC001");
        channel1.setState(PayChannelStatus.ENABLE.toString());
        channel1 = payChannelRepository.save(channel1);

        assertNotNull(channel1.getId());

        //给App1绑定分配渠道Channel1
        app1.getPayChannels().add(channel1);
        app1 = appSettingRepository.save(app1);

        assertEquals(app1.getPayChannels().size(), 1);

        AppSetting app2 = appSettingRepository.findByAppId("app_01");
        assertEquals(app2.getId(), app1.getId());
        app2.getPayChannels().stream().forEach(channel -> {
            assertEquals(channel.getTag(), "TC001");
        });
    }

}
