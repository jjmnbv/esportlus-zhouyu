package com.kaihei.esportingplus.gamingteam.api.params;

import com.kaihei.esportingplus.gamingteam.api.enums.ImTeamMemberStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * 车队服务结束通知
 * @author zhangfang
 */
@Validated
@ApiModel("车队成员一般状态变化通知")
@Data
@ToString
public class ImTeamStatusChangeParams extends ImGroupBaseParams {

    private static final long serialVersionUID = 4728974551584808485L;

    /**
     * 提示语内容
     */
    @NotNull(message = "状态枚举不能为空")
    private ImTeamMemberStatusEnum statusEnum;

    /**
     * 推送的成员uid
     */
    @NotEmpty(message = "队员uid不能为空")
    @ApiModelProperty(value = "成员uid集合", required = true, position = 1)
    private List<String> members;

    @NotBlank(message = "车队序列号不能为空")
    @ApiModelProperty(value = "车队序列号", required = true, position = 1)
    private String teamSequence;
}
