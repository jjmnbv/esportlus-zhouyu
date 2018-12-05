package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * @author zhangfang
 */
@Validated
@ApiModel("banner参数")
@Data

public class ResourceStateConfigVo implements Serializable {

    private static final long serialVersionUID = 3224416878825786420L;
    private Integer resourceId;

    /**
     * banner名称
     */
    private String name;
    /**
     * 用户类型 0 老板 1暴鸡
     */
    public Integer userType;
    /**
     * 用户类型描述
     */
    public String userTypeDesc;

    /**
     * 投放位置，预留属性
     */
    private String position;

    /**
     * 位置中文名称
     */
    private String positionZh;

    /**
     * banner图片网络地址
     */
    private String imgUrl;

    /**
     * 点击时，跳转的目标地址
     */
    private String redirectUrl;

    /**
     * 权重、排序号
     */
    @ApiModelProperty(value = "权重", required = false, position = 1, example = "")
    private Integer orderIndex;



}
