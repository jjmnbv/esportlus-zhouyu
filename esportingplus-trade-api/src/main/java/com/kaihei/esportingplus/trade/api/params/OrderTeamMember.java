package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class OrderTeamMember {

    @NotBlank(message = "车队队员UID不能为空")
    @ApiModelProperty(value = "车队队员UID")
    private String teamMemberUID;

    @NotBlank(message = "车队队员名称不能为空")
    @ApiModelProperty(value = "车队队员名称")
    private String teamMemberName;

    @NotNull(message = "队员身份不能为空")
    @Range(min = 0,max = 9,message = "队员身份有误，要求：0-9")
    @ApiModelProperty(value = "队员身份，0：老板，1：暴鸡，2：暴娘，9：队长")
    private Integer userIdentity;

    @NotNull(message = "车队队员订单状态不能为空")
    @Range(min = 0,max = 12,message = "车队队员状态有误，要求：0-12")
    @ApiModelProperty(value = "车队队员订单状态:参考枚举TeamOrderPVPActionEnum")
    private Integer teamMemberStatus;

    @NotNull(message = "暴鸡等级")
    @ApiModelProperty(value = "暴鸡等级 100: 普通暴鸡 101: 优选暴鸡 102: 超级暴鸡 300: 暴娘")
    private Integer baojiLevel;

    public String getTeamMemberUID() {
        return teamMemberUID;
    }

    public void setTeamMemberUID(String teamMemberUID) {
        this.teamMemberUID = teamMemberUID;
    }

    public String getTeamMemberName() {
        return teamMemberName;
    }

    public void setTeamMemberName(String teamMemberName) {
        this.teamMemberName = teamMemberName;
    }

    public Integer getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(Integer userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Integer getTeamMemberStatus() {
        return teamMemberStatus;
    }

    public void setTeamMemberStatus(Integer teamMemberStatus) {
        this.teamMemberStatus = teamMemberStatus;
    }

    public Integer getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(Integer baojiLevel) {
        this.baojiLevel = baojiLevel;
    }
}
