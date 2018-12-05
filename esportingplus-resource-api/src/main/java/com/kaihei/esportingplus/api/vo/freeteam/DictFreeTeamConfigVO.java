package com.kaihei.esportingplus.api.vo.freeteam;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * @author liangyi
 */
@Validated
public class DictFreeTeamConfigVO implements Serializable {

    private static final long serialVersionUID = -530537431676851677L;

    /**
     * 免费车队配置数据字典主键 id
     */
    @NotNull(message = "免费车队配置id不能为空")
    @ApiModelProperty(value = "免费车队配置主键id",
            required = true, position = 1, example = "1")
    private Integer dictId;

    /**
     * 数据字典value, 这里是一个 FreeTeamConfigVO 对象
     */
    @NotNull(message = "免费车队配置不能为空")
    private FreeTeamConfigVO value;

    public DictFreeTeamConfigVO() {
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public FreeTeamConfigVO getValue() {
        return value;
    }

    public void setValue(FreeTeamConfigVO value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
