package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:相片状态
 * @date: 2018/10/25 15:18
 */
public enum AlbumPictureType {
    PICTURE_STATUS_DELETE(0,"已删除"),
    PICTURE_STATUS_REPLACE(1,"被替换"),
    PICTURE_STATUS_VERIFYING(2,"待审批"),
    PICTURE_STATUS_VERIFY_FAIL(3,"审批不通过"),
    PICTURE_STATUS_VERIFY_SUCCESS(4,"审批通过");
    private Integer code;
    private String msg;

    AlbumPictureType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
