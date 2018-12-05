package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 免费车队类型简单 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeTeamTypeSimpleVO extends FreeTeamTypeDictVO {

    private static final long serialVersionUID = 3095906741725358349L;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 免费车队类型图片
     */
    private String imgUrl;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 暴鸡身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    @JsonIgnore
    private Integer baojiIdentity;

    /**
     * 所属游戏
     */
    @JsonIgnore
    private GameDictVO game;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getBaojiIdentity() {
        return baojiIdentity;
    }

    public void setBaojiIdentity(Integer baojiIdentity) {
        this.baojiIdentity = baojiIdentity;
    }

    public GameDictVO getGame() {
        return game;
    }

    public void setGame(GameDictVO game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}