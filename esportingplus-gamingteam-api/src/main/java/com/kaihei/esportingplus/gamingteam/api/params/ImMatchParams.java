package com.kaihei.esportingplus.gamingteam.api.params;

import com.kaihei.esportingplus.gamingteam.api.enums.MatchResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhangfang
 */
@Validated
@ApiModel("匹配失败消息")
@Builder
@Data
public class ImMatchParams implements Serializable {

    private static final long serialVersionUID = -4603352575836688068L;
    /**
     * 用户uid集合，兼容以后组队匹配，所以用集合
     */
    @NotEmpty(message = "uid不能为空")
    @ApiModelProperty(value = "uid集合", required = true, position = 1)
    private List<String> uids;
    @ApiModelProperty(value = "匹配结果 0成功，1失败", required = true, position = 1)
    private Integer result;
    /**
     * 提示语内容
     */
    @NotBlank(message = "提示语不能为空")
    @ApiModelProperty(value = "提示语", required = true, position = 1)
    private String msgContent;
    @ApiModelProperty(value = "车队序列号", required = false, position = 1)
    private String teamSequence;

}
