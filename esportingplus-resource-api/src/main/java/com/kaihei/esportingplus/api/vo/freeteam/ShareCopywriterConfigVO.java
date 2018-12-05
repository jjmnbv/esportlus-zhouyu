package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ShareCopywriterConfigVO implements Serializable {

    /**
     * 分享id，即数据字典id
     */
    private Integer shareId;
    /**
     * 分享场景
     */
    private String scene;
    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享文案
     */
    private String shareContent;

    /**
     * 图片网络地址
     */
    private String imgUrl;

    /**
     * 点击时，跳转的目标地址
     */
    private String redirectUrl;
    /**
     * 分享状态 0，禁用，1启用
     */
    private Integer status;
    /**
     * 分享次数
     */
    private Long shareCount;
    /**
     * 点击次数
     */
    private Long chickCount;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

}
