package com.kaihei.esportingplus.core.data.dto;

/**
 * @Author liuyang
 * @Description  发送短信参数类
 * @Date 2018/10/25 15:57
 **/
public class ChuanglanSmsSendDto {

    private String account;
    private String password;
    private boolean report = false;
    private String msg;
    private String phone;

    public ChuanglanSmsSendDto(String account, String password, String msg, String phone) {
        this.account = account;
        this.password = password;
        this.msg = msg;
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
