package com.kaihei.esportingplus.customer.center.domain.entity;

import lombok.Builder;

import java.util.Date;
import javax.persistence.*;

@Builder
@Table(name = "customer_order_evaluate")
public class CustomerOrderEvaluate {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 评价星级
     */
    private Short star;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评用户uid
     */
    @Column(name = "author_uid")
    private String authorUid;

    /**
     * 评价用户id
     */
    @Column(name = "author_id")
    private Integer authorId;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 订单类型
     */
    @Column(name = "order_type")
    private Short orderType;

    /**
     * 暴鸡id
     */
    @Column(name = "baoji_uid")
    private String baojiUid;

    /**
     * 关联资源id(普通单:暴鸡uid,车队单:车队id,服务单:自定义技能id)
     */
    @Column(name = "relate_source_id")
    private String relateSourceId;

    /**
     * 关联资源描述
     */
    @Column(name = "relate_soruce_desc")
    private String relateSoruceDesc;

    /**
     * 暴鸡类型(BJ:暴鸡,BN:暴娘)
     */
    @Column(name = "baoji_type")
    private String baojiType;

    /**
     * 暴鸡级别(99:冻结暴鸡,100:普通暴鸡,101:优选暴鸡,102:超级暴鸡)
     */
    @Column(name = "baoji_level")
    private Short baojiLevel;

    /**
     * 订单完成时间
     */
    @Column(name = "order_finish_time")
    private String orderFinishTime;

    /**
     * 游戏局数
     */
    @Column(name = "game_round")
    private Short gameRound;

