package com.kaihei.esportingplus.riskrating.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DingdingWarnBean {

    /**
     * 风控名
     */
    private String name;
    /**
     * 阀值
     */
    private Long threshold;

    /**
     * 已充值
     */
    private Long recharged;

    /**
     * 此次充值
     */
    private Integer recharge;
}
