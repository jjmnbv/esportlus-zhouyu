package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.payment.api.feign.AttachServiceClient;
import com.kaihei.esportingplus.payment.api.vo.AccountInfoVo;
import com.kaihei.esportingplus.payment.service.AttachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 附加服务相关接口
 *
 * @author xusisi
 * @create 2018-09-30 16:44
 **/
@RestController
@Api(tags = {"附件接口相关API"})
@RequestMapping("/attach")
public class AttachController implements AttachServiceClient {

    private static Logger logger = LoggerFactory.getLogger(AttachController.class);

    @Autowired
    private AttachService attachService;

    @Override
    @ApiOperation(value = "创建支付订单")
    public ResponsePacket<AccountInfoVo> checkAccount(@PathVariable String userId, @RequestParam String accountType, @RequestParam Integer amount) throws Exception {

        logger.debug("checkAccount >> start >> userId : {} ,accountType : {} ,amount : {} ", userId, accountType, amount);
        AccountInfoVo vo = null;
        try {

            vo = attachService.checkAccountInfo(accountType, userId, amount);

        } catch (BusinessException e) {
            logger.error("checkAccount >> exception : {} ", e.getErrMsg());
            return ResponsePacket.onError(e.getErrCode(), e.getErrMsg());
        }

        logger.debug("checkAccount >> end >> vo : {}", vo);
        return ResponsePacket.onSuccess(vo);
    }
}
