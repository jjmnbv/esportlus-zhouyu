package com.kaihei.esportingplus.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询反馈与投诉列表参数
 *
 * @author yangshidong
 * @date 2018/12/3
 */
@Validated
@ApiModel("查询反馈与投诉列表参数")
public class FeedbackListParams implements Serializable {

    private static final long serialVersionUID = -531946127183052155L;

    @NotBlank(message = "查询开始日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询开始日期", required = true, position = 1, example = "2018-12-3")
    private String startDate;

    @NotBlank(message = "查询结束日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询结束日期", required = true, position = 2, example = "https://www.baidu.com")
    private String endDate;

    @NotNull(message = "处理状态不能为空")
    @ApiModelProperty(value = "处理状态", required = true, position = 3, example = "0")
    private short handleStatus;

    @NotNull(message = "页码不能为空")
    @ApiModelProperty(value = "页码", required = true, position = 4, example = "1")
    private int page;

    @NotNull(message = "单页条数不能为空")
    @ApiModelProperty(value = "单页条数", required = true, position = 5, example = "10")
    private int size;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public short getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(short handleStatus) {
        this.handleStatus = handleStatus;
    }

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
