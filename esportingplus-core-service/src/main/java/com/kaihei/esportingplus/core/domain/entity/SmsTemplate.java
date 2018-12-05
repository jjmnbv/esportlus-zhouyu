package com.kaihei.esportingplus.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author liuyang
 * @Description 短信模板
 * @Date 2018/10/24 16:36
 **/
@Table(name = "tenkan_smstemplate")
public class SmsTemplate {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "template_id")
    private Integer templateId;
    private Integer channel;
    private String name;
    private String template;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
