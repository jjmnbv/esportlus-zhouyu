package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.api.vo.SettlementModeVO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队类型详情 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeTeamTypeDetailVO extends FreeTeamTypeDictVO {

    private static final long serialVersionUID = 355295142522110292L;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 免费车队类型图片
     */
    private String imgUrl;

    /**
     * 所属游戏id,来自数据字典
     */
    private GameDictVO game;

    /**
     * 所属游戏的游戏区, 来自数据字典
     */
    private List<DictBaseVO> gameZoneList;

    /**
     * 所属游戏的段位, 来自数据字典
     */
    private List<DanDictVo> gameDanList;

    /**
     * 玩法模式 {@link com.kaihei.esportingplus.common.enums.PlayModeEnum}
     */
    private Integer playMode;

    /**
     * 结算类型
     */
    private List<SettlementModeVO> settlementModeList;

    /**
     * 可组建的身份 {@link com.kaihei.esportingplus.common.enums.UserIdentityEnum}
     */
    private Integer baojiIdentity;

    /**
     * 最大座位数
     */
    private Integer maxPositionCount;

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

    public GameDictVO getGame() {
        return game;
    }

    public void setGame(GameDictVO game) {
        this.game = game;
    }

    public List<DictBaseVO> getGameZoneList() {
        return gameZoneList;
    }

    public void setGameZoneList(List<DictBaseVO> gameZoneList) {
        this.gameZoneList = gameZoneList;
    }

    public List<DanDictVo> getGameDanList() {
        return gameDanList;
    }

    public void setGameDanList(
            List<DanDictVo> gameDanList) {
        this.gameDanList = gameDanList;
    }

    public Integer getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public List<SettlementModeVO> getSettlementModeList() {
        return settlementModeList;
    }

    public void setSettlementModeList(
            List<SettlementModeVO> settlementModeList) {
        this.settlementModeList = settlementModeList;
    }

    public Integer getBaojiIdentity() {
        return baojiIdentity;
    }

    public void setBaojiIdentity(Integer baojiIdentity) {
        this.baojiIdentity = baojiIdentity;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}