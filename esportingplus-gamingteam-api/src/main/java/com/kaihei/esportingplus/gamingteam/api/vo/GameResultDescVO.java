package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * 比赛结果具体描述
 * @author zhangfang
 */
@Data
@ToString
public class GameResultDescVO implements Serializable{

    private static final long serialVersionUID = -4506264617986423029L;
    /**
     *  第一局或者第一小时
     */
    private String resultNoDesc;

    /**
     * 胜利 失败 已打 未打
     */
    private String resultDesc;
    /**
     * 截图，如果有多个，则逗号分隔
     */
    private String screenshot;
}
