package com.kaihei.esportingplus.gamingteam.api.vo;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WxSmallProgramPushData implements Serializable {

    private static final long serialVersionUID = 1164553804757652023L;
    private String touser;
    private String templateId;
    private String formId;
    private String page;
    private Map<String, ValueFieldVO> data;
    private String emphasisKeyword;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Map<String, ValueFieldVO> getData() {
        return data;
    }

    public void setData(
            Map<String, ValueFieldVO> data) {
        this.data = data;
    }

    public String getEmphasisKeyword() {
        return emphasisKeyword;
    }

    public void setEmphasisKeyword(String emphasisKeyword) {
        this.emphasisKeyword = emphasisKeyword;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
