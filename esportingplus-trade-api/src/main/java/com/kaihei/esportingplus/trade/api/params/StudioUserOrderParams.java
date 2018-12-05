package com.kaihei.esportingplus.trade.api.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public class StudioUserOrderParams {

    /**
     * 暴鸡uid
     */
    private List<String> uids;
    /**
     * 查询开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date startDate;
    /**
     * 查询结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh", timezone = "GMT+8")
    private Date endDate;

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
