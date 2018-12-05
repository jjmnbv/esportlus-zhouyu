package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.core.api.feign.UserTagInfoServiceClient;
import com.kaihei.esportingplus.core.api.params.UserTagInfoParam;
import com.kaihei.esportingplus.core.api.vo.PageInfo;
import com.kaihei.esportingplus.core.domain.service.UserTagService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: esportingplus
 * @description: 用户标签controller
 * @author: xusisi
 * @create: 2018-12-03 17:01
 **/
@RestController
@Api(tags = {"用户标签水相关API"})
@RequestMapping("/user_tag")
public class UserTagInfoController implements UserTagInfoServiceClient {

    private Logger logger = LoggerFactory.getLogger(UserTagInfoController.class);

    @Autowired
    private UserTagService userTagService;

    /***
     * 创建tags
     */
    @Override
    public ResponsePacket createTagInfo(@RequestBody UserTagInfoParam userTagInfoParam) {
        logger.debug("userTagInfoParam : {} ", FastJsonUtils.toJson(userTagInfoParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userTagInfoParam);
        return ResponsePacket.onSuccess(userTagService.insertOrUpdateUserTagInfo(userTagInfoParam));
    }

    @Override
    public ResponsePacket updateTagInfo(@RequestBody UserTagInfoParam userTagInfoParam) {
        logger.debug("userTagInfoParam : {} ", FastJsonUtils.toJson(userTagInfoParam));
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, userTagInfoParam);
        return ResponsePacket.onSuccess(userTagService.insertOrUpdateUserTagInfo(userTagInfoParam));
    }

    /***
     * 获取tags列表
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponsePacket<PageInfo> getRecords(@RequestParam(value = "page", required = true) Integer page,
                                               @RequestParam(value = "size", required = true) Integer size) {
        logger.debug("page : {} ,size : {}", page, size);
        return ResponsePacket.onSuccess(userTagService.getTagsList(page, size));
    }

    /**
     * 更加tagName判断tag是否已经存在
     *
     * @param tagName
     * @return
     */
    @Override
    public ResponsePacket checkTagNameIsExist(@RequestParam(value = "tagName", required = true) String tagName) {
        logger.debug("tagName :{} ", tagName);
        return ResponsePacket.onSuccess(userTagService.checkTagNameIsExist(tagName));
    }

    @Override
    public ResponsePacket getTagInfo(@PathVariable Integer tagId) {
        logger.debug("tagId : {} ", tagId);
        return ResponsePacket.onSuccess(userTagService.selectById(tagId));
    }
}
