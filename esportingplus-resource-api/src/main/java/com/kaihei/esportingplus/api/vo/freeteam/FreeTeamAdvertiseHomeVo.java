package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("免费车队宣传图")
public class FreeTeamAdvertiseHomeVo {

    /**
     * 宣传图id
     */
    @ApiModelProperty(value = "宣传图id", required = false, position = 1, example = "")
    private Integer advertiseId;
    /**
     * 描述
     */
    @ApiModelProperty(value = "配置描述", required = false, position = 1, example = "")
    private String remark;
    /**
     * 机器类型
     */
    @ApiModelProperty(value = "机器类型 0:默认配置,1：ios正常3x版，2:ios的x/xs版,3:ios的xsm/xr版,4:android的1080p版,5:android的18:9全面屏版", required = false, position = 1, example = "")
    @NotNull(message = "机器类型不能为空")
    private Integer machineType;
    /**
     * 宣传图的url地址
     */
    @ApiModelProperty(value = "宣传图的url地址", required = false, position = 1, example = "")
    private String imgUrl;
    /**
     * 点击图片后的跳转地址
     */
    @ApiModelProperty(value = "点击图片后的跳转地址", required = false, position = 1, example = "")
    private String redirectUrl;

    public Integer getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(Integer advertiseId) {
        this.advertiseId = advertiseId;
    }

    public Integer getMachineType() {
        return machineType;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
