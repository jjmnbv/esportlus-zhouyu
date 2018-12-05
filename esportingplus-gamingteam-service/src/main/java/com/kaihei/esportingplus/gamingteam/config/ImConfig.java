package com.kaihei.esportingplus.gamingteam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImConfig {
    @Value("${rongyun.im.sys.user}")
    private String sysUser;
    @Value("${rongyun.im.domain}")
    private String imDomain;
    @Value("${rongyun.im.group.create.uri}")
    private String createGroupUri;
    @Value("${rongyun.im.group.join.uri}")
    private String joinGroupUri;
    @Value("${rongyun.im.group.leave.uri}")
    private String leaveGroupUri;

    @Value("${rongyun.im.group.dismiss.uri}")
    private String dismissGroupUri;
    @Value("${rongyun.im.msg.send.uri}")
    private String sendMsgUri;
    @Value("${rongyun.im.cmd.template.id}")
    private Integer cmdTemplateId;
    @Value("${rongyun.im.cmd.template.system.id}")
    private Integer cmdSystemTemplateId;

    public String getImDomain() {
        return imDomain;
    }

    public void setImDomain(String imDomain) {
        this.imDomain = imDomain;
    }

    public String getCreateGroupUri() {
        return createGroupUri;
    }

    public void setCreateGroupUri(String createGroupUri) {
        this.createGroupUri = createGroupUri;
    }

    public String getJoinGroupUri() {
        return joinGroupUri;
    }

    public void setJoinGroupUri(String joinGroupUri) {
        this.joinGroupUri = joinGroupUri;
    }

    public String getLeaveGroupUri() {
        return leaveGroupUri;
    }

    public void setLeaveGroupUri(String leaveGroupUri) {
        this.leaveGroupUri = leaveGroupUri;
    }

    public String getDismissGroupUri() {
        return dismissGroupUri;
    }

    public void setDismissGroupUri(String dismissGroupUri) {
        this.dismissGroupUri = dismissGroupUri;
    }

    public String getSendMsgUri() {
        return sendMsgUri;
    }

    public void setSendMsgUri(String sendMsgUri) {
        this.sendMsgUri = sendMsgUri;
    }

    public String getSysUser() {
        return sysUser;
    }

    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }

    public Integer getCmdTemplateId() {
        return cmdTemplateId;
    }

    public void setCmdTemplateId(Integer cmdTemplateId) {
        this.cmdTemplateId = cmdTemplateId;
    }

    public Integer getCmdSystemTemplateId() {
        return cmdSystemTemplateId;
    }

    public void setCmdSystemTemplateId(Integer cmdSystemTemplateId) {
        this.cmdSystemTemplateId = cmdSystemTemplateId;
    }
}
