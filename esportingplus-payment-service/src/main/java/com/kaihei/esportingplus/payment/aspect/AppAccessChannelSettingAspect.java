package com.kaihei.esportingplus.payment.aspect;

import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.enums.AppSettingStatus;
import com.kaihei.esportingplus.payment.api.enums.PayChannelEnum;
import com.kaihei.esportingplus.payment.api.enums.PayChannelStatus;
import com.kaihei.esportingplus.payment.data.jpa.repository.AlipaySettingRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.CapaySettingRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.PayChannelRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.TenpaySettingReppository;
import com.kaihei.esportingplus.payment.domain.entity.AlipaySetting;
import com.kaihei.esportingplus.payment.domain.entity.CapaySetting;
import com.kaihei.esportingplus.payment.domain.entity.PayChannel;
import com.kaihei.esportingplus.payment.domain.entity.TenpaySetting;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 支付应用权限及支付渠道权限控制，渠道配置注入
 *
 * @author haycco
 */
@Aspect
@Configuration
public class AppAccessChannelSettingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AppAccessChannelSettingAspect.class);
    private static final CacheManager cacheManager = CacheManagerFactory.create();
    private static final String PAY_SETTING_KEY_PREFIX = "payment:pay_setting:";

    @Autowired
    private PayChannelRepository payChannelRepository;

    @Autowired
    private AlipaySettingRepository alipaySettingRepository;

    @Autowired
    private TenpaySettingReppository tenpaySettingReppository;

    @Autowired
    private CapaySettingRepository capaySettingRepository;

    /**
     * 切入点为加了 @CheckAndAutowireChannelSetting 注解
     */
    @Pointcut("@annotation(com.kaihei.esportingplus.payment.annotation.CheckAndAutowireChannelSetting)")
    public void CheckAndAutowireChannelSettingAspect() {
    }

    @Around("CheckAndAutowireChannelSettingAspect()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //Advice
        logger.info(" Allowed execution for {}", proceedingJoinPoint);
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 参数名
        String[] parameterNames = methodSignature.getParameterNames();
        // 参数类型
        Class[] parameterTypes = methodSignature.getParameterTypes();
        // 参数值对象
        Object[] parameterValues = proceedingJoinPoint.getArgs();

        Object result = null;

        String appId = String.valueOf(parameterValues[1]);
        String channelTag = String.valueOf(parameterValues[2]);
        Object channelSetting = String.valueOf(parameterValues[3]);

        PayChannel payChannel = payChannelRepository.findOneByTagAndAppSettingsAppId(channelTag, appId);
        if (payChannel == null) {
            throw new BusinessException(BizExceptionEnum.UNSUPPORT_PAY_CHANNEL);
        }
        if (!payChannel.getAppSettings().iterator().hasNext()) {
            throw new BusinessException(BizExceptionEnum.UNAUTH_PAY_CHANNEL);
        }
        if (AppSettingStatus.CLOSE == AppSettingStatus
                .valueOf(payChannel.getAppSettings().iterator().next().getState())) {
            throw new BusinessException(BizExceptionEnum.APP_PAY_CHANNEL_IS_CLOSED);
        }
        if (PayChannelStatus.DISABLE == PayChannelStatus.valueOf(payChannel.getState())) {
            throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_IS_DISABLE);
        }

        PayChannelEnum channel = PayChannelEnum.lookup(channelTag);
        try {
            switch (channel) {
                case ALI_APP_PAY:
                case ALI_H5_PAY:
                    logger.info("query {} setting... ", channel.getName());
                    AlipaySetting alipaySetting = cacheManager
                            .get(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), AlipaySetting.class);
                    if (alipaySetting == null) {
                        alipaySetting = alipaySettingRepository.findOneByChannelId(payChannel.getId());
                        if (alipaySetting == null) {
                            throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_SETTING_NOT_CONFIG);
                        }
                        cacheManager.set(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), alipaySetting, -1);
                        logger.info("{}-支付渠道配置信息已缓存", channel.getName());
                    }
                    parameterValues[3] = alipaySetting;
                    logger.info("{}-支付渠道配置信息已注入", channel.getName());
                    break;
                case WECHAT_APP_PAY:
                case WECHAT_INNER_H5_PAY:
                case WECHAT_OUTER_H5_PAY:
                    TenpaySetting tenpaySetting = cacheManager
                            .get(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), TenpaySetting.class);
                    if (tenpaySetting == null) {
                        tenpaySetting = tenpaySettingReppository.findOneByChannelId(payChannel.getId());
                        if (tenpaySetting == null) {
                            throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_SETTING_NOT_CONFIG);
                        }
                        cacheManager.set(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), tenpaySetting, -1);
                        logger.info("{}-支付渠道配置信息已缓存", channel.getName());
                    }
                    parameterValues[3] = tenpaySetting;
                    logger.info("{}-支付渠道配置信息已注入", channel.getName());
                    logger.info("query {} setting... ", channel.getName());
                    break;
                case WECHAT_PA_PAY:
                    logger.info("query {} setting... ", channel.getName());
                    break;
                case WECHAT_MP_PAY:
                    logger.info("query {} setting... ", channel.getName());
                    break;
                case CLOUD_ACCOUNT_PAY:
                    logger.info("query {} setting... ", channel.getName());
                    CapaySetting capaySetting = cacheManager
                            .get(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), CapaySetting.class);
                    if (capaySetting == null) {
                        capaySetting = capaySettingRepository.findOneByChannelId(payChannel.getId());
                        if (capaySetting == null) {
                            throw new BusinessException(BizExceptionEnum.PAY_CHANNEL_SETTING_NOT_CONFIG);
                        }
                        cacheManager.set(PAY_SETTING_KEY_PREFIX + channel.getValue().toLowerCase(), capaySetting, -1);
                        logger.info("{}-支付渠道配置信息已缓存", channel.getName());
                    }
                    parameterValues[3] = capaySetting;
                    logger.info("{}-支付渠道配置信息已注入", channel.getName());
                    break;
                default:
                    break;
            }

            result = proceedingJoinPoint.proceed(parameterValues);
        } catch (Throwable throwable) {
            logger.error("aop exception : {} ", throwable.getMessage());
            throwable.printStackTrace();
            throw throwable;
        }
        return result;
    }
}
