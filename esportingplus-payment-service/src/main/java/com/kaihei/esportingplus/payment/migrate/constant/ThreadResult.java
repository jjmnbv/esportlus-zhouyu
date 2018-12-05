package com.kaihei.esportingplus.payment.migrate.constant;

import java.util.List;

/**
 * @author: tangtao
 **/
public class ThreadResult {


    private long totalTime;
    private long initTime;
    private long handleTime;
    private long saveTime;
    private int handleOrderSize;//总共处理的单据数据
    private int orderSize;//生成支付单
    private int billSize;//生成流水数量
    private int warningSize;
    private int errSize;
    private String errMsg;
    private List<String> warningData;
    private List<String> errData;


    public ThreadResult() {
    }

    public ThreadResult(String errMsg) {
        this.errMsg = errMsg;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    public long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(long handleTime) {
        this.handleTime = handleTime;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getHandleOrderSize() {
        return handleOrderSize;
    }

    public void setHandleOrderSize(int handleOrderSize) {
        this.handleOrderSize = handleOrderSize;
    }

    public int getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(int orderSize) {
        this.orderSize = orderSize;
    }

    public int getBillSize() {
        return billSize;
    }

    public void setBillSize(int billSize) {
        this.billSize = billSize;
    }

    public int getWarningSize() {
        return warningSize;
    }

    public void setWarningSize(int warningSize) {
        this.warningSize = warningSize;
    }

    public List<String> getWarningData() {
        return warningData;
    }

    public void setWarningData(List<String> warningData) {
        this.warningData = warningData;
    }

    public int getErrSize() {
        return errSize;
    }

    public void setErrSize(int errSize) {
        this.errSize = errSize;
    }

    public List<String> getErrData() {
        return errData;
    }

    public void setErrData(List<String> errData) {
        this.errData = errData;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ThreadResult{" +
                "totalTime=" + totalTime +
                ", initTime=" + initTime +
                ", handleTime=" + handleTime +
                ", saveTime=" + saveTime +
                ", handleOrderSize=" + handleOrderSize +
                ", orderSize=" + orderSize +
                ", billSize=" + billSize +
                ", warningSize=" + warningSize +
                ", errSize=" + errSize +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
