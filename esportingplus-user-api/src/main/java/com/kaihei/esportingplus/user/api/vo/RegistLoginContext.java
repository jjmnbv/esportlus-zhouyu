package com.kaihei.esportingplus.user.api.vo;

import com.kaihei.esportingplus.user.api.params.RegistLoginBaseParam;
import com.kaihei.esportingplus.user.api.params.ThirdPartLoginParams;

/**
 * @Auther: chen.junyong
 * @Date: 2018-09-17 16:28
 * @Description:
 */
public class RegistLoginContext {

    private MembersUserVo userVo;

    private RegistLoginBaseParam params;

    private boolean isRegist = false;

    private String mDeviceId;

    private String version;

    public RegistLoginContext(){}

    public RegistLoginContext(MembersUserVo userVo,
            RegistLoginBaseParam params, boolean isRegist, String mDeviceId, String version) {
        this.userVo = userVo;
        this.params = params;
        this.isRegist = isRegist;
        this.mDeviceId = mDeviceId;
        this.version = version;
    }

    /**
     * 把原有对象属性设置到新对象上，序列化专用，把user设置到uservo上
     * @param from
     */
    public RegistLoginContext(RegistLoginContext from) {
        this.userVo = from.getUserVo();
        this.params = from.getParams();
        this.isRegist = from.isRegist();
        this.mDeviceId = from.getmDeviceId();
        this.version = from.getVersion();
    }

    public MembersUserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(MembersUserVo userVo) {
        this.userVo = userVo;
    }

    public RegistLoginBaseParam getParams() {
        return params;
    }

    public void setParams(RegistLoginBaseParam params) {
        this.params = params;
    }

    public boolean isRegist() {
        return isRegist;
    }

    public void setRegist(boolean regist) {
        isRegist = regist;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
