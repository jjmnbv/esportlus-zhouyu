package com.kaihei.esportingplus.payment.service.impl;

import com.kaihei.esportingplus.common.algorithm.SnowFlake;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.AppSettingStatus;
import com.kaihei.esportingplus.payment.data.jpa.repository.AppSettingRepository;
import com.kaihei.esportingplus.payment.domain.entity.AppSetting;
import com.kaihei.esportingplus.payment.domain.entity.PayChannel;
import com.kaihei.esportingplus.payment.service.AppSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Set;

/**
 * @program: esportingplus
 * @description:
 * @author: xusisi
 * @create: 2018-11-17 17:58
 **/
public class AppSettingServiceImpl implements AppSettingService {

    private Logger logger = LoggerFactory.getLogger(AppSettingServiceImpl.class);

    @Autowired
    private AppSettingRepository appSettingRepository;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * @Description: 根据AppName判断是否存在
     * @Param: [appName]
     * @Return com.kaihei.esportingplus.payment.domain.entity.AppSetting
     * @Author: xusisi
     */
    public AppSetting checkAppNameIsExist(String appName) {
        AppSetting appSetting = appSettingRepository.findOneByAppName(appName);
        return appSetting;
    }

    @Override
    public void addAppSetting(AppSetting insertParam) {
        String appName = insertParam.getAppName();
        //判断appName是否已经存在
        AppSetting appSetting = this.checkAppNameIsExist(appName);

        if (appSetting != null) {
            logger.error("exception : {} >> appName : {} ", BizExceptionEnum.APP_SETTING_APP_NAME_IS_EXIST.getErrMsg(), appName);
            throw new BusinessException(BizExceptionEnum.APP_SETTING_APP_NAME_IS_EXIST);
        }

        //构建新的AppSetting
        appSetting = new AppSetting();
        String appId = String.valueOf(snowFlake.nextId());
        appSetting.setAppId(appId);
        appSetting.setAppName(appName);
        //appSetting 状态默认是关闭的
        appSetting.setState(AppSettingStatus.CLOSE.name());
        appSettingRepository.save(appSetting);

    }

    @Override
    public void updateAppSetting(AppSetting updateParams) {
        String appId = updateParams.getAppId();
        String appName = updateParams.getAppName();
        String state = updateParams.getState();
        Set<PayChannel> channels = updateParams.getPayChannels();
        AppSetting appSetting = appSettingRepository.findOneByAppId(appId);
        if (appSetting == null) {
            logger.error("没有对应的AppId {}", appId);
            throw new BusinessException(BizExceptionEnum.APP_SETTING_APP_ID_IS_NOT_EXIST);
        }

        AppSetting appSetting_name = this.checkAppNameIsExist(appName);
        if (appSetting_name != null && appSetting_name.getAppId() != appId) {
            logger.error("appName已经存在，请换一个新的appName");
            throw new BusinessException(BizExceptionEnum.APP_SETTING_APP_NAME_IS_EXIST);
        }


    }

    @Override
    public Page<AppSetting> getPageInfo(AppSetting appSetting) {
        return null;
    }
}
