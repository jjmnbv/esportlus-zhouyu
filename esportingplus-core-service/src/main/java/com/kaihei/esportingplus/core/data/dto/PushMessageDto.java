package com.kaihei.esportingplus.core.data.dto;

/**
 * @program: esportingplus
 * @description: push消息
 * @author: xusisi
 * @create: 2018-12-03 11:13
 **/
public class PushMessageDto {

    /***
     * 消息主键
     */
    private Integer id;

    /***
     * 发送渠道：1、站内消息，2、站外push
     */
    private Integer pushChannel;

    /***
     *推送形式：图文推送(RichContentMessage)，纯文字推送(TextMessage)
     */
    private String pushForm;

    /***
     * 推送标题
     */
    private String title;

    /***
     * 推送内容
     */
    private String content;

    /***
     * 消息中图片地址
     */
    private String imageUri;

    /***
     * 后续动作（单选必填）：
     * 1、打开APP首页、2、打开APP内部页面、3、打开内部H5页面，4、打开第三方网页
     */
    private Integer followAction;

    /***
     * 跳转地址
     * 若选择打开app内部页面、内部h5页面或第三方网页，则《跳转地址》项必填
     */
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(Integer pushChannel) {
        this.pushChannel = pushChannel;
    }

    public String getPushForm() {
        return pushForm;
    }

    public void setPushForm(String pushForm) {
        this.pushForm = pushForm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Integer getFollowAction() {
        return followAction;
    }

    public void setFollowAction(Integer followAction) {
        this.followAction = followAction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