    /**
     * 比赛结果(0输1赢2没打以英文逗号(,)隔开)
     */
    @Column(name = "game_result")
    private String gameResult;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * 删除时间
     */
    @Column(name = "delete_time")
    private Date deleteTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public CustomerOrderEvaluate(Integer id, Short star, String content, String authorUid, Integer authorId, Integer orderId, Short orderType, String baojiUid, String relateSourceId, String relateSoruceDesc, String baojiType, Short baojiLevel, String orderFinishTime, Short gameRound, String gameResult, Boolean isDeleted, Date deleteTime, Date createTime, Date updateTime) {
        this.id = id;
        this.star = star;
        this.content = content;
        this.authorUid = authorUid;
        this.authorId = authorId;
        this.orderId = orderId;
        this.orderType = orderType;
        this.baojiUid = baojiUid;
        this.relateSourceId = relateSourceId;
        this.relateSoruceDesc = relateSoruceDesc;
        this.baojiType = baojiType;
        this.baojiLevel = baojiLevel;
        this.orderFinishTime = orderFinishTime;
        this.gameRound = gameRound;
        this.gameResult = gameResult;
        this.isDeleted = isDeleted;
        this.deleteTime = deleteTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public CustomerOrderEvaluate() {
        super();
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取评价星级
     *
     * @return star - 评价星级
     */
    public Short getStar() {
        return star;
    }

    /**
     * 设置评价星级
     *
     * @param star 评价星级
     */
    public void setStar(Short star) {
        this.star = star;
    }

    /**
     * 获取评价内容
     *
     * @return content - 评价内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评价内容
     *
     * @param content 评价内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取评用户uid
     *
     * @return author_uid - 评用户uid
     */
    public String getAuthorUid() {
        return authorUid;
    }

    /**
     * 设置评用户uid
     *
     * @param authorUid 评用户uid
     */
    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid == null ? null : authorUid.trim();
    }

    /**
     * 获取评价用户id
     *
     * @return author_id - 评价用户id
     */
    public Integer getAuthorId() {
        return authorId;
    }

    /**
     * 设置评价用户id
     *
     * @param authorId 评价用户id
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单类型
     *
     * @return order_type - 订单类型
     */
    public Short getOrderType() {
        return orderType;
    }

    /**
     * 设置订单类型
     *
     * @param orderType 订单类型
     */
    public void setOrderType(Short orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取暴鸡id
     *
     * @return baoji_id - 暴鸡id
     */
    public String getBaojiUid() {
        return baojiUid;
    }

    /**
     * 设置暴鸡id
     *
     * @param baojiId 暴鸡id
     */
    public void setBaojiUid(String baojiId) {
        this.baojiUid = baojiId;
    }

    /**
     * 获取关联资源id(普通单:暴鸡id,车队单:车队id,服务单:自定义技能id)
     *
     * @return relate_source_id - 关联资源id(普通单:暴鸡id,车队单:车队id,服务单:自定义技能id)
     */
    public String getRelateSourceId() {
        return relateSourceId;
    }

    /**
     * 设置关联资源id(普通单:暴鸡id,车队单:车队id,服务单:自定义技能id)
     *
     * @param relateSourceId 关联资源id(普通单:暴鸡id,车队单:车队id,服务单:自定义技能id)
     */
    public void setRelateSourceId(String relateSourceId) {
        this.relateSourceId = relateSourceId;
    }

    /**
     * 获取关联资源描述
     *
     * @return relate_soruce_desc - 关联资源描述
     */
    public String getRelateSoruceDesc() {
        return relateSoruceDesc;
    }

    /**
     * 设置关联资源描述
     *
     * @param relateSoruceDesc 关联资源描述
     */
    public void setRelateSoruceDesc(String relateSoruceDesc) {
        this.relateSoruceDesc = relateSoruceDesc == null ? null : relateSoruceDesc.trim();
    }

    /**
     * 获取暴鸡类型(BJ:暴鸡,BN:暴娘)
     *
     * @return baoji_type - 暴鸡类型(BJ:暴鸡,BN:暴娘)
     */
    public String getBaojiType() {
        return baojiType;
    }

    /**
     * 设置暴鸡类型(BJ:暴鸡,BN:暴娘)
     *
     * @param baojiType 暴鸡类型(BJ:暴鸡,BN:暴娘)
     */
    public void setBaojiType(String baojiType) {
        this.baojiType = baojiType == null ? null : baojiType.trim();
    }

    /**
     * 获取暴鸡级别(99:冻结暴鸡,100:普通暴鸡,101:优选暴鸡,102:超级暴鸡)
     *
     * @return baoji_level - 暴鸡级别(99:冻结暴鸡,100:普通暴鸡,101:优选暴鸡,102:超级暴鸡)
     */
    public Short getBaojiLevel() {
        return baojiLevel;
    }

    /**
     * 设置暴鸡级别(99:冻结暴鸡,100:普通暴鸡,101:优选暴鸡,102:超级暴鸡)
     *
     * @param baojiLevel 暴鸡级别(99:冻结暴鸡,100:普通暴鸡,101:优选暴鸡,102:超级暴鸡)
     */
    public void setBaojiLevel(Short baojiLevel) {
        this.baojiLevel = baojiLevel;
    }

    /**
     * 获取订单完成时间
     *
     * @return order_finish_time - 订单完成时间
     */
    public String getOrderFinishTime() {
        return orderFinishTime;
    }

    /**
     * 设置订单完成时间
     *
     * @param orderFinishTime 订单完成时间
     */
    public void setOrderFinishTime(String orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    /**
     * 获取游戏局数
     *
     * @return game_round - 游戏局数
     */
    public Short getGameRound() {
        return gameRound;
    }

    /**
     * 设置游戏局数
     *
     * @param gameRound 游戏局数
     */
    public void setGameRound(Short gameRound) {
        this.gameRound = gameRound;
    }

    /**
     * 获取比赛结果(0输1赢2没打以英文逗号(,)隔开)
     *
     * @return game_result - 比赛结果(0输1赢2没打以英文逗号(,)隔开)
     */
    public String getGameResult() {
        return gameResult;
    }

    /**
     * 设置比赛结果(0输1赢2没打以英文逗号(,)隔开)
     *
     * @param gameResult 比赛结果(0输1赢2没打以英文逗号(,)隔开)
     */
    public void setGameResult(String gameResult) {
        this.gameResult = gameResult == null ? null : gameResult.trim();
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取删除时间
     *
     * @return delete_time - 删除时间
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * 设置删除时间
     *
     * @param deleteTime 删除时间
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}