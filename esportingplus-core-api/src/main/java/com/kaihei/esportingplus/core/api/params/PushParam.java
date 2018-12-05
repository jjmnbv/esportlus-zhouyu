package com.kaihei.esportingplus.core.api.params;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Author liuyang
 * @Description 消息推送参数
 * @Date 2018/11/6 17:15
 **/
@Validated
public class PushParam {

    /**
     * 目标操作系统，iOS、Android 最少传递一个
     */
    @NotEmpty(message = "目标操作系统，iOS、Android 最少传递一个")
    private List<String> platform;

    /**
     * 推送条件，包括：tag 、userid 、packageName 、 is_to_all
     */
    @NotNull(message = "推送条件，包括：tag 、userid 、packageName 、 is_to_all")
    private Audience audience;

    /**
     * 发送人用户 Id
     */
    @NotEmpty(message = "发送人用户 Id不能为空")
    private String fromuserid;

    /**
     * 发送消息内容，参考融云 Server API 消息类型表.示例说明；如果 objectName 为自定义消息类型，该参数可自定义格式。
     */
    private Message message;

    /**
     * 按操作系统类型推送消息内容，如 platform 中设置了给 iOS 和 Android 系统推送消息，而在 notification 中只设置了 iOS 的推送内容，则 Android 的推送内容为最初 alert 设置的内容。
     */
    private Notification notification;

    public static class Message {
        private String content;

        private String objectName;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }
    }


    public static class Audience {
        /**
         * 用户标签，每次发送时最多发送 20 个标签，标签之间为 AND 的关系，is_to_all 为 true 时参数无效
         */
        private List<String> tag;

        /**
         * 用户标签，每次发送时最多发送 20 个标签，标签之间为 OR 的关系，is_to_all 为 true 时参数无效，tag_or 同 tag 参数可以同时存在
         */
        private List<String> tag_or;

        /**
         * 用户 Id，每次发送时最多发送 1000 个用户，如果 tag 和 userid 两个条件同时存在时，则以 userid 为准，如果 userid 有值时，则 platform 参数无效，is_to_all 为 true 时参数无效
         */
        private List<String> userid;

        /**
         * 是否全部推送，false 表示按 tag 、tag_or 或 userid 条件推送，true 表示向所有用户推送，tag、tag_or 和 userid 条件无效。
         */
        private boolean is_to_all = false;

        public List<String> getTag() {
            return tag;
        }

        public void setTag(List<String> tag) {
            this.tag = tag;
        }

        public List<String> getTag_or() {
            return tag_or;
        }

        public void setTag_or(List<String> tag_or) {
            this.tag_or = tag_or;
        }

        public List<String> getUserid() {
            return userid;
        }

        public void setUserid(List<String> userid) {
            this.userid = userid;
        }

        public boolean isIs_to_all() {
            return is_to_all;
        }

        public void setIs_to_all(boolean is_to_all) {
            this.is_to_all = is_to_all;
        }
    }

    public static class Notification {

        /**
         * notification 下 alert，默认推送消息内容，如填写了 iOS 或 Android 下的 alert 时，则推送内容以对应平台系统的 alert 为准。
         */
        private String alert;

        private Ios ios;

        private Android android;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public Ios getIos() {
            return ios;
        }

        public void setIos(Ios ios) {
            this.ios = ios;
        }

        public Android getAndroid() {
            return android;
        }

        public void setAndroid(Android android) {
            this.android = android;
        }
    }

    @Validated
    public static class Ios {
        private String title;

        /**
         * iOS 或 Android 不同平台下的推送消息内容，传入后默认的推送消息内容失效，不能为空。
         */
        @NotNull(message = "alert不能为空")
        private String alert;

        /**
         * 针对 iOS 平台，静默推送是 iOS7 之后推出的一种推送方式。 允许应用在收到通知后在后台运行一段代码，且能够马上执行，查看详细。1 表示为开启，0 表示为关闭，默认为 0
         */
        private Integer contentAvailable;

        /**
         * 应用角标，仅针对 iOS 平台；不填时，表示不改变角标数；为 0 或负数时，表示 App 角标上的数字清零；否则传相应数字表示把角标数改为指定的数字，最大不超过 9999，参数在 ios 节点下设置，详细可参考“设置 iOS 角标数 HTTP 请求示例”
         */
        private Integer badge;

        /**
         * iOS 或 Android 不同平台下的附加信息，如果开发者自己需要，可以自己在 App 端进行解析
         */
        private Map extras;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public Integer getContentAvailable() {
            return contentAvailable;
        }

        public void setContentAvailable(Integer contentAvailable) {
            this.contentAvailable = contentAvailable;
        }

        public Integer getBadge() {
            return badge;
        }

        public void setBadge(Integer badge) {
            this.badge = badge;
        }

        public Map getExtras() {
            return extras;
        }

        public void setExtras(Map extras) {
            this.extras = extras;
        }
    }

    public static class Android {
        /**
         * iOS 或 Android 不同平台下的附加信息，如果开发者自己需要，可以自己在 App 端进行解析
         */
        private Map extras;

        /**
         * iOS 或 Android 不同平台下的推送消息内容，传入后默认的推送消息内容失效，不能为空。
         */
        @NotNull(message = "alert不能为空")
        private String alert;

        public Map getExtras() {
            return extras;
        }

        public void setExtras(Map extras) {
            this.extras = extras;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }
    }


    public List<String> getPlatform() {
        return platform;
    }

    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
