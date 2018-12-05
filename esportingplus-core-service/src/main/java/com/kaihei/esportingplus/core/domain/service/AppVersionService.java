package com.kaihei.esportingplus.core.domain.service;

import com.kaihei.esportingplus.core.api.enums.AppVersionSaveTypeEnum;
import com.kaihei.esportingplus.core.api.params.PageParams;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.api.vo.AppVersionNotifyVo;
import com.kaihei.esportingplus.core.api.vo.PageInfo;

/**
 * 版本管理服务
 *
 * @author yangshidong
 * @date 2018/12/1
 */
public interface AppVersionService {
    /**
     * 保存版本更新记录
     *
     * @param appVersionChangelogVo
     * @param appVersionSaveTypeEnum 保存类型枚举
     */
    void saveAppVersion(AppVersionChangelogVo appVersionChangelogVo, AppVersionSaveTypeEnum appVersionSaveTypeEnum);

    /**
     * 获取版本更新记录list
     *
     * @param pageParams
     * @return List<AppVersionChangelog>
     */
    PageInfo getAppVersionList(PageParams pageParams);

    /**
     * 获取版本升级弹窗通知vo
     *
     * @param clientType
     * @param version
     * @return {@link AppVersionNotifyVo}
     * */
    AppVersionNotifyVo getVersionNotify(short clientType, String version);
}
