package com.kaihei.esportingplus.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@ApiModel("个人中心小鸡配置")
public class ChickenConfigVo implements Serializable {

    private static final long serialVersionUID = 5979664847649990999L;
    @ApiModelProperty(value = "小鸡图片网络地址", required = false, position = 1, example = "")
    @NotBlank(message = "小鸡图片网络地址不能为空")
    private String imgUrl;
    /**
     * 点击时，跳转的目标地址
     */
    @ApiModelProperty(value = "点击跳转地址", required = false, position = 1, example = "")
    @NotBlank(message = "点击跳转地址不能为空")
    private String redirectUrl;

    @ApiModelProperty(value = "小鸡配置类型 1:ios个人中心小鸡, 2：ios免费车队小鸡 3: Android个人中心小鸡 4：Android免费车队小鸡", required = false, position = 1, example = "1")
    @NotNull(message = "小鸡配置类型")
    private Integer type;

    @ApiModelProperty(value = "状态0 禁用 1启用", required = false, position = 1, example = "")
    @NotNull(message = "是否显示不能为空")
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
