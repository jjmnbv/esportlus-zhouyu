package com.kaihei.esportingplus.api.vo.freeteam;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FreeTeamHomeVo implements Serializable {

    private static final long serialVersionUID = -3058458092908151859L;
    private List<FreeTeamAdvertiseHomeVo> advertises;

    private List<FreeTeamScrollTemplateHomeVo> scrollWords;

    public List<FreeTeamAdvertiseHomeVo> getAdvertises() {
        return advertises;
    }

    public void setAdvertises(
            List<FreeTeamAdvertiseHomeVo> advertises) {
        this.advertises = advertises;
    }

    public List<FreeTeamScrollTemplateHomeVo> getScrollWords() {
        return scrollWords;
    }

    public void setScrollWords(
            List<FreeTeamScrollTemplateHomeVo> scrollWords) {
        this.scrollWords = scrollWords;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
