package com.kaihei.esportingplus.payment.api.params;

/**
 * @program: esportingplus
 * @description: 验证账户时需要传递的参数
 * @author: xusisi
 * @create: 2018-09-30 16:17
 **/
public class AccountValidateParam {

    /***
     * 账户类型：（001暴鸡币，002暴击值）
     */
    private String accountType;

    /***
     * 用户ID
     */
    private String userId;

    /***
     * 操作金额（单位：分）
     */
    private Integer operateAmount;


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getOperateAmount() {
        return operateAmount;
    }

    public void setOperateAmount(Integer operateAmount) {
        this.operateAmount = operateAmount;
    }


    @Override
    public String toString() {
        return "AccountValidateParam{" +
                "accountType='" + accountType + '\'' +
                ", userId='" + userId + '\'' +
                ", operateAmount=" + operateAmount +
                '}';
    }
}
