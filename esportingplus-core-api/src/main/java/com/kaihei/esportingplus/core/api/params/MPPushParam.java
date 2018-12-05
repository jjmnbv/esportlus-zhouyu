package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author liuyang
 * @Description //TODO
 * @Date 2018/11/5 14:28
 **/
@Validated
public class MPPushParam {

    /***/
    @NotNull(message = "touser 不能为空")
    String touser;
    String formid;
    String templateId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。
     */
    String page;

    /**
     * 模板内容
     */
    String data;

    /**
     * 模板需要放大的关键词，不填则默认无放大
     */
    String emphasisKkeyword;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmphasisKkeyword() {
        return emphasisKkeyword;
    }

    public void setEmphasisKkeyword(String emphasisKkeyword) {
        this.emphasisKkeyword = emphasisKkeyword;
    }
}
