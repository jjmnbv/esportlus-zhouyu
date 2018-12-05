package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 暴鸡 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PVPFreeBaseVO implements Serializable {

    private static final long serialVersionUID = 7438320551095250095L;
    /**
     * 用户uid
     */
    private  String uid;
    /**
     * 游戏段位Id
     */
    private Integer gameDanId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getGameDanId() {
        return gameDanId;
    }

    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
