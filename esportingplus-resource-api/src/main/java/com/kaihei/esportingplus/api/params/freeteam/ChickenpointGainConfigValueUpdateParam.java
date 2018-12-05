package com.kaihei.esportingplus.api.params.freeteam;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class ChickenpointGainConfigValueUpdateParam {

    /**
     * 是否支持获取鸡分 1支持 0不支持
     */
    @JsonProperty("is_able")
    private Integer suportGainChickenpoint;

    private List<JSONObject> data;
}
