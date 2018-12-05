package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ChickenpointGainConfigVo {

    /**
     * 免费车队类型主键id
     */
    @JsonProperty("fleet_id")
    private Integer freeTeamTypeId;

    /**
     * 免费车队名称
     */
    @JsonProperty(value = "fleet_name")
    private String name;

    /**
     * 所属游戏id,来自数据字典
     */
    private Integer gameId;

    /**
     * 玩法模式id,来自数据字典(上分/陪玩)
     */
    private Integer playMode;

    /**
     * 结算类型id,来自数据字典(小时/局数)
     */
    private Integer settlementType;

    /**
     * 可选的小时/局数
     */
    private Double settlementNumber;

    /**
     * 可组建的身份,来自数据字典(暴鸡/暴娘)
     */
    private Integer baojiIdentity;

    /**
     * 排序,值越大越靠前
     */
    private Integer orderIndex;


    /**
     * 是否支持获取鸡分 1支持 0不支持
     */
    @JsonProperty("is_able")
    private Integer suportGainChickenpoint;

    private List<Map<String, Object>> data;


}


