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
 * 保存资源位参数
 * @author zhangfang
 */
@Validated
@Data
@ToString
public class ResourceStateSaveParams implements Serializable {

    private static final long serialVersionUID = -5930980231018591645L;
    /**
     * banner名称
     */
    @ApiModelProperty(value = "资源位配置名称", required = true, position = 1, example = "")
    @NotBlank(message = "资源位配置名称不能为空")
    private String name;
    /**
     * 用户类型 0 老板 1暴鸡
     */
    @ApiModelProperty(value = "用户类型0:全量用户, 1 老板 2暴鸡暴娘", required = true, position = 1, example = "0")
    @NotNull(message = "用户类型不能为空")
    public Integer userType;
    /**
     * 投放位置，预留属性
     */
    @ApiModelProperty(value = "资源位位置 ", required = false, position = 1, example = "")
    @NotBlank(message = "资源位位置不能为空")
    private String position;

    /**
     * banner图片网络地址
     */
    @ApiModelProperty(value = "资源位图片网络地址", required = false, position = 1, example = "")
    @NotBlank(message = "资源位图片网络地址不能为空")
    private String imgUrl;

    /**
     * 点击时，跳转的目标地址
     */
    @ApiModelProperty(value = "资源位目标地址", required = false, position = 1, example = "")
    @NotBlank(message = "资源位目标地址不能为空")
    private String redirectUrl;

    /**
     * 权重、排序号
     */
    @ApiModelProperty(value = "权重", required = false, position = 1, example = "")
    @NotNull(message = "权重不能为空")
    private Integer orderIndex;

}
