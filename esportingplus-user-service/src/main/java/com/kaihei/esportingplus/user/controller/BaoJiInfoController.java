package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.user.api.feign.BaoJiInfoServiceClient;
import com.kaihei.esportingplus.user.api.vo.PointDateVo;
import com.kaihei.esportingplus.user.domain.service.BaojiBaojiService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/baojiinfo")
@Api(tags = {"暴鸡基础服务接口"})
public class BaoJiInfoController implements BaoJiInfoServiceClient {

    @Autowired
    private BaojiBaojiService baojiBaojiService;

    /**
     * 获取暴鸡身份
     * @param uid
     * @return
     */
    @Override
    public ResponsePacket<Integer> getIdentityByUid(String uid) {
        return ResponsePacket.onSuccess(baojiBaojiService.getIdentityByUid(uid));
    }

    /**
     * 暴鸡中心-鸡分数据
     * @param uid
     * @return
     */
    @Override
    public ResponsePacket<PointDateVo> getUserPointDate(String uid) {
        if (StringUtils.isEmpty(uid)){
            uid = UserSessionContext.getUser().getUid();
        }
        return ResponsePacket.onSuccess(baojiBaojiService.getUserPointDate(uid));
    }
}
