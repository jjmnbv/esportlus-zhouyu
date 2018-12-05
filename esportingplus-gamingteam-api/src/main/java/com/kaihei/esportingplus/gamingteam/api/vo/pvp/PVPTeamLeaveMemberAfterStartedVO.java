package com.kaihei.esportingplus.gamingteam.api.vo.pvp;

import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamMemberVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PVPTeamLeaveMemberAfterStartedVO implements JsonSerializable {

    /**
     * 车队唯一标识
     */
    private String teamSequence;

    /**
     * 用户基础信息
     */
    private TeamMemberVO teamMemberVO;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 房间号
     */
    private Integer roomNum;

    /**
     * 游戏Id
     */
    private Integer gameId;
}
