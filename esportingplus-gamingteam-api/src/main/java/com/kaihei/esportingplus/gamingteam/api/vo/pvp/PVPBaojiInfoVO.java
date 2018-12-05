package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.gamingteam.api.vo.BaojiInfoBaseVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创建暴鸡订单时需要的暴鸡信息 vo
 * @author liangyi
 */
public class PVPBaojiInfoVO extends BaojiInfoBaseVO {


    private static final long serialVersionUID = 1805453616385332242L;
    /**
     * 段位id
     */
    private Integer gameDanId;
    /**
     * 段位名称
     */
    private String gameDanName;

    public PVPBaojiInfoVO() {
    }


    public Integer getGameDanId() {
        return gameDanId;
    }

    public void setGameDanId(Integer gameDanId) {
        this.gameDanId = gameDanId;
    }

    public String getGameDanName() {
        return gameDanName;
    }

    public void setGameDanName(String gameDanName) {
        this.gameDanName = gameDanName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
