package com.kaihei.esportingplus.riskrating.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.PageInfo;
import com.kaihei.esportingplus.riskrating.api.vo.TransferBlackListVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 风控需求-提现黑名单-Feign客户端
 *
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-risk-rating-service",
        path = "/transfer", fallbackFactory = TransferBlackFallbackFactory.class)
public interface TransferBlackClient {

    /**
     * 查询提现黑名单列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/blacklists")
    public ResponsePacket<PageInfo<TransferBlackListVo>> getTransferBlackList(@RequestParam(value = "user_id", required = false) String userId,
                                                                              @RequestParam(value = "page", defaultValue = "1") String page,
                                                                              @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 拉黑
     * @param transferVo
     * @return
     */
    @PostMapping("/blacklist")
    public ResponsePacket insertTransferBlack(@RequestBody TransferBlackListVo transferVo);

    /**
     * 移除
     * @param transferVo
     * @return
     */
    @DeleteMapping("/blacklist")
    public ResponsePacket deleteTransferBlack(@RequestBody TransferBlackListVo transferVo);


    /**
     * 查询用户是否在黑名单
     * @param userId
     * @return
     */
    @GetMapping("/check")
    public ResponsePacket<FreeTeamResponse> checkTransfer(@RequestParam(value = "user_id", required = false) String userId);

}
