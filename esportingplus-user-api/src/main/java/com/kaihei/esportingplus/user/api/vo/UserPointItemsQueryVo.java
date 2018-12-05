package com.kaihei.esportingplus.user.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户鸡分明细
 *
 * @author xiekeqing
 * @version 1.0
 * @date 2018/10/9 12:03
 */
public class UserPointItemsQueryVo implements Serializable {

    private static final long serialVersionUID = 5033272235590872670L;

    /**
     * 明细类型，0：兑换暴鸡值；1：开免费车队；2：获得评分
     */
    private Integer itemType;

    /**
     * 鸡分明细值
     */
    private Integer itemAcmount;

    /**
     * 明细描述
     */
    private String instructions;

    /**
     * 车队标识符
     */
    private String slug;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDatetime;

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemAcmount() {
        return itemAcmount;
    }

    public void setItemAcmount(Integer itemAcmount) {
        this.itemAcmount = itemAcmount;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * 拼接积分值
     */
    public String getPoint(){
        if(itemAcmount == null){
            return "0";
        }
        if(itemAcmount.intValue()<0) {
            //拼接减少鸡分值
            return new StringBuffer("-").
                    append(" ").
                    append(Math.abs(itemAcmount)).toString();
        }else{
            //拼接增加鸡分值
            return new StringBuffer("+").
                    append(" ").
                    append(itemAcmount.toString()).toString();
        }
    }
}
