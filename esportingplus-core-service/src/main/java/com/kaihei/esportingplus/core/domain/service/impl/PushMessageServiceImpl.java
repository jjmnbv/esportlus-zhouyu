package com.kaihei.esportingplus.core.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.enums.FollowActionEnum;
import com.kaihei.esportingplus.core.api.enums.SendChannelEnum;
import com.kaihei.esportingplus.core.api.params.PushMessageParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.constant.MessageConstants;
import com.kaihei.esportingplus.core.data.repository.PushMessageRecordRepository;
import com.kaihei.esportingplus.core.data.repository.UserTagInfoRepository;
import com.kaihei.esportingplus.core.domain.entity.PushMessageRecord;
import com.kaihei.esportingplus.core.domain.entity.UserTagInfo;
import com.kaihei.esportingplus.core.domain.service.PushMessageService;
import com.kaihei.esportingplus.core.message.PushMessagePublish;
import com.kaihei.esportingplus.core.message.PushMessagePublishFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: esportingplus
 * @description: 消息推送serviceImpl
 * @author: xusisi
 * @create: 2018-12-01 12:11
 **/
@Service
public class PushMessageServiceImpl implements PushMessageService {

    private static Logger logger = LoggerFactory.getLogger(PushMessageServiceImpl.class);

    @Autowired
    private PushMessageRecordRepository pushMessageRecordRepository;

    @Autowired
    private UserTagInfoRepository userTagInfoRepository;

    @Override
    public Boolean createMessagePush(PushMessageParam pushMessageParam) {
        //保存到MySQL
        PushMessageRecord record = new PushMessageRecord();
        List<String> tagNames = pushMessageParam.getTagNames();
        //更加tags获取对应的每一个标签下的用户数量
        Set<String> uidSet = new HashSet<>();
        Set<String> tagNameSet = new HashSet<>();
        for (String tagName : tagNames) {
            UserTagInfo userTagInfo = userTagInfoRepository.getTagInfoByTagName(tagName);
            if (userTagInfo == null) {
                continue;
            }
            String uids = userTagInfo.getUids();
            List<String> uidList = FastJsonUtils.fromJsonToList(uids, String.class);
            uidSet.addAll(uidList);
            tagNameSet.add(tagName);
        }
        if (tagNameSet == null || tagNameSet.size() < 1) {
            logger.error("exception : {} ", BizExceptionEnum.PUSH_MESSAGE_TAG_HAS_NO_USER.getErrMsg());
            throw new BusinessException(BizExceptionEnum.PUSH_MESSAGE_TAG_HAS_NO_USER);
        }
        record.setSendNumber(uidSet.size());
        record.setTags(FastJsonUtils.toJson(tagNameSet));
        //去重以后的tagName
        pushMessageParam.setTagNames(new ArrayList<String>(tagNameSet));
        Integer sendTimes = pushMessageParam.getSendTimes();
        record.setSendTimes(sendTimes);
        List<Integer> sendChannels = pushMessageParam.getSendChannels();
        record.setSendChannels(FastJsonUtils.toJson(sendChannels));
        Integer followAction = pushMessageParam.getFollowAction();
        if (FollowActionEnum.APP_INDEX.getCode() != followAction) {
            String url = pushMessageParam.getUrl();
            if (StringUtils.isEmpty(url)) {
                logger.error("exception : {} ", BizExceptionEnum.PUSH_MESSAGE_URL_EMPTY.getErrMsg());
                throw new BusinessException(BizExceptionEnum.PUSH_MESSAGE_URL_EMPTY);
            }
            record.setUrl(url);
        }
        record.setFollowAction(followAction);
        Integer pushMode = pushMessageParam.getPushMode();
        record.setPushMode(pushMode);
        Integer pushType = pushMessageParam.getPushType();
        record.setPushType(pushType);
        Integer pushForm = pushMessageParam.getPushForm();
        record.setPushForm(pushForm);
        String title = pushMessageParam.getTitle();
        record.setTitle(title);
        String content = pushMessageParam.getContent();
        record.setContent(content);
        String imageUri = pushMessageParam.getImageUri();
        record.setImageUri(imageUri);
        record.setPlatforms(FastJsonUtils.toJson(pushMessageParam.getPlatforms()));
        record.setOperator(pushMessageParam.getOperator());
        //保存推送消息
        Integer id = pushMessageRecordRepository.insertRecord(record);
        pushMessageParam.setId(id);
        PushMessagePublishFactory factory = new PushMessagePublishFactory();
        PushMessagePublish pushMessagePublish = null;

        for (Integer channel : pushMessageParam.getSendChannels()) {
            if (SendChannelEnum.EXTERNAL_PUSH.getCode() == channel) {
                pushMessagePublish = factory.createMessagePublish(MessageConstants.Type.RON_YUN_PUSH);
                pushMessagePublish.sendMessage(pushMessageParam);
            }
            if (SendChannelEnum.INSIDE_MESSAGE.getCode() == channel) {
                pushMessagePublish = factory.createMessagePublish(MessageConstants.Type.PRIVATE);
                pushMessagePublish.sendSystemMessage(pushMessageParam);
            }
        }
        return true;

    }

    @Override
    public PageInfo<PushMessageRecord> getRecords(Integer currentPage, Integer size) {
        PageInfo pageInfo = new PageInfo();
        Page<PushMessageRecord> page = PageHelper
                .startPage(currentPage, size)
                .doSelectPage(() -> pushMessageRecordRepository.selectRecords());
        pageInfo.setDataList(page.getResult());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

}
