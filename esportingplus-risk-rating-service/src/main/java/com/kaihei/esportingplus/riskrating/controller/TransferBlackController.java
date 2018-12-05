package com.kaihei.esportingplus.riskrating.controller;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.feign.TransferBlackClient;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.PageInfo;
import com.kaihei.esportingplus.riskrating.api.vo.TransferBlackListVo;
import com.kaihei.esportingplus.riskrating.service.TransferBlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferBlackController implements TransferBlackClient {

    @Autowired
    private TransferBlackService transferBlackService;

    @Override
    public ResponsePacket<PageInfo<TransferBlackListVo>> getTransferBlackList(@RequestParam(value = "user_id", required = false) String userId,
                                                                              @RequestParam(value = "page", defaultValue = "1") String page,
                                                                              @RequestParam(value = "size", defaultValue = "20") String size) {
        return ResponsePacket.onSuccess(transferBlackService.getTransferBlackList(userId, page, size));
    }

    @Override
    public ResponsePacket insertTransferBlack(@RequestBody TransferBlackListVo transferVo) {
        String userId = transferVo.getUserId();
        String remark = transferVo.getRemark();
        transferBlackService.insertTransferBlack(userId, remark);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket deleteTransferBlack(@RequestBody TransferBlackListVo transferVo) {
        String ids = transferVo.getIds();
        transferBlackService.deleteTransferBlack(ids);
        return ResponsePacket.onSuccess();
    }

    @Override
    public ResponsePacket<FreeTeamResponse> checkTransfer(@RequestParam(value = "user_id", required = false) String userId) {
        return ResponsePacket.onSuccess(transferBlackService.checkTransfer(userId));
    }
}
