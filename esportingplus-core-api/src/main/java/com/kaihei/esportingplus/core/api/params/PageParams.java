package com.kaihei.esportingplus.core.api.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页参数
 *
 * @author yangshidong
 * @date 2018/12/1
 **/
@Validated
public class PageParams implements Serializable {

    private static final long serialVersionUID = 4290477227105876133L;
    /**
     * 页码
     */
    @NotEmpty(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private int page;

    /**
     * 单页条数
     */
    @NotEmpty(message = "单页条数不能为空")
    @ApiModelProperty(value = "单页条数", required = true, example = "15")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
