package com.kaihei.esportingplus.marketing.api.event;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * 免费车队用户评论事件相关参数
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/10 11:44
 */
@Validated
public class TeamFreeObtainstarEvent extends TeamFreeEvent {

    private static final long serialVersionUID = 8222846067004666804L;

    /**
     * 评论用户uid
     */
    @NotNull(message = "评论用户ID")
    private String commentUserid;

    /**
     * 评星10-50
     */
    @NotNull(message = "评星不能为空")
    private String star;



    /**
     * 订单时间
     */
    @NotNull(message = "订单时间不能为空")
    private Date orderTime;

    /**
     * 游戏局数
     */
    @NotNull(message = "游戏局数不能为空")
    private Integer gameNum;

    /**
     * 匹配段位
     */
    @NotNull(message = "匹配段位不能为空")
    private String dan;

    public String getCommentUserid() {
        return commentUserid;
    }

    public void setCommentUserid(String commentUserid) {
        this.commentUserid = commentUserid;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getGameNum() {
        return gameNum;
    }

    public void setGameNum(Integer gameNum) {
        this.gameNum = gameNum;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }
}
