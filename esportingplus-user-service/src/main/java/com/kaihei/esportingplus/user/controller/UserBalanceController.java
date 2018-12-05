package com.kaihei.esportingplus.user.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.user.api.feign.UserBalanceServiceClient;
import com.kaihei.esportingplus.user.api.vo.UserBalanceResutVo;
import com.kaihei.esportingplus.user.domain.service.UserBalanceService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zl.zhao
 * @description: 用户暴击值控制类
 * @date: 2018/10/22 17:29
 */
@RestController
@RequestMapping("/balance")
public class UserBalanceController implements UserBalanceServiceClient {
    @Autowired
    private UserBalanceService userBalanceService;


    /**
     * 获取是否可兑换暴击币权限
     * @param uid
     * @return
     */
    @Override
    public ResponsePacket<UserBalanceResutVo> getExchangeAuthority(@RequestParam(value = "uid", required = true) String uid) {
        return  ResponsePacket.onSuccess(userBalanceService.getExchangeAuthority(uid));
    }
}
