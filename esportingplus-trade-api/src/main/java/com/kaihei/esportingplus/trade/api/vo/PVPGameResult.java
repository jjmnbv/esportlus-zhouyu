package com.kaihei.esportingplus.trade.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PVPGameResult implements Serializable {

    private static final long serialVersionUID = -5025415442060033934L;
    /**
     * 游戏结果
     */
    private String gameResultZh;
    /**
     * 坐标， 就是第一小时或者第一局
     */
    private String positionZh;

}
