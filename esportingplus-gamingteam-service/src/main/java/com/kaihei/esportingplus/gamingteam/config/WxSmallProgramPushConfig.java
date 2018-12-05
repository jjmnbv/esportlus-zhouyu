package com.kaihei.esportingplus.gamingteam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxSmallProgramPushConfig {
    @Value("${wx.small.program.push.domain}")
    private String pushDomain;
    @Value("${wx.small.program.push.uri}")
    private String pushUri;
    @Value("${wx.small.program.push.order.end.template.id}")
    private String orderEndTemplateId;
    @Value("${wx.small.program.push.order.end.page}")
    private String orderEndPage;
    @Value("${wx.small.program.push.order.cancel.template.id}")
    private String orderCancelTemplateId;
    @Value("${wx.small.program.push.order.cancel.page}")
    private String orderCancelPage;
    @Value("${wx.small.program.push.team.start.template.id}")
    private String teamStartTemplateId;
    @Value("${wx.small.program.push.team.start.page}")
    private String teamStartPage;
    public String getPushDomain() {
        return pushDomain;
    }

    public void setPushDomain(String pushDomain) {
        this.pushDomain = pushDomain;
    }

    public String getPushUri() {
        return pushUri;
    }

    public void setPushUri(String pushUri) {
        this.pushUri = pushUri;
    }

    public String getOrderEndTemplateId() {
        return orderEndTemplateId;
    }

    public void setOrderEndTemplateId(String orderEndTemplateId) {
        this.orderEndTemplateId = orderEndTemplateId;
    }

    public String getOrderCancelTemplateId() {
        return orderCancelTemplateId;
    }

    public void setOrderCancelTemplateId(String orderCancelTemplateId) {
        this.orderCancelTemplateId = orderCancelTemplateId;
    }

    public String getTeamStartTemplateId() {
        return teamStartTemplateId;
    }

    public void setTeamStartTemplateId(String teamStartTemplateId) {
        this.teamStartTemplateId = teamStartTemplateId;
    }

    public String getTeamStartPage() {
        return teamStartPage;
    }

    public void setTeamStartPage(String teamStartPage) {
        this.teamStartPage = teamStartPage;
    }

    public String getOrderCancelPage() {
        return orderCancelPage;
    }

    public void setOrderCancelPage(String orderCancelPage) {
        this.orderCancelPage = orderCancelPage;
    }

    public String getOrderEndPage() {
        return orderEndPage;
    }

    public void setOrderEndPage(String orderEndPage) {
        this.orderEndPage = orderEndPage;
    }
}
