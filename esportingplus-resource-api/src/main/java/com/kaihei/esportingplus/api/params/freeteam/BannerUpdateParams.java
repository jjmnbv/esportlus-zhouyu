package com.kaihei.esportingplus.api.params.freeteam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

/**
 * 保存banner参数
 *
 * @author zhangfang
 */
@Validated
@Data
@ToString
public class BannerUpdateParams extends BannerSaveParams {

    private static final long serialVersionUID = 2464828137725170794L;
    @ApiModelProperty(value = "banner配置id", required = false, position = 1, example = "")
    private Integer bannerId;

}
