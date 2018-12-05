package com.kaihei.esportingplus.gamingteam.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamSequenceVO {

    /**
     * 车队唯一标识
     */
    private String sequence;

    /**
     * RC 群组Id
     */
    private String group;
}
