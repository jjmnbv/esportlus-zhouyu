package com.kaihei.esportingplus.gamingteam.api.params;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.validation.annotation.Validated;

/**
 * 根据车队序列号批量查询车队信息
 * @author liangyi
 */
@Validated
public class TeamInfoBatchParams implements Serializable{

    private static final long serialVersionUID = 4079128537671975717L;
    /**
     * 车队序列号集合
     */
    @NotEmpty(message = "车队序列号列表不能为空")
    private List<String> teamSequenceList;

    public TeamInfoBatchParams() {
    }

    public List<String> getTeamSequenceList() {
        return teamSequenceList;
    }

    public void setTeamSequenceList(List<String> teamSequenceList) {
        this.teamSequenceList = teamSequenceList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
