package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 免费车队，老板给与的积分
 * @author zhangfang
 */
@Data
@ToString
@Builder
public class PVPFreeBossPointsVO implements Serializable {

    private static final long serialVersionUID = 4954593634513244317L;
    /**
     * 车队队员昵称
     */
    private String username;

    /**
     * 车队队员头像
     */
    private String avatar;
    /**
     * 给与的积分量
     */
    private Integer amount=0;
}
