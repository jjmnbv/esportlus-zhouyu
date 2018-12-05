package com.kaihei.esportingplus.gamingteam.api.vo.rpg;

import com.kaihei.esportingplus.gamingteam.api.vo.BaojiInfoBaseVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 创建暴鸡订单时需要的暴鸡信息 vo
 * @author liangyi
 */
public class RPGBaojiInfoVO extends BaojiInfoBaseVO {

    private static final long serialVersionUID = 5120533430242155582L;



    /**
     * 车队队员游戏角色ID
     */
    private Long gameRoleId;

    /**
     * 车队队员游戏角色名称
     */
    private String gameRoleName;

    /**
     * 副本位置 code
     */
    private Integer raidLocationCode;

    /**
     * 副本位置名称
     */
    private String raidLocationName;

    public RPGBaojiInfoVO() {
    }

    public Long getGameRoleId() {
        return gameRoleId;
    }

    public void setGameRoleId(Long gameRoleId) {
        this.gameRoleId = gameRoleId;
    }

    public String getGameRoleName() {
        return gameRoleName;
    }

    public void setGameRoleName(String gameRoleName) {
        this.gameRoleName = gameRoleName;
    }

    public Integer getRaidLocationCode() {
        return raidLocationCode;
    }

    public void setRaidLocationCode(Integer raidLocationCode) {
        this.raidLocationCode = raidLocationCode;
    }

    public String getRaidLocationName() {
        return raidLocationName;
    }

    public void setRaidLocationName(String raidLocationName) {
        this.raidLocationName = raidLocationName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
