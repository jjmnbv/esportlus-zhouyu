package com.kaihei.esportingplus.api.params.freeteam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * 保存资源位参数
 *
 * @author zhangfang
 */
@Validated
@Data
@ToString
public class ResourceStateUpdateParams extends ResourceStateSaveParams {

    private static final long serialVersionUID = 4912303985974669896L;
    @ApiModelProperty(value = "banner配置id", required = false, position = 1, example = "")
    private Integer resourceId;

}
