package com.kaihei.esportingplus.payment.api.feign;

import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.payment.api.params.WithdrawCreateParams;
import com.kaihei.esportingplus.payment.api.params.WithdrawUpdateParams;
import com.kaihei.esportingplus.payment.api.vo.PageInfo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawConfigVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 基于 feign 实现 暴击值提现兑换服务接口调用
 * 1. esportingplus-payment-service为服务名
 * 2. fallbackFactory指定断路器实现类
 *
 * @author chenzhenjun
 */
@FeignClient(name = "esportingplus-payment-service",
        path = "/withdraw", fallbackFactory = WithdrawClientFallbackFactory.class)
public interface WithdrawServiceClient {

    /**
     * APP端发起提现
     *
     * @param token
     * @param amount
     * @param appId
     * @param transferType
     * @return
     */
    @PostMapping("/balance/transfer")
    public ResponsePacket<Map<String, String>> createWithdrawAuditOrder(@RequestHeader(name = "Authorization") String token,
                                                                        @RequestParam(value = "amount", required = true) Integer amount,
                                                                        @RequestParam(value = "app_id", required = true) String appId,
                                                                        @RequestParam(value = "transfer_type", required = true) Integer transferType,
                                                                        HttpServletRequest httpServletRequest);

    /**
     * 接收提现请求
     *
     * @param moneyType            货币类型
     * @param withdrawCreateParams 入参
     * @return
     */
    @PostMapping("/{money_type}")
    public ResponsePacket<Map<String, String>> createWithdrawOrder(
            @PathVariable("money_type") String moneyType,
            @RequestBody WithdrawCreateParams withdrawCreateParams);

    /**
     * 修改提现订单状态
     *
     * @param moneyType            货币类型
     * @param withdrawUpdateParams 入参
     * @return
     */
    @PutMapping("/{money_type}")
    public ResponsePacket<Map<String, String>> updateWithdrawStatus(@PathVariable("money_type") String moneyType,
                                                                    @RequestBody WithdrawUpdateParams withdrawUpdateParams);

    /**
     * 查询工作室暴鸡的暴击值
     *
     * @param moneyType 货币类型
     * @param userIds   暴鸡IDs
     * @return
     */
    @GetMapping("/balance/{money_type}")
    public ResponsePacket getStarLightValues(@PathVariable("money_type") String moneyType,
                                             @RequestParam(value = "user_ids", required = true) String userIds);

    /**
     * 暴击值兑换暴鸡币
     *
     * @return
     */
    @PostMapping("/balance/exchange")
    public ResponsePacket<Map<String, String>> convertStarlightToGCoin(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(value = "amount", required = true) String amount,
            @RequestParam(value = "source_id", required = true) String sourceId,
            @RequestParam(value = "currency_type", required = true) String currencyType);

    /**
     * 查询提现订单状态
     *
     * @param orderId 提现订单ID
     * @param userId  用户ID
     * @return
     */
    @GetMapping("/{out_trade_no}")
    public ResponsePacket getWithdrawStatus(@PathVariable("out_trade_no") String outTradeNo,
                                            @RequestParam(value = "user_id", required = false) String userId,
                                            @RequestParam(value = "order_id", required = false) String orderId);

    /**
     * 查询兑换订单状态
     *
     * @param orderId 兑换订单ID
     * @param userId  用户ID
     * @return
     */
    @GetMapping("/exchange/{order_id}")
    public ResponsePacket getExchangeStatus(@PathVariable("order_id") String orderId,
                                            @RequestParam(value = "user_id", required = true) String userId);

    /**
     * 鸡分兑换暴击值
     *
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping("/balance/score")
    public ResponsePacket convertScoreToStarlight(@RequestParam(value = "user_id", required = true) String userId,
                                                  @RequestParam(value = "amount", required = true) int amount,
                                                  @RequestParam(value = "out_trade_no", required = true) String outTradeNo);

    /**
     * app端查询提现审核记录
     *
     * @return
     */
    @GetMapping("/app/audits")
    public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByApp(@RequestHeader(name = "Authorization") String token,
                                                                           @RequestParam(value = "page", defaultValue = "1") String page,
                                                                           @RequestParam(value = "size", defaultValue = "20") String size);

    /**
     * 后台查询提现审核记录
     * @return
     */
    @GetMapping("/backend/audits")
    public ResponsePacket<PageInfo<WithdrawAuditListVo>> getAuditListByBackend(@RequestParam(value = "uid", required = false) String uid,
                                                                               @RequestParam(value = "verify_state", required = false) String verifyState,
                                                                               @RequestParam(value = "order_id", required = false) String orderId,
                                                                               @RequestParam(value = "block_state", required = false) String blockState,
                                                                               @RequestParam(value = "page", defaultValue = "1") String page,
                                                                               @RequestParam(value = "size", defaultValue = "20") String size);


    /**
     * 审批提现申请
     * @param queryVo
     * @return
     */
    @PostMapping("/audit")
    public ResponsePacket updateAuditState(@RequestBody WithdrawAuditListVo queryVo);

    /**
     * 截停提现记录
     * @param queryVo
     * @return
     */
    @PostMapping("/block")
    public ResponsePacket updateBlockState(@RequestBody WithdrawAuditListVo queryVo);

    /**
     * 获取提现配置信息
     * @return
     */
    @GetMapping("/config")
    public ResponsePacket<WithdrawConfigVo> getWithdrawConfigVo();

    /**
     * 更新提现配置信息
     * @param configVo
     * @return
     */
    @PostMapping("/config")
    public ResponsePacket updateWithdrawConfig(@RequestBody WithdrawConfigVo configVo);
}
