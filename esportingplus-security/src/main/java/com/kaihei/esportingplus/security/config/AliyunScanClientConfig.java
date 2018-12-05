package com.kaihei.esportingplus.security.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kaihei.esportingplus.common.config.AliYunScanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunScanClientConfig {

    @Autowired
    private AliYunScanConfig aliYunScanConfig;

    @Bean
    public IAcsClient setAliYunScanConfig() throws ClientException {
        IClientProfile profile = DefaultProfile
                .getProfile(aliYunScanConfig.getRegionId(), aliYunScanConfig.getAccessKeyId(),
                        aliYunScanConfig.getAccessKeySecret());
        DefaultProfile
                .addEndpoint(aliYunScanConfig.getEndpointName(), aliYunScanConfig.getRegionId(),
                        aliYunScanConfig.getProduct(), aliYunScanConfig.getDomain());
        return new DefaultAcsClient(profile);
    }
}
