package com.kaihei.esportingplus.user.api.vo;

public class QrCodeVo {
    private String avatarLink;
    private String qrCodeLink;

    public QrCodeVo() {
    }

    public QrCodeVo(String avatarLink, String qrCodeLink) {
        this.avatarLink = avatarLink;
        this.qrCodeLink = qrCodeLink;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getQrCodeLink() {
        return qrCodeLink;
    }

    public void setQrCodeLink(String qrCodeLink) {
        this.qrCodeLink = qrCodeLink;
    }
}
