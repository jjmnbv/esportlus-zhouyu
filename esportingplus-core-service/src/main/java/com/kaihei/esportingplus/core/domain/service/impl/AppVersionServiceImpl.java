package com.kaihei.esportingplus.core.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.core.api.enums.AppVersionSaveTypeEnum;
import com.kaihei.esportingplus.core.api.params.PageParams;
import com.kaihei.esportingplus.core.api.vo.AppVersionChangelogVo;
import com.kaihei.esportingplus.core.api.vo.AppVersionNotifyVo;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.constant.VersionLogConstans;
import com.kaihei.esportingplus.core.data.repository.AppVersionChangelogRepository;
import com.kaihei.esportingplus.core.domain.entity.AppVersionChangelog;
import com.kaihei.esportingplus.core.domain.service.AppVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 版本管理服务实现类
 *
 * @author yangshidong
 * @date 2018/12/1
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppVersionChangelogRepository appVersionChangelogRepository;

    @Value("${version.notify.api.host}")
    private String apiHost;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAppVersion(AppVersionChangelogVo appVersionChangelogVo, AppVersionSaveTypeEnum appVersionSaveTypeEnum) {
        logger.info("cmd =AppVersionServiceImpl.saveAppVersion | params ={} | AppVersionSaveType ={}", JSON.toJSONString(appVersionChangelogVo), appVersionSaveTypeEnum.ordinal());
        //如果是保存以外的操作,则首先判断当前操作的日志记录是否被删除,删除则抛出异常
        if (appVersionSaveTypeEnum != AppVersionSaveTypeEnum.SAVE) {
            if (isDeleted(appVersionChangelogVo.getId())) {
                throw new BusinessException(BizExceptionEnum.CORE_VERSION_LOG_HAS_DELETED);
            }
        }
        AppVersionChangelog appVersionChangelog = new AppVersionChangelog();
        BeanUtils.copyProperties(appVersionChangelogVo, appVersionChangelog);
        switch (appVersionSaveTypeEnum) {
            case SAVE:
                appVersionChangelog.setCreateDatetime(new Date());
                appVersionChangelog.setUpdateDatetime(new Date());
                appVersionChangelog.setIsDeleted(false);
                appVersionChangelog.setIsEnabled(true);
                appVersionChangelog.setPackageName(VersionLogConstans.PACKAGE_NAME_KAIHEI);
                int i = appVersionChangelogRepository.insertSelective(appVersionChangelog);
                if (i < 1) {
                    logger.error("cmd =AppVersionServiceImpl.saveAppVersion | AppVersionChangeLog ={} | errMsg =save versionLog failed", JacksonUtils.toJson(appVersionChangelogVo));
                    throw new BusinessException(BizExceptionEnum.CORE_CREATE_VERSION_LOG_FAILED);
                }
                break;
            case UPDATE:
                AppVersionChangelog appVersionChangelogOld = appVersionChangelogRepository.selectByPrimaryKey(appVersionChangelog.getId());
                appVersionChangelog.setUpdateDatetime(new Date());
                appVersionChangelog.setIsDeleted(false);
                appVersionChangelog.setIsEnabled(true);
                BeanUtils.copyProperties(appVersionChangelog, appVersionChangelogOld, getNullPropertyNames(appVersionChangelog));
                i = appVersionChangelogRepository.updateByPrimaryKey(appVersionChangelogOld);
                if (i != 1) {
                    logger.error("cmd =AppVersionServiceImpl.saveAppVersion | params ={} | errMsg =update versionLog failed", JacksonUtils.toJson(appVersionChangelogVo));
                    throw new BusinessException(BizExceptionEnum.CORE_UPDATE_VERSION_LOG_FAILED);
                }
                break;
            case ENABLE:
                i = appVersionChangelogRepository.updateVersionLogStatus(1, appVersionChangelogVo.getId());
                if (i != 1) {
                    logger.error("cmd =AppVersionServiceImpl.saveAppVersion | params ={} | errMsg =enable versionLog failed", JacksonUtils.toJson(appVersionChangelogVo), appVersionSaveTypeEnum.ordinal());
                    throw new BusinessException(BizExceptionEnum.CORE_UPDATE_VERSION_LOG_FAILED);
                }
                break;
            case DISABLE:
                i = appVersionChangelogRepository.updateVersionLogStatus(2, appVersionChangelogVo.getId());
                if (i != 1) {
                    logger.error("cmd =AppVersionServiceImpl.saveAppVersion | params ={} | errMsg =disable versionLog failed", JacksonUtils.toJson(appVersionChangelogVo), appVersionSaveTypeEnum.ordinal());
                    throw new BusinessException(BizExceptionEnum.CORE_UPDATE_VERSION_LOG_FAILED);
                }
                break;
            case DELETE:
                i = appVersionChangelogRepository.updateVersionLogStatus(3, appVersionChangelogVo.getId());
                if (i != 1) {
                    logger.error("cmd =AppVersionServiceImpl.saveAppVersion | params ={} | errMsg =delete versionLog failed", JacksonUtils.toJson(appVersionChangelogVo), appVersionSaveTypeEnum.ordinal());
                    throw new BusinessException(BizExceptionEnum.CORE_UPDATE_VERSION_LOG_FAILED);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public PageInfo getAppVersionList(PageParams pageParams) {
        PageInfo pageInfo = new PageInfo();
        Page<AppVersionChangelogVo> page = PageHelper
                .startPage(pageParams.getPage(), pageParams.getSize())
                .doSelectPage(() -> appVersionChangelogRepository.selectChangelogVoList());
        pageInfo.setDataList(page.getResult());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public AppVersionNotifyVo getVersionNotify(short clientType, String version) {
        logger.info("cmd =AppVersionServiceImpl.getVersionNotify | clientType ={} | version ={}", clientType, version);
        AppVersionNotifyVo appVersionNotifyVo = new AppVersionNotifyVo();
        AppVersionChangelog appVersionChangelog = appVersionChangelogRepository.queryChangeLogByClientType(clientType);
        logger.info("cmd =AppVersionServiceImpl.getVersionNotify | the latest appVersionChangelog ={} ", JacksonUtils.toJson(appVersionChangelog));
        if (!version.equals(appVersionChangelog.getVersion())) {
            BeanUtils.copyProperties(appVersionChangelog, appVersionNotifyVo);
            String[] versionArray = version.split("\\.");
            String[] requireVersionArray = appVersionChangelog.getRequireVersion().split("\\.");
            //当前版本与最低版本进行比较,如果小于最低版本,则将强制更新设置为true
            for (int i = 0; i < versionArray.length; i++) {
                if (Integer.parseInt(versionArray[i]) < Integer.parseInt(requireVersionArray[i])) {
                    appVersionNotifyVo.setIsForcedUpdate(true);
                    break;
                }
            }
        }
        appVersionNotifyVo.setApiHost(apiHost);
        return appVersionNotifyVo;
    }

    /**
     * 非插入操作时，判断当前操作的日志记录是否被删除
     */
    private boolean isDeleted(int id) {
        return appVersionChangelogRepository.judgeIsDeleted(id);
    }

    /**
     * 获取对象属性中为null的属性
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
