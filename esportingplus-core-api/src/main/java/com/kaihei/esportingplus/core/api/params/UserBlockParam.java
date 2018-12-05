package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author liuyang
 * @Description 用户封禁参数
 * @Date 2018/10/26 16:07
 **/
@Validated
public class UserBlockParam {

    /** 用户Id*/
    @NotEmpty(message = "用户id不能为空")
    private List<String> userIds;

    /** 封禁时长，单位为分钟，最大值为43200分钟。（必传）*/
    @NotNull(message = "封禁时长不能为空")
    @Max(value = 43200, message = "封禁时长，单位为分钟，最大值为43200分钟")
    private Integer minute;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
}
