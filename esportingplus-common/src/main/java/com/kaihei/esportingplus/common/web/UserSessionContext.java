package com.kaihei.esportingplus.common.web;

import com.kaihei.esportingplus.common.constant.CommonConstants;
import com.kaihei.esportingplus.common.data.JsonSerializable;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Orochi-Yzh
 * @Description: python 用户信息
 * @dateTime 2018/8/6 10:13
 */
public class UserSessionContext implements JsonSerializable {

    private static final long serialVersionUID = 1495413060843290738L;
    private int id;
    private String username;
    private String uid;
    private String avatar;
    private int sex;
    private String chickenId;
    private String pythonToken;
    //  private String region;
//  private String desc;
//  private int age;
//  private int constellation;
//  private int birthday;
//  private boolean show_group;


    public UserSessionContext() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getChickenId() {
        return chickenId;
    }

    public void setChickenId(String chickenId) {
        this.chickenId = chickenId;
    }

    public String getPythonToken() {
        return pythonToken;
    }

    public void setPythonToken(String pythonToken) {
        this.pythonToken = pythonToken;
    }
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getRegion() {
//        return region;
//    }
//
//    public void setRegion(String region) {
//        this.region = region;
//    }
//
//    public String getChicken_id() {
//        return chicken_id;
//    }
//
//    public void setChicken_id(String chicken_id) {
//        this.chicken_id = chicken_id;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public int getConstellation() {
//        return constellation;
//    }
//
//    public void setConstellation(int constellation) {
//        this.constellation = constellation;
//    }
//
//    public int getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(int birthday) {
//        this.birthday = birthday;
//    }
//
//    public boolean isShow_group() {
//        return show_group;
//    }
//
//    public void setShow_group(boolean show_group) {
//        this.show_group = show_group;
//    }


    /**
     * 从上下文中获取当前登录的用户信息
     * @return
     */
    public static UserSessionContext getUser() {
        UserSessionContext userSessionContext = (UserSessionContext) RequestContextHolder.getRequestAttributes()
                .getAttribute(CommonConstants.USER_INFO, RequestAttributes.SCOPE_REQUEST);
        if (ObjectTools.isNotEmpty(userSessionContext)
                && ObjectTools.isNotEmpty(userSessionContext.getUid())) {
            return userSessionContext;
        }
         throw new BusinessException(BizExceptionEnum.INVALID_TOKEN);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

