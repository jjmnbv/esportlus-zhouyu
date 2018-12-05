package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.api.enums.EvaluateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 提交评价参数
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@ApiModel("评价订单查询请求参数")
public final class EvaluateQueryParam implements Serializable {

    private static final long serialVersionUID = 3468681693047019094L;

    @NotEmpty(message = "评价类型不能为空")
    @ApiModelProperty(value = "评价类型", required = true, position = 1, example = "1")
    private EvaluateTypeEnum evaluateType;

    @ApiModelProperty(value = "订单/技能id", required = false, position = 2, example = "1324564")
    private int id;

    @ApiModelProperty(value = "页数", required = false, position = 3, example = "1")
    private int page;

    @ApiModelProperty(value = "每页数据条数", required = false, position = 4, example = "10")
    private int pageCount;

    @ApiModelProperty(value = "是否暴鸡", required = false, position = 5, example = "0")
    private int isBaoji;

    /**
     * 用户id
     * */
    private int authorId;

    /**
     * 订单id
     * */
    private int orderId;

    /**
     * 关联资源id
     * */
    private String relateSourceId;

    /**
     * 暴鸡uid
     * */
    private String baojiUid;

    /**
     * 订单类型
     * */
    private short orderType;

    /**
     * 是否针对订单查询
     * */
    private short isAimedOrder;

    public EvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(EvaluateTypeEnum evaluateType) {
        this.evaluateType = evaluateType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getIsBaoji() {
        return isBaoji;
    }

    public void setIsBaoji(int isBaoji) {
        this.isBaoji = isBaoji;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getRelateSourceId() {
        return relateSourceId;
    }

    public void setRelateSourceId(String relateSourceId) {
        this.relateSourceId = relateSourceId;
    }

    public String getBaojiUid() {
        return baojiUid;
    }

    public void setBaojiUid(String baojiUid) {
        this.baojiUid = baojiUid;
    }

    public short getOrderType() {
        return orderType;
    }

    public void setOrderType(short orderType) {
        this.orderType = orderType;
    }

    public short getIsAimedOrder() {
        return isAimedOrder;
    }

    public void setIsAimedOrder(short isAimedOrder) {
        this.isAimedOrder = isAimedOrder;
    }
}
