package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.api.vo.DictBaseVO;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队类型详情 VO
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class TeamTypeDetailVO implements Serializable {

    private static final long serialVersionUID = 3332503488093380569L;

    /**
     * 车队类型主键id
     */
    private Integer teamTypeId;

    /**
     * 车队类型名称
     */
    private String name;

    /**
     * 车队类型显示名称
     */
    private String displayName;

    /**
     * 类别, 0:免费, 1:付费
     */
    private Integer category;

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
     * 玩法模式id,来自数据字典(上分/陪玩)
     */
    private DictBaseVO playMode;

    private List<TeamSettlementModeVO> settlementModeList;

    /**
     * 可组建的身份,来自数据字典(暴鸡/暴娘)
     */
    private DictBaseVO baojiIdentity;

    /**
     * 最大座位数
     */
    private Integer maxPositionCount;

    /**
     * 状态 1: 上架, 0: 下架
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer orderIndex;

    public TeamTypeDetailVO() {
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        if (ObjectTools.isEmpty(name)) {
            displayName = game.getName();
        } else {
            displayName = game.getName()+"-"+name;
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
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

    public DictBaseVO getPlayMode() {
        return playMode;
    }

    public void setPlayMode(DictBaseVO playMode) {
        this.playMode = playMode;
    }

    public List<TeamSettlementModeVO> getSettlementModeList() {
        return settlementModeList;
    }

    public void setSettlementModeList(
            List<TeamSettlementModeVO> settlementModeList) {
        this.settlementModeList = settlementModeList;
    }

    public DictBaseVO getBaojiIdentity() {
        return baojiIdentity;
    }

    public void setBaojiIdentity(DictBaseVO baojiIdentity) {
        this.baojiIdentity = baojiIdentity;
    }

    public Integer getMaxPositionCount() {
        return maxPositionCount;
    }

    public void setMaxPositionCount(Integer maxPositionCount) {
        this.maxPositionCount = maxPositionCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }


    public static void main(String[] args) {
        List<TeamTypeDetailVO> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            TeamTypeDetailVOBuilder builder = TeamTypeDetailVO.builder();
            builder.teamTypeId(i).name("free_team_type"+i);
            builder.game(new GameDictVO());
            List<DictBaseVO> zones = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                zones.add(new DictBaseVO());
            }
            builder.gameZoneList(zones);
            List<DanDictVo> dans = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                dans.add(new DanDictVo());
            }
            builder.gameDanList(dans);
            builder.playMode(new DictBaseVO());
            builder.settlementModeList(Arrays.asList(new TeamSettlementModeVO()));
            builder.baojiIdentity(new DictBaseVO());
            builder.maxPositionCount(i+10);
            TeamTypeDetailVO teamTypeDetailVO = builder.build();
            list.add(teamTypeDetailVO);
        }
        System.out.println(JacksonUtils.toJsonWithSnake(ResponsePacket.onSuccess(list.get(0))));
    }

}