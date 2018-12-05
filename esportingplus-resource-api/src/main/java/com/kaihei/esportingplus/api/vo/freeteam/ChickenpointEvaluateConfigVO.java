package com.kaihei.esportingplus.api.vo.freeteam;

import lombok.Data;

@Data
public class ChickenpointEvaluateConfigVO {

    /**
     * 等级Id 来自字典
     */
    private Integer evaluateLevelId;
    /**
     * 等级名
     */
    private String evaluateLevelName;

    /**
     * 星级标识
     */
    private Integer evaluateLevelValue;
    /**
     * 获取鸡分
     */
    private Integer gainChickenPoint;

    /**
     * 是否起用
     */
    private Boolean enable;
}
