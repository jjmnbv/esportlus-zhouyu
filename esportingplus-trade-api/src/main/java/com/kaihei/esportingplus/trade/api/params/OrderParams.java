package com.kaihei.esportingplus.trade.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@ApiModel("查询订单列表请求参数")
@Validated
public class OrderParams extends PagingRequest {
    @ApiModelProperty(value = "查询页(查询第几页)", required = false, position = 1,example = "1")
    @Min(value=1)
    private int offset;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
