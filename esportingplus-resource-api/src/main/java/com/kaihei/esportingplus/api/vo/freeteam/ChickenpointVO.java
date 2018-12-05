package com.kaihei.esportingplus.api.vo.freeteam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChickenpointVO {

    /**
     * 免费车队类型Id
     */
    private Integer freeTeamTypeId;
    /**
     * 免费车队类型名
     */
    private String name;
}
