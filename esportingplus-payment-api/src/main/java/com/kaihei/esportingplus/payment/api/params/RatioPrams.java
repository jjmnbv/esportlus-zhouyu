package com.kaihei.esportingplus.payment.api.params;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分成比例设置入参
 *
 * @author zhouyu, haycco
 */
@Validated
public class RatioPrams implements Serializable {

    private static final long serialVersionUID = -8899810095502289951L;
    /***
     * 分成比例
     */
    @NotNull(message = "分成比例不能为空")
    private Float ratio;

    /***
     * 状态设置 ENABLE 启用；DISABLE 冻结
     */
    @NotNull(message = "分成比例的使用状态不能为空")
    private String state;

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
