package com.kaihei.esportingplus.api.vo.freeteam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaihei.esportingplus.api.vo.GameDictVO;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.JacksonUtils;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车队类型, 供列表显示用
 * @author liangyi
 */
@Builder
@AllArgsConstructor
public class TeamTypeSimpleVO implements Serializable {

    private static final long serialVersionUID = -1239447046336724796L;

    /**
     * 车队类型主键 id
     */
    private Integer teamTypeId;

    /**
     * 免费车队名称
     */
    private String name;

    /**
     * 类别, 0:免费, 1:付费
     */
    private Integer category;

    /**
     * 所属的游戏名称
     */
    private String gameName;

    /**
     * 所属的游戏图片
     */
    private String gameUrl;

    /**
     * 所属的游戏 id
     */
    @JsonIgnore
    private Integer gameId;

    /**
     * 所属的游戏
     */
    @JsonIgnore
    private GameDictVO game;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 暴鸡身份 id, 来自数据字典
     */
    @JsonIgnore
    private Integer baojiIdentityId;

    /**
     * 排序权重, 值越大越靠前
     */
    private Integer orderIndex;

    /**
     * 状态, 0:失效,1:有效
     */
    private Integer status;

    public TeamTypeSimpleVO() {
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

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getGameName() {
        return game.getName();
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameUrl() {
        return game.getUrl();
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public GameDictVO getGame() {
        return game;
    }

    public void setGame(GameDictVO game) {
        this.game = game;
    }

    public String getDisplayName() {
        if (ObjectTools.isNotEmpty(name)) {
            displayName = game.getName() + "-" + name;
        } else {
            displayName = game.getName();
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getBaojiIdentityId() {
        return baojiIdentityId;
    }

    public void setBaojiIdentityId(Integer baojiIdentityId) {
        this.baojiIdentityId = baojiIdentityId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static void main(String[] args) {
        PagingResponse<TeamTypeSimpleVO> pagingResponse = new PagingResponse<>();
        pagingResponse.setOffset(1);
        pagingResponse.setLimit(10);
        pagingResponse.setTotal(20);
        pagingResponse.setPages(1);
        List<TeamTypeSimpleVO> simpleVOS = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            /*FreeTeamTypeSimpleVO simpleVO = new Builder()
                    .freeTeamTypeId(i).name("免费车队00" + i).gameName("游戏名称"+i).build();*/
            TeamTypeSimpleVO simpleVO = new TeamTypeSimpleVO();
            simpleVO.setTeamTypeId(i);
            simpleVO.setGameName("游戏名称_"+i);
            simpleVO.setName("上分一局_"+i);
            //simpleVO.setDisplayName();
            simpleVO.setBaojiIdentityId(i);
            simpleVOS.add(simpleVO);
        }
        pagingResponse.setList(simpleVOS);
/*
        System.out.println(
                JacksonUtils.toJsonWithSnake(ResponsePacket.onSuccess(pagingResponse)));*/
        System.out.println(
                JacksonUtils.toJsonWithSnake(ResponsePacket.onSuccess(simpleVOS)));
    }

}