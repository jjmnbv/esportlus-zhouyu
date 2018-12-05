package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.feign.RiskRatingFreezeClient;
import com.kaihei.esportingplus.riskrating.api.params.RechargeFreezeUserParams;
import com.kaihei.esportingplus.riskrating.api.params.RechargeFreezedUserFindParams;
import com.kaihei.esportingplus.riskrating.api.params.UserRechargeFreezeParams;
import com.kaihei.esportingplus.riskrating.api.vo.RechargeFreezeUserVO;
import com.kaihei.esportingplus.riskrating.service.impl.RechargeFreezeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("freezz")
@Api("充值冻结")
public class RiskRatingFreezeController implements RiskRatingFreezeClient {

    @Resource
    private RechargeFreezeService rechargeFreezeService;

    /**
     * 冻结用户充值功能
     */
    @ApiOperation("冻结用户充值功能")
    @Override
    @PostMapping
    public ResponsePacket<Boolean> freezeUserRecharge(@RequestBody
            UserRechargeFreezeParams userRechargeFreezeParams) {
        return ResponsePacket
                .onSuccess(rechargeFreezeService.freezeUserRecharge(userRechargeFreezeParams));
    }

    /**
     * 解除用户充值冻结
     */
    @ApiOperation("解除用户充值冻结")
    @DeleteMapping("{uid}")
    @Override
    public ResponsePacket<Boolean> unfreezeUserRecharge(@PathVariable String uid) {
        return ResponsePacket.onSuccess(rechargeFreezeService.unfreezeUserRecharge(uid));
    }

    /**
     * 查询用户充值功能是否被冻结
     */
    @ApiOperation("查询用户充值功能是否被冻结")
    @PostMapping("freezedusers")
    public ResponsePacket<Map<String, Integer>> rechargeFreezedUsers(@RequestBody
            RechargeFreezeUserParams rechargeFreezeUserParams) {
        List<String> uids = rechargeFreezeUserParams.getUids();

        return ResponsePacket.onSuccess(rechargeFreezeService.rechargeFreezedUsers(uids));
    }

    /**
     * 分页查询冻结的用户
     */
    @ApiOperation("分页查询冻结的用户")
    @GetMapping("freezedusers")
    public ResponsePacket<Page<RechargeFreezeUserVO>> freezedusers(
            RechargeFreezedUserFindParams rechargeFreezedUserFindParams) {
        return ResponsePacket.onSuccess(
                rechargeFreezeService.findFreezedUsersPage(rechargeFreezedUserFindParams));
    }

}
