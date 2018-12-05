package com.kaihei.esportingplus.trade.api.params;


import java.util.List;
import lombok.Data;

@Data
public class StudioUserComplitedBossOrderStatisticGetParams {

    /**
     * 工作室Id
     */
    private String studioId;

    /**
     * 工作室暴鸡uid集合
     */
    private List<String> uids;
}
