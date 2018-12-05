package com.kaihei.esportingplus.marketing.api.vo;

import java.util.List;
import lombok.ToString;

/**
 * @author zl.zhao
 * @description:
 * @date: 2018/11/16 15:50
 */
@ToString
public class UserFreeCouponsQueryResultVo {
    /**
     * 操作返回值
     */
    private Boolean oprResult;

    /**
     * 次数免费券id集合
     */
    private List<Long> couponsIds;

    public Boolean getOprResult() {
        return oprResult;
    }

    public void setOprResult(Boolean oprResult) {
        this.oprResult = oprResult;
    }

    public List<Long> getCouponsIds() {
        return couponsIds;
    }

    public void setCouponsIds(List<Long> couponsIds) {
        this.couponsIds = couponsIds;
    }
}
