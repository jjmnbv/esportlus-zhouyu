package com.kaihei.esportingplus.gamingteam.api.params;

import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Data
@ToString
@Validated
public class ImTeamStartParams extends ImEndTeamMsgParams {

    private static final long serialVersionUID = -4244604114692189994L;
}
