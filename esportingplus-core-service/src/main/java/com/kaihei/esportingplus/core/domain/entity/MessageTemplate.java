package com.kaihei.esportingplus.core.domain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/10/29 12:08
 **/
@Table(name = "tenkan_msgtemplate")
public class MessageTemplate {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "msg_type")
    private String msgType;

    @Column(name = "msg_class")
    private Integer msgClass;

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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(Integer msgClass) {
        this.msgClass = msgClass;
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
