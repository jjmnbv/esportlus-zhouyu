package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.ConsumeGCoinParams;
import com.kaihei.esportingplus.payment.api.vo.WalletBillsVO;
import com.kaihei.esportingplus.payment.api.vo.WalletsVO;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户钱包相关服务API
 *
 * @author xiaolijun
 */
@FeignClient(name = "esportingplus-payment-service",
        path = "/wallets", fallbackFactory = WalletClientFallbackFactory.class)
public interface WalletServiceClient {

    /**
     * 用户消费暴鸡币进行打赏，对应暴鸡获得收益暴击值
     */
    @PostMapping("/{user_id}/reward/{order_id}")
    ResponsePacket consumeGCoin(@PathVariable("user_id") String userId,
            @PathVariable("order_id") String orderId);

    /**
     * 创建打赏预支付订单
     */
    @PostMapping("/{user_id}/reward")
    ResponsePacket<Map<String, String>> createRewardOrder(
            @PathVariable("user_id") String userId, @RequestBody ConsumeGCoinParams params);

    /**
     * 获取用户钱包信息服务接口
     */
    @GetMapping("/{user_id}")
    ResponsePacket<WalletsVO> getBalance(@PathVariable("user_id") String userId);

    /**
     * 获取用户钱包信息服务接口
     * 不带路径变量
     */
    @GetMapping("/balance")
    ResponsePacket<WalletsVO> getBalanceByApp(@RequestParam(value = "user_id", required = true) String userId);

    /**
     * 获取用户钱包流水信息接口
     *
     * @param moneyType 流水类型：暴击值类型、暴鸡币类型
     * @param size、page 分页参数
     */
    @GetMapping("/{user_id}/bills")
    ResponsePacket<List<WalletBillsVO>> getBills(@PathVariable("user_id") String userId,
            @RequestParam(value = "moneyType") String moneyType,
            @RequestParam(value = "page") String page,
            @RequestParam(value = "size") String size,
            @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
            @RequestParam(value = "orderDimension", required = false) String orderDimension,
            @RequestParam(value = "orderType", required = false) String orderType);


    /**
     * 获取用户累计暴击值
     *
     * @param userId 用户id
     * @return 累计暴击值
     */
    @GetMapping("/{user_id}/starlight")
    ResponsePacket<Map<String, Integer>> getStarlight(@PathVariable("user_id") String userId,
            @RequestParam("tradeType") String tradeType);

}
