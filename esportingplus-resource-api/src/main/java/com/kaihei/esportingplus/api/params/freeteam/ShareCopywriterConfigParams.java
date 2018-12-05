package com.kaihei.esportingplus.api.params.freeteam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
@ToString
@ApiModel("分享文案配置参数")
public class ShareCopywriterConfigParams {
    @ApiModelProperty(value = "分享任务id",required = false, position = 1, example = "1")
    private Integer shareId;
    @ApiModelProperty(value = "分享标题",required = true, position = 1, example = "xxx")
    @NotBlank(message = "分享标题不能为空")
    private String shareTitle;
    @ApiModelProperty(value = "分享文案",required = true, position = 1, example = "xxx")
    @NotBlank(message = "分享文案不能为空")
    private String shareContent;
    @ApiModelProperty(value = "状态 0:禁用,1:启用",required = true, position = 1, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;
    /**
     * 图片网络地址
     */
    @ApiModelProperty(value = "分享文案图片网络地址", required = false, position = 1, example = "")
    @NotBlank(message = "分享文案图片网络地址不能为空")
    private String imgUrl;

    /**
     * 点击时，跳转的目标地址
     */
    @ApiModelProperty(value = "分享文案目标地址", required = false, position = 1, example = "")
    @NotBlank(message = "分享文案目标地址不能为空")
    private String redirectUrl;

    /**
     * 投放位置，预留属性
     */
    @ApiModelProperty(value = "分享文案场景", required = false, position = 1, example = "")
    @NotBlank(message = "分享文案场景")
    private String scene;
}
