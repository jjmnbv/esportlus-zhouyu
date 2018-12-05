package com.kaihei.esportingplus.trade.domain.entity;

import lombok.Data;

@Data
public class TeamOrderCount {

    /**
     * 车队唯一标识
     */
    private String teamSequeue;

    /**
     * 统计
     */
    private Long count;
}
