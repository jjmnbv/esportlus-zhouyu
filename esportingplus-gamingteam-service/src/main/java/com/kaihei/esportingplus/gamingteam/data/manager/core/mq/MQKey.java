package com.kaihei.esportingplus.gamingteam.data.manager.core.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class MQKey {

    private String topic;

    private String tag;
}
