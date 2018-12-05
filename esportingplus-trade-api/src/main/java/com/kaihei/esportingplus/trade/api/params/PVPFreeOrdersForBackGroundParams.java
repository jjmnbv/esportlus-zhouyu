package com.kaihei.esportingplus.trade.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import com.kaihei.esportingplus.payment.api.params.PageParams;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfang
 */
@Data
@ToString
@Builder
public class PVPFreeOrdersForBackGroundParams extends PagingRequest {

    private static final long serialVersionUID = 4399492842229295596L;
    /**
     * 订单序列号
     */
    private String sequence;
    /**
     * 老板uid
     */
    private String uid;
    /**
     * 老板鸡牌号
     */
    private String chickenId;
    /**
     * 车队序列号
     */
    private String teamSequence;
    /**
     * 查询起始时间
     */
    private Date startDate;
    /**
     * 查询终止时间
     */
    private Date endDate;
    /**
     * 订单状态
     */
    private Integer status;

}
