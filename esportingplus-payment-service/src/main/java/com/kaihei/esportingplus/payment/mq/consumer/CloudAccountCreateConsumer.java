package com.kaihei.esportingplus.payment.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.common.constant.HttpConstant;
import com.kaihei.esportingplus.common.constant.RedisKey;
import com.kaihei.esportingplus.common.constant.RocketMQConstant;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.payment.api.enums.CloudChannelEnum;
import com.kaihei.esportingplus.payment.api.enums.CloudReturnCodeEnum;
import com.kaihei.esportingplus.payment.api.enums.ExternalWithdrawStateEnum;
import com.kaihei.esportingplus.payment.api.enums.WithdrawVerifyEnum;
import com.kaihei.esportingplus.payment.api.vo.CloudCreateMessageVo;
import com.kaihei.esportingplus.payment.api.vo.WithdrawAuditListVo;
import com.kaihei.esportingplus.payment.config.CloudAccountConfig;
import com.kaihei.esportingplus.payment.data.jpa.repository.ExternalWithdrawOrderRepository;
import com.kaihei.esportingplus.payment.data.jpa.repository.WithdrawAuditRecordRepository;
import com.kaihei.esportingplus.payment.data.mongodb.repository.WithdrawVoucherRepository;
import com.kaihei.esportingplus.payment.domain.document.WithdrawVoucher;
import com.kaihei.esportingplus.payment.domain.entity.ExternalWithdrawOrder;
import com.kaihei.esportingplus.payment.domain.entity.WithdrawAuditRecord;
import com.kaihei.esportingplus.payment.service.WithdrawService;
import com.maihaoche.starter.mq.annotation.MQConsumer;
import com.maihaoche.starter.mq.base.AbstractMQPushConsumer;
import com.maihaoche.starter.mq.base.MessageExtConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 云账户提现订单入库-消息处理-消费者
 * @author chenzhenjun
 */
@MQConsumer(topic = RocketMQConstant.PAYMENT_TOPIC, tag = RocketMQConstant.EXTERNEL_CLOUD_CREATEORDER_TAG,
        consumerGroup = RocketMQConstant.EXTERNAL_CLOUD_CREATE_CONSUMER_GROUP)
public class CloudAccountCreateConsumer extends AbstractMQPushConsumer<CloudCreateMessageVo> {

    private static final String HUNDRED = "100";

    private static final Logger logger = LoggerFactory.getLogger(CloudAccountCreateConsumer.class);

    private static CacheManager cacheManager = CacheManagerFactory.create();

    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;

    @Autowired
    private ExternalWithdrawOrderRepository orderRepository;

    @Autowired
    private WithdrawVoucherRepository withdrawVoucherRepository;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private WithdrawAuditRecordRepository auditRecordRepository;

    @Transactional
    @Override
    public boolean process(CloudCreateMessageVo messageVo, Map<String, Object> map) {
        logger.debug("CloudAccountNotifyConsumer >> process >> message >> " + messageVo);
        logger.debug("CloudAccountNotifyConsumer >> process >> extMap >> " + map);
        try {
            //MQ消费次数：第一次为0，
            int consumeTimes = (int) map.get(MessageExtConst.PROPERTY_EXT_RECONSUME_TIMES);
            logger.debug("cloud >> create >> process >> reconsumeTimes : {} ", consumeTimes);

            String outTradeNo = messageVo.getOutTradeNo();
            String key = String.format(RedisKey.EXTERNAL_CLOUD_PAY_KEY, outTradeNo);
            ExternalWithdrawOrder orderEntity = cacheManager.get(key, ExternalWithdrawOrder.class);
            if (null == orderEntity) {
                logger.error("CloudAccountCreateConsumer >> consume >> Exception : "
                        + BizExceptionEnum.CLOUDPAY_ORDER_NOT_FUND.getErrMsg() + ",业务订单号为：{}", outTradeNo);
                throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_NOT_FUND);
            }

            // 构建加密参数
            Map<String, String> orginalParams = this.buildCloudParam(orderEntity, messageVo);
            String orginalParamsJson = FastJsonUtils.toJson(orginalParams);
            // 加密
            Map<String, String> data = null;
            try {
                data = CloudAccountConfig.encryptData(orginalParamsJson,
                        messageVo.getDescKey(), messageVo.getAppKey());
            } catch (Exception e) {
                logger.error("云账户创建提现订单异常，业务订单号为{}， 构建加密参数异常:【{}】", outTradeNo, e.getMessage());
                throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_ENCRYPT_ERROR);
            }
            logger.debug("云账户创建提现订单，加密过后的数据为{}", data.toString());
            // 调用第三方
            String ret = "";
            try {
                ret = this.postForCloud(data, messageVo.getCloudUrl(), messageVo.getDealerId());
            } catch (BusinessException e) {
                logger.error("云账户提现订单，业务订单号为{}，调用云账户接口异常:【{}】", outTradeNo, e.getMessage());
                throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_NETWORK_ERROR);
            }

            if (StringUtils.isEmpty(ret)) {
                logger.error("云账户下单，业务订单号为{}，Exception：{}", outTradeNo, BizExceptionEnum.CLOUDPAY_ORDER_RETURN_NULL);
                throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_RETURN_NULL);
            }
            JSONObject jsonObject = JSONObject.parseObject(ret);
            logger.debug(" >>云账户提现 >> thirdResponse  >> " + jsonObject.toString());

            WithdrawVoucher withdrawVoucher = new WithdrawVoucher();

            String code = jsonObject.getString("code");
            if (!CloudReturnCodeEnum.SUCCESS.getValue().equals(code)) {
                String retMessage = jsonObject.getString("message");
                String name = CloudReturnCodeEnum.lookup(code).getName();
                logger.error("云账户提现订单异常，业务订单号为{}，错误申明为 :【{}】，云账户具体返回为：【{}】", outTradeNo, name, retMessage);

                withdrawVoucher.setMetadata(jsonObject);
                withdrawVoucher.setState(ExternalWithdrawStateEnum.FAILED.getValue());

                // 正常业务失败，不是异常
                WithdrawAuditRecord record = auditRecordRepository.findByOrderId(outTradeNo);
                // 云账户直接返回错误-通知，直接返回失败
                WithdrawAuditListVo vo = new WithdrawAuditListVo();
                vo.setVerifyState(WithdrawVerifyEnum.FAIL.getValue());
                vo.setId(record.getId());
                withdrawService.updateAuditState(vo);

//                throw new BusinessException(BizExceptionEnum.CLOUDPAY_ORDER_RETURN_ERROR);
            } else {
                withdrawVoucher.setState(ExternalWithdrawStateEnum.PROCESSING.getValue());
                // 记录提现订单
                JSONObject dataResp = jsonObject.getJSONObject("data");
                String refNo = dataResp.getString("ref");
                orderEntity.setRefNo(refNo);
                orderEntity.setOutTradeNo(outTradeNo);
                orderEntity.setMessage(jsonObject.getString("message"));
                orderRepository.save(orderEntity);
                cacheManager.set(key, orderEntity, 24 * 60 * 60);
            }

            // 初始消息入MongoDB
            withdrawVoucher.setAppId(messageVo.getAppId());
            withdrawVoucher.setClientIp(messageVo.getIp());
            withdrawVoucher.setOrderId(orderEntity.getOrderId());
            withdrawVoucher.setTimestamp(LocalDateTime.now());
            withdrawVoucher.setRequestParams(orginalParamsJson);
            withdrawVoucher.setRequestUrl(messageVo.getCloudUrl());
            withdrawVoucherRepository.save(withdrawVoucher);

        } catch (Exception e) {
            logger.error("第三方支付创建订单消息消费失败：{}", e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 构建提现参数-原始数据
     *
     * @param orderParams
     * @param messageVo
     * @return
     */
    private Map<String, String> buildCloudParam(ExternalWithdrawOrder orderParams, CloudCreateMessageVo messageVo) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        String channel = messageVo.getChannel();

        paramMap.put("order_id", orderParams.getOutTradeNo());
        paramMap.put("dealer_id", messageVo.getDealerId());
        paramMap.put("broker_id", messageVo.getBrokerId());
        paramMap.put("real_name", orderParams.getRealName());
        paramMap.put("id_card", orderParams.getIdcardNumber());
        if (CloudChannelEnum.WECHAT.getValue().equals(channel)) {
            paramMap.put("openid", orderParams.getCardNo());
        } else if (CloudChannelEnum.ALI.getValue().equals(channel)) {
            paramMap.put("card_no", orderParams.getCardNo());
        }
        BigDecimal payAmount = new BigDecimal(orderParams.getTotalFee()).divide(new BigDecimal(HUNDRED));
        paramMap.put("pay", payAmount.toString());
        paramMap.put("notes", "开黑提现");
        if (CloudChannelEnum.ALI.getValue().equals(channel)) {
            paramMap.put("check_name", "NoCheck");
        }
        paramMap.put("notify_url", messageVo.getNotifyUrl());

        return paramMap;
    }

    /**
     * post请求调用云账户
     *
     * @param map
     * @param cloudUrl
     * @param dealerId
     * @return
     */
    private String postForCloud(Map<String, String> map, String cloudUrl, String dealerId) {
        String thirdResponse = "";
        String timestamp = map.get("timestamp");

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("data", map.get("data"));
        postParameters.add("mess", map.get("mess"));
        postParameters.add("timestamp", timestamp);
        postParameters.add("sign", map.get("sign"));
        postParameters.add("sign_type", map.get("sign_type"));

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(HttpConstant.CONTENT_TYPE_FORM);
        headers.setContentType(type);
        headers.set("dealer-id", dealerId);
        headers.set("request-id", dealerId + "-" + timestamp);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postParameters, headers);
        ResponseEntity<String> thirdResponseEntity = restTemplateExtrnal.exchange(cloudUrl, HttpMethod.POST, httpEntity, String.class);
        logger.info(" >>云账户提现下单 >> 全部数据为 >> " + thirdResponseEntity.getBody());
        if (HttpStatus.OK.equals(thirdResponseEntity.getStatusCode())) {
            thirdResponse = thirdResponseEntity.getBody();
        }

        return thirdResponse;
    }



}
