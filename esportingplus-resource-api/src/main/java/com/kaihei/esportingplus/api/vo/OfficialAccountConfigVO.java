package com.kaihei.esportingplus.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 官方账号配置 VO
 * @author liangyi
 */
@Validated
@ApiModel(description = "官方账号配置")
@Builder
@AllArgsConstructor
public class OfficialAccountConfigVO implements Serializable {

    private static final long serialVersionUID = -6193632151134018116L;

    /**
     * 官方账号配置 id
     */
    @ApiModelProperty(value = "官方账号配置id",
            required = false, position = 1, example = "1")
    private Integer officialAccountConfigId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称",
            required = true, position = 2, example = "客服IM账号")
    private String categoryName;

    /**
     * 账号id
     */
    @ApiModelProperty(value = "账号id",
            required = true, position = 3, example = "12345678900")
    private String accountId;

    /**
     * 账号用户名
     */
    @ApiModelProperty(value = "账号用户名",
            required = true, position = 4, example = "需求频繁变更")
    private String accountName;

    /**
     * 账号头像
     */
    @ApiModelProperty(value = "账号头像",
            required = true, position = 5,
            example = "http://img.nga.178.com/attachments/mon_201812/02/-d1rcQ5-idsaZ1fT3cS1k0-160.jpg")
    @NotEmpty(message = "免费车队类型图片不能为空")
    private String accountAvatar;

    /**
     * 排序权重, 值越大越靠前
     */
    @ApiModelProperty(value = "排序权重,值越小越靠前",
            required = false, position = 11, example = "1")
    private Integer orderIndex;

    /**
     * 状态 1: 上架, 0: 下架
     */
    @ApiModelProperty(value = "状态",
            required = false, position = 12, example = "1")
    private Integer status;

    public OfficialAccountConfigVO() {
    }

    public Integer getOfficialAccountConfigId() {
        return officialAccountConfigId;
    }

    public void setOfficialAccountConfigId(Integer officialAccountConfigId) {
        this.officialAccountConfigId = officialAccountConfigId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountAvatar() {
        return accountAvatar;
    }

    public void setAccountAvatar(String accountAvatar) {
        this.accountAvatar = accountAvatar;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}