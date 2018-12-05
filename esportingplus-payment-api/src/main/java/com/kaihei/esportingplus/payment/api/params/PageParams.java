package com.kaihei.esportingplus.payment.api.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: tangtao
 **/
public class PageParams implements Serializable {

    private static final long serialVersionUID = -1927627254719512489L;
    /**
     * 当前页码数
     */
    @NotNull(message = "当前页码数不能为空")
    @ApiModelProperty(value = "当前页码数", required = true, example = "1")
    @JsonProperty("page")
    private String page;

    /**
     * 单页条数
     */
    @NotNull(message = "单页条数不能为空")
    @ApiModelProperty(value = "单页条数", required = true, example = "10")
    @JsonProperty("size")
    private String size;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}
