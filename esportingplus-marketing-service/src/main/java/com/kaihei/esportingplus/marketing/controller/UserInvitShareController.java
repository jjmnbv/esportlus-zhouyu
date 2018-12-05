package com.kaihei.esportingplus.marketing.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.marketing.api.event.CoinConsumeEvent;
import com.kaihei.esportingplus.marketing.api.event.UserEventHandler;
import com.kaihei.esportingplus.marketing.api.event.UserRegistEvent;
import com.kaihei.esportingplus.marketing.api.feign.UserInvitShareServiceClient;
import javax.annotation.Resource;

import com.kaihei.esportingplus.marketing.api.vo.ShareVo;
import com.kaihei.esportingplus.marketing.domian.service.MarketUserShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: chen.junyong
 * @Date: 2018-10-15 15:10
 * @Description:
 */
@RestController
@RequestMapping("/invit")
public class UserInvitShareController implements UserInvitShareServiceClient {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "coinConsumeEventHandler")
    UserEventHandler coinConsumeEventHandler;

    @Resource(name = "marketUserRegistEventHandler")
    UserEventHandler marketUserRegistEventHandler;

    @Autowired
    private MarketUserShareService marketUserShareService;

    /**
     * 用户注册任务接口
     * @param event
     * @return
     */
    @Override
    public ResponsePacket shareTask(@RequestBody UserRegistEvent event) {
        if (StringUtils.isEmpty(event.getUid())) {
            logger.error("cmd=UserInvitShareController.shareTask | msg={}", "uid非法");
            return ResponsePacket.onError(BizExceptionEnum.PARAM_VALID_FAIL);
        }
        if (StringUtils.isEmpty(event.getInvitedUid())) {
            logger.error("cmd=UserInvitShareController.shareTask | msg={}", "invitedUid非法");
            return ResponsePacket.onError(BizExceptionEnum.PARAM_VALID_FAIL);
        }
        marketUserRegistEventHandler.handle(event);
        return ResponsePacket.onSuccess();
    }

    /**
     * 消费暴击币奖励接口
     * @param event
     * @return
     */
    @Override
    public ResponsePacket awardTask(@RequestBody CoinConsumeEvent event) {
        if (event.getUsers() == null || event.getUsers().isEmpty()) {
            logger.error("cmd=UserInvitShareController.shareTask | msg={}", "users非法");
            return ResponsePacket.onError(BizExceptionEnum.PARAM_VALID_FAIL);
        }
        if (event.getPayOrderNo() == null) {
            logger.error("cmd=UserInvitShareController.shareTask | msg={}", "payOrderNo非法");
            return ResponsePacket.onError(BizExceptionEnum.PARAM_VALID_FAIL);
        }

        coinConsumeEventHandler.handle(event);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<ShareVo> sharePoint(@RequestParam(value = "uid", required = false) String uid,
                                              @RequestParam(value = "shareuid" , required = false) String shareuid,
                                              @RequestParam(value = "type" , required = true) String type) {
        if (StringUtils.isEmpty(uid)) {
            uid = UserSessionContext.getUser().getUid();
        }
        if (StringUtils.isEmpty(shareuid)) {
            shareuid = UserSessionContext.getUser().getUid();
        }
        return ResponsePacket.onSuccess(marketUserShareService.getShareByType(uid,shareuid,type));
    }

}
