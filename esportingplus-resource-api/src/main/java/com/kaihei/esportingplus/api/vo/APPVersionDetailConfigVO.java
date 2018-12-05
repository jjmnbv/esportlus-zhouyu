package com.kaihei.esportingplus.api.vo;

import com.kaihei.esportingplus.common.enums.PlatformEnum;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 客户端版本详细信息 VO, 后台管理系统配置用
 * @author liangyi
 */
@AllArgsConstructor
@NoArgsConstructor
@Validated
@ApiModel(description = "客户端版本信息后台配置")
public class APPVersionDetailConfigVO extends APPVersionConfigVO {

    private static final long serialVersionUID = -6183004486888663729L;

    /**
     * 数据字典 id
     */
    private Integer dictId;

    /**
     * 客户端平台标识 {@link PlatformEnum}
     */
    @NotEmpty(message = "客户端平台标识不能为空")
    @ApiModelProperty(value = "客户端平台标识",
            required = true, position = 1, example = "iOS")
    private String platform;

    /**
     * 包名
     */
    @NotEmpty(message = "包名不能为空")
    @ApiModelProperty(value = "包名",
            required = true, position = 2, example = "暴鸡电竞")
    private String packageName;

    /**
     * 最低版本号, 小于则强更(预留字段)
     */
    @ApiModelProperty(value = "最低版本号", position = 3, example = "1.0.0")
    private String lowestVersion;

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLowestVersion() {
        return lowestVersion;
    }

    public void setLowestVersion(String lowestVersion) {
        this.lowestVersion = lowestVersion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static void main(String[] args) {
        APPVersionDetailConfigVO detailConfigVO = new APPVersionDetailConfigVO();
        detailConfigVO.setDictId(191);
        detailConfigVO.setLowestVersion("1.0.0");
        detailConfigVO.setPackageName("暴暴APP");
        detailConfigVO.setPlatform(PlatformEnum.MINI_PROGRAM.getCode());
        detailConfigVO.setApiHost("https://dev.kaiheikeji.com/");
        detailConfigVO.setUpdateTime(new Date());
        detailConfigVO.setUpdateDesc("又一个小程序的更新....本次更新");
        detailConfigVO.setVersion("1.1.2");
        detailConfigVO.setDownloadUrl("https://dev.kaiheikeji.com/");
        detailConfigVO.setForceUpdate(false);
        detailConfigVO.setRemindUpdate(true);
        System.out.println(JacksonUtils.toJsonWithSnake(detailConfigVO));
    }

}