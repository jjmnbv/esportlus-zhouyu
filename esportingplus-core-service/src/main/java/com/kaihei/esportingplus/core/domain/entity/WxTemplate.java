package com.kaihei.esportingplus.core.domain.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/11/7 17:09
 **/
@Table(name = "tenkan_wxnotifyform")
public class WxTemplate {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "formid")
    private String formId;

    @Column(name = "openid")
    private String openId;

    @Column(name = "unionid")
    private String unionId;

    @Column(name = "expired_day")
    private String expiredDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(String expiredDay) {
        this.expiredDay = expiredDay;
    }
}
