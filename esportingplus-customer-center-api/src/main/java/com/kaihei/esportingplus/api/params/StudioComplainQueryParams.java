package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

/**
 * 工作室投诉订单查询参数
 * @author zhangfang
 */
@ApiModel("分页查询投诉单列表请求参数（工作室）")
@Validated
public class StudioComplainQueryParams extends PagingRequest implements Serializable {

    private static final long serialVersionUID = 8895815431527188598L;

    @NotEmpty(message = "uid集合不能为空")
    @ApiModelProperty(value = "uid集合", required = true, position = 1, example = "['3sdfa2','3543sd']")
    private List<String> uids;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期startDate", required = false, position = 2, example = "2018-01-31")
    private String startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期endDate", required = false, position = 3, example = "2018-01-31")
    private String endDate;

    @ApiModelProperty(value = "订单状态status", required = false, position = 4, example = "0")
    private Byte status;

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StudioComplainQueryParams{" +
                "uids=" + uids +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
