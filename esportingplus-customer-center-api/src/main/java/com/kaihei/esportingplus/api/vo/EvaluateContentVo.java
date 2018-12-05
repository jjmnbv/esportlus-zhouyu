package com.kaihei.esportingplus.api.vo;

import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价具体内容信息
 *
 * @author yangshidong
 * @date 2018/11/15
 */
@Builder
public class EvaluateContentVo implements Serializable {
    private static final long serialVersionUID = 8089275232084412621L;

    /**
     * 评价者信息
     */
    private EvaluateAppraiserVo appraiser;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价星级
     */
    private int star;

    /**
     * 评价时间
     */
    private Date createTime;

    /**
     * 评价对应订单Id
     */
    private int orderId;

    public EvaluateAppraiserVo getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(EvaluateAppraiserVo appraiser) {
        this.appraiser = appraiser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}

