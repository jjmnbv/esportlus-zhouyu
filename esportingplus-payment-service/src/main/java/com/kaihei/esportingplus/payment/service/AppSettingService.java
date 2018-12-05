package com.kaihei.esportingplus.payment.service;

import com.kaihei.esportingplus.payment.domain.entity.AppSetting;
import org.springframework.data.domain.Page;

public interface AppSettingService {

    public void addAppSetting(AppSetting appSetting);

    public void updateAppSetting(AppSetting appSetting);

    public Page<AppSetting> getPageInfo(AppSetting appSetting);
}
