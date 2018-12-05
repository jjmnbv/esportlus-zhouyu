package com.kaihei.esportingplus.payment.api.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @program: esportingplus
 * @description: 前台用户暴鸡币充值记录列表
 * @author: xusisi
 * @create: 2018-10-18 16:54
 **/
public class FrontGCcoinRechargeVo {

    /***
     * 查询条件：用户ID
     */
    private String userId;

    /***
     * 查询条件：来源
     */
    private String sourceId;

    /***
     * 查询条件：支付方式
     */
    private String channel;
    /***
     * 查询条件：开始时间
     */
    private String beginDate;

    /***
     * 查询条件：结束时间
     */
    private String endDate;

    /***
     * 查询条件：每页展示数量
     */
    private int size;

    /***
     * 查询条件：当前页面
     */
    private int page;

    /***
     * 充值用户数
     */
    private int totalUser;

    /***
     * 充值暴鸡币数量
     */
    private int totalGcoinAmount;

    /***
     * 支付金额
     */
    private int totalAmount;

    /***
     * 总记录数
     */
    private int totalRecords;

    /***
     * 暴鸡币充值记录列表
     */
    private List<GCoinRechargeVo> gCoinRechargeVoList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(int totalUser) {
        this.totalUser = totalUser;
    }

    public int getTotalGcoinAmount() {
        return totalGcoinAmount;
    }

    public void setTotalGcoinAmount(int totalGcoinAmount) {
        this.totalGcoinAmount = totalGcoinAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<GCoinRechargeVo> getgCoinRechargeVoList() {
        return gCoinRechargeVoList;
    }

    public void setgCoinRechargeVoList(List<GCoinRechargeVo> gCoinRechargeVoList) {
        this.gCoinRechargeVoList = gCoinRechargeVoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
