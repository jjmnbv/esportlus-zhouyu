package com.kaihei.esportingplus.trade.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ApiModel("统计工作室暴鸡查询")
// @Validated
public class StudioUserOrderStatisticsQueryParams implements Serializable {

    /**
     *
     */
    // @NotEmpty 有些工作室后台刚创建无暴鸡, 这里先去掉这个限制
    @ApiModelProperty(value = "uid集合", required = true, position = 1, example = "['3sdfa2','3543sd']")
    private List<String> uids;

    @ApiModelProperty(value = "开始日期startTime")
    private Date startDate;
    @ApiModelProperty(value = "结束日期endTime")
    private Date endDate;

    private Integer withCompaint;

    public Integer getWithCompaint() {
        return withCompaint;
    }

    public void setWithCompaint(Integer withCompaint) {
        this.withCompaint = withCompaint;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.startDate = simpleDateFormat.parse(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.endDate = simpleDateFormat.parse(endDate);
    }
}
