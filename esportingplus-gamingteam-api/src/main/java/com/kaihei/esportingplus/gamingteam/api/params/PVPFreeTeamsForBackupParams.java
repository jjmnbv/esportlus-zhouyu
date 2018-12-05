package com.kaihei.esportingplus.gamingteam.api.params;

import com.kaihei.esportingplus.common.paging.PagingRequest;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 免费车队后管系统车队查询参数
 * @author zhangfang
 */
@Data
@ToString
@Builder
public class PVPFreeTeamsForBackupParams extends PagingRequest {

    private static final long serialVersionUID = -3156378047383387374L;
    /**
     * 车队序列号
     */
    private String sequence;
    /**
     * 队长uid
     */
    private String uid;
    /**
     * 队长鸡牌号
     */
    private String chickenId;
    /**
     * 查询起始时间
     */
    private Date startDate;
    /**
     * 查询终止时间
     */
    private Date endDate;
    /**
     * 0：准备中 1：已发车 2：已解散 3：已结束
     */
    private Integer status;

}
