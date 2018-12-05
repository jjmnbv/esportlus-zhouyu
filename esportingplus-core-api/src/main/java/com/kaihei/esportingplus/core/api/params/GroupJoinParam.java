package com.kaihei.esportingplus.core.api.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author liuyang
 * @Description 创建群组参数
 * @Date 2018/10/26 16:40
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class GroupJoinParam {

    /** 要加入群的用户id集合*/
    @NotEmpty(message = "要加入群的用户id集合不能为空")
    private List<String> members;

    /** 创建群组 Id*/
    @NotBlank(message = "群组 Id不能为空")
    private String groupId;

    /** 群组 Id 对应的名称*/
    @NotBlank(message = "群组名称不能为空")
    private String groupName;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
