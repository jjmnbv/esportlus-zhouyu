package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.common.tools.ObjectTools;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

/**
 * @author admin
 */
@ApiModel("分页查询投诉单列表请求参数")
@Validated
public class ComplaintQueryParam implements Serializable {

    private static final long serialVersionUID = 3265420297423973593L;
    /**
     * 页号
     */
    @ApiModelProperty(value = "页号", position = 1, example = "1")
    private int page;

    /**
     * 页面大小
     */
    @ApiModelProperty(value = "页面大小", position = 1, example = "10")
    private int pageSize;


    /**
     * 搜索类型
     */
    @NotNull(message = "搜索类型searchType不能为空")
    @Min(0)
    @ApiModelProperty(value = "搜索类型searchType", required = true, position = 1, example = "1")
    private Integer searchType;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字searchText", required = false, position = 2, example = "DNF")
    private String searchText;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期startDate", required = false, position = 3, example = "2018-01-01")
    private String startDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期endDate", required = false, position = 4, example = "2018-01-31")
    private String endDate;

    /**
     * 投诉单状态
     */
    @NotNull(message = "投诉单状态status不能为空")
    @Min(0)
    @ApiModelProperty(value = "投诉单状态status", required = true, position = 5, example = "0")
    private Integer status;

    /**
     * 投诉单类型
     */
    @ApiModelProperty(value = "投诉单类型type", required = false, position = 6, example = "0")
    private int type;

    /**
     * 暴鸡等级code
     */
    @ApiModelProperty(value = "暴鸡等级", required = false, position = 7, example = "102")
    private int baojiLevel;

    /**
     * 游戏code
     */
    @ApiModelProperty(value = "游戏code", required = false, position = 8, example = "88")
    private int gameCode;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型orderType", required = false, position = 9, example = "5")
    private int orderType;

    /**
     * 排序字段名, 加 -号表示降序
     */
    @ApiModelProperty(value = "排序字段名sortKey", required = false, position = 10, example = "type")
    private String sortKey;

    /**
     * 排序字段名
     */
    private String sortField;

    /**
     * 排序类型 0: 降序 1: 升序
     */
    private Integer sortType;

    /**
     * 当前时间
     */
    private Long now = System.currentTimeMillis();

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBaojiLevel() {
        return baojiLevel;
    }

    public void setBaojiLevel(int baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    public int getGameCode() {
        return gameCode;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        if (ObjectTools.isNotEmpty(this.sortKey)) {
            this.sortField = this.sortKey.substring(1, this.sortKey.length());
        } else {
            this.sortField = null;
        }
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        if (ObjectTools.isNotEmpty(this.sortKey)) {
            String st = this.sortKey.substring(0, 1);
            if ("-".equals(st)) {
                this.sortType = 0;
            } else {
                this.sortType = 1;
            }
        }
        this.sortType = null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
