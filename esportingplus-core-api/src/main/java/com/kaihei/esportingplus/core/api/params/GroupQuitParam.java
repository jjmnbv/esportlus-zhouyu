package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author liuyang
 * @Description 退出群组参数
 * @Date 2018/10/26 18:03
 **/
@Validated
public class GroupQuitParam {

    @NotEmpty(message = "要加入群的用户id集合不能为空")
    private List<String> memebers;

    @NotBlank(message = "群组 Id不能为空")
    private String groupId;

    public List<String> getMemebers() {
        return memebers;
    }

    public void setMemebers(List<String> memebers) {
        this.memebers = memebers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
