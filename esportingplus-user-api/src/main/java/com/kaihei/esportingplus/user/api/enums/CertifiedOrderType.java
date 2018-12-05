package com.kaihei.esportingplus.user.api.enums;

/**
 * @author linruihe
 * @description:订单状态
 * @date: 2018/10/29 15:18
 */
public enum CertifiedOrderType {
    STATUS_CREATE(1,"创建订单(待付款)"),
    STATUS_FAIL(2,"付款失败"),
    STATUS_CERTIFIED(4,"付款成功"),
    STATUS_CANCEL_VERIFY(5,"取消认证待审批(未退款)"),
    STATUS_CANCEL(6,"已取消认证(已退款)"),
    STATUS_FREEZE(7,"认证冻结(无法接单)");
    private Integer code;
    private String msg;

    CertifiedOrderType(Integer code, String msg) {
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
