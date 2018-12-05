package com.kaihei.esportingplus.payment.api.vo;

import java.util.List;

/**
 * 返回python管理后台的Vo
 */
public class ExchangeBackendRespVo {

    private long total;

    private List<ExchangeRecordsVo> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<ExchangeRecordsVo> getList() {
        return list;
    }

    public void setList(List<ExchangeRecordsVo> list) {
        this.list = list;
    }
}
