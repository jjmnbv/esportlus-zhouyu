package com.kaihei.esportingplus.customer.center.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.params.FeedbackListParams;
import com.kaihei.esportingplus.api.params.FeedbackSubmitParams;
import com.kaihei.esportingplus.api.vo.FeedbackVo;
import com.kaihei.esportingplus.api.vo.PageInfoVo;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.customer.center.constant.CustomerRedisKey;
import com.kaihei.esportingplus.customer.center.data.repository.FeedbackLogRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.FeedbackLog;
import com.kaihei.esportingplus.customer.center.domain.service.FeedBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 投诉与反馈服务实现类
 *
 * @author yangshidong
 * @date 2018/12/3
 */
@Service
public class FeedbackServiceImpl implements FeedBackService {
    Logger logger = LoggerFactory.getLogger(getClass());

    CacheManager cacheManager = CacheManagerFactory.create();

    @Autowired
    private FeedbackLogRepository feedbackLogRepository;

    @Override
    public void submitFeedback(FeedbackSubmitParams feedbackSubmitParams) {
        //获取当前日期的yyMMdd格式字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String now = simpleDateFormat.format(new Date());
        if (!cacheManager.exists(String.format(CustomerRedisKey.FEEDBACK_LOG_ID_DAY, now))) {
            cacheManager.set(String.format(CustomerRedisKey.FEEDBACK_LOG_ID_DAY, now), "0", 86400);
        }
        long id = cacheManager.incr(String.format(CustomerRedisKey.FEEDBACK_LOG_ID_DAY, now));
        String displayId = now + StringUtils.leftPad(id + "", 4, "0");
        logger.info("cmd=FeedbackServiceImpl>>submitEvaluate | feedbackSubmitParams={}", JacksonUtils.toJson(feedbackSubmitParams));
        UserSessionContext userSessionContext = UserSessionContext.getUser();
        String userId = userSessionContext.getUid() + "/" + userSessionContext.getChickenId() + "/" + userSessionContext.getUsername();
        FeedbackLog feedbackLog = FeedbackLog.builder()
                .content(feedbackSubmitParams.getContent())
                .createDatetime(new Date())
                .displayId(displayId)
                .handleStatus((short) 0)
                .phone(feedbackSubmitParams.getPhone())
                .url(feedbackSubmitParams.getUrl())
                .userId(userId)
                .build();
        int i = feedbackLogRepository.insertSelective(feedbackLog);
        if (i < 1) {
            cacheManager.decr(String.format(CustomerRedisKey.FEEDBACK_LOG_ID_DAY, now));
            logger.info("cmd=FeedbackServiceImpl>>submitFeedback>>insert feedbackLog failed | feedbackLog={}", JacksonUtils.toJson(feedbackLog));
            throw new BusinessException(BizExceptionEnum.CORE_FEEDBACK_INSERT_FAILED);
        }
    }

    @Override
    public PageInfoVo queryFeedbackList(FeedbackListParams feedbackListParams) {
        logger.info("cmd=FeedbackServiceImpl>>queryFeedbackList | feedbackListParams={}", JacksonUtils.toJson(feedbackListParams));
        String startDate = feedbackListParams.getStartDate().trim() + " 00:00:00";
        String endDate = feedbackListParams.getEndDate().trim() + " 23:59:59";
        Page<FeedbackVo> page = PageHelper
                .startPage(feedbackListParams.getPage(), feedbackListParams.getSize())
                .doSelectPage(() -> feedbackLogRepository.selectFeedBackVoList(startDate, endDate, feedbackListParams.getHandleStatus()));
        logger.info("cmd=FeedbackServiceImpl>>queryFeedbackList | feedbackListCount={}", page.getTotal());
        PageInfoVo pageInfoVo = new PageInfoVo();
        pageInfoVo.setDataList(page.getResult());
        pageInfoVo.setTotal(page.getTotal());
        return pageInfoVo;
    }
}
