package com.kaihei.esportingplus.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@ApiModel("批量查询暴鸡系数")
@Validated
public class BatchBaojiRateQueryParams {

    //暴鸡等级系数，支持批量查询，用英文,隔开
    @ApiModelProperty(value = "暴鸡等级,多个用英文逗号隔开", required = true, position = 1, example = "")
    @NotBlank(message = "暴鸡等级不能为空")
    private String baojiLevels;

    public String getBaojiLevels() {
        return baojiLevels;
    }

    public void setBaojiLevels(String baojiLevels) {
        this.baojiLevels = baojiLevels;
    }
}
