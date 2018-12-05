package com.kaihei.esportingplus.riskrating.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaihei.esportingplus.common.tools.FastJsonUtils;
import com.kaihei.esportingplus.riskrating.api.params.RechargeRiskParams;
import com.kaihei.esportingplus.riskrating.api.vo.DingDingResultVO;
import com.kaihei.esportingplus.riskrating.api.vo.RiskBaseResponse;
import com.kaihei.esportingplus.riskrating.declare.PreventAbleRiskDeclare;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDict;
import com.kaihei.esportingplus.riskrating.service.RiskDictService;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 风控字典设置及通知推送相关服务实现类
 *
 * @author 谢思勇
 */
@Service
public class RiskRatingService {

    private final Logger log = LoggerFactory.getLogger(RiskRatingService.class);
    /**
     * 字典：分组code
     */
    private final String DIC_GROUP_CODE = "RISK_RATING";

    /**
     * 字典：钉钉预警URL
     */
    private final String DIC_DINGDING_WEB_HOOK_CODE = "DINGDING_WEB_HOOK";

    @Resource
    private RiskDictService riskDictService;

    /**
     * 单个用户当日充值累加KEY
     *
     * 格式 riskrating:ios:用户Id:日期（20180922）
     */
    @Resource
    private RestTemplate restTemplate;
    @Autowired
    private List<PreventAbleRiskDeclare> preventAbleRiskDeclares;
    private Pattern regex = Pattern.compile("\\$\\{([\\w\\.]+)}");

    /**
     * 根据uid 及将要充值的gcoin
     *
     * 判断该次充值是否达到风险预估值
     */

    public RiskBaseResponse hasRisk(RechargeRiskParams rechargeRiskParams) {
        for (PreventAbleRiskDeclare riskDeclare : preventAbleRiskDeclares) {
            RiskBaseResponse riskBaseResponse = riskDeclare.hasRisk(rechargeRiskParams);
            if (riskBaseResponse != null) {
                return riskBaseResponse;
            }
        }
        return null;
    }

    /**
     * 发送钉钉预警
     */

    public DingDingResultVO send(String msg) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE,
                Collections.singletonList(MediaType.APPLICATION_JSON_UTF8_VALUE));

        HttpEntity<String> httpEntity = new HttpEntity<>(msg, headers);

        RiskDict riskDict = riskDictService
                .findByGroupCodeAndItemCode(DIC_GROUP_CODE, DIC_DINGDING_WEB_HOOK_CODE);
        return restTemplate
                .postForEntity(riskDict.getItemValue(), httpEntity, DingDingResultVO.class)
                .getBody();

    }

    /**
     * 发送钉钉预警
     *
     * { "msgtype": "text", "text": { "content": "风控预警提示： ${a.b} 当前IOS充值最大限额为：%d 元,
     * 已充值金额为：%d元,已达风控阀值。" } }
     */
    public DingDingResultVO send(String msg, Object param) {
        if (param != null) {
            JSONObject jsonObject = FastJsonUtils.toParamMap(param);
            Matcher matcher = regex.matcher(msg);
            while (matcher.find()) {
                String path = matcher.group(1);
                String s = jsonObject.getString(path);
                if (s != null) {
                    msg = msg.replaceAll("\\$\\{" + path + "}", s);
                }
            }
        }
        return send(msg);
    }
}

