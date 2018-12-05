package com.kaihei.esportingplus.core.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.core.api.feign.QiniuManageServiceClient;
import com.kaihei.esportingplus.core.api.params.QiniuQueryParam;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.core.api.vo.QiniuTokenVo;
import com.kaihei.esportingplus.core.domain.service.QiniuManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zl.zhao
 * @description:七牛管理API
 * @date: 2018/10/30 10:37
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuManageController implements QiniuManageServiceClient {
    @Autowired
    private QiniuManageService qiniuManageService;

    @Override
    public ResponsePacket<QiniuTokenVo> getTokenByTokenType(@RequestBody  QiniuQueryParam param) {
        return ResponsePacket.onSuccess(qiniuManageService.getTokenByTokenType(param.getTokenType(),
                                                            param.getGameOrderId(), param.getPictureNo(), param.getSerialNo()));
    }

    /**
     * 鉴暴恐
     *
     * @param param
     */
    @Override
    public ResponsePacket<QiniuImageCheckVo> checkQterrorImage(@RequestParam(value = "param", required = true) String param) {
        return ResponsePacket.onSuccess(qiniuManageService.checkQterrorImage(param));
    }

    /**
     * 鉴黄
     *
     * @param param
     */
    @Override
    public ResponsePacket<QiniuImageCheckVo> checkQpulpImage(@RequestParam(value = "param", required = true) String param) {
        return ResponsePacket.onSuccess(qiniuManageService.checkQpulpImage(param));
    }

    /**
     * （首次注册）上传头像获取token
     *
     * @param
     */
    @Override
    public ResponsePacket<QiniuTokenVo> getTokenByAvatar() {
        return ResponsePacket.onSuccess(qiniuManageService.getTokenByAvatar());
    }
}
