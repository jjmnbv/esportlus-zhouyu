package com.kaihei.esportingplus.api.params.freeteam;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 车队类型后台查询参数
 * @author liangyi
 */
@Validated
@ApiModel(description = "车队类型后台查询参数")
public class TeamTypeBackendQueryParams extends PagingRequest implements Serializable {

    private static final long serialVersionUID = -7014802410655764347L;

    /**
     * 车队类型状态 {@link com.kaihei.esportingplus.common.enums.StatusEnum}
     */
    @ApiModelProperty(value = "车队类型状态",
            required = false, position = 1, example = "1")
    private Integer status;

    /**
     * 车队类型分类 {@link com.kaihei.esportingplus.common.enums.TeamCategoryEnum}
     */
    @ApiModelProperty(value = "车队类型分类",
            required = false, position = 2, example = "0")
    private Integer category;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}