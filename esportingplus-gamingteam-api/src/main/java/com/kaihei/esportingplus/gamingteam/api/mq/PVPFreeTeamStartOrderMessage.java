package com.kaihei.esportingplus.gamingteam.api.mq;

import com.kaihei.esportingplus.gamingteam.api.vo.pvp.PVPFreeTeamStartOrderVO;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创建免费车队开车消息
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class PVPFreeTeamStartOrderMessage implements Serializable {

    private static final long serialVersionUID = -2877977575510330672L;
    /**
     * 暴鸡队员信息
     */
    private PVPFreeTeamStartOrderVO pvpTeamStartOrderVO;

    public PVPFreeTeamStartOrderMessage() {
    }

    public PVPFreeTeamStartOrderVO getPvpTeamStartOrderVO() {
        return pvpTeamStartOrderVO;
    }

    public void setPvpTeamStartOrderVO(
            PVPFreeTeamStartOrderVO pvpTeamStartOrderVO) {
        this.pvpTeamStartOrderVO = pvpTeamStartOrderVO;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
