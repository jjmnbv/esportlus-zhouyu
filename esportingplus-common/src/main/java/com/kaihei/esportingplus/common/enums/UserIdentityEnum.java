package com.kaihei.esportingplus.common.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 用户游戏身份枚举
 *
 * @author zhangfang
 */
public enum UserIdentityEnum {

    /** 老板 */
    BOSS(0, "老板", "boss", Collections.singletonList(BaojiLevelEnum.BOSS)),

    /** 暴鸡 */
    BAOJI(1, "暴鸡", "baoji",
            Arrays.asList(BaojiLevelEnum.COMMON, BaojiLevelEnum.PREFERENCE, BaojiLevelEnum.SUPER)),

    /** 暴娘 */
    BN(2, "暴娘", "bn", Collections.singletonList(BaojiLevelEnum.BN)),

    /** 暴鸡暴娘均可 */
    BJ_BN(3, "暴鸡暴娘均可", "",
            Arrays.asList(BaojiLevelEnum.COMMON, BaojiLevelEnum.PREFERENCE, BaojiLevelEnum.SUPER,
                    BaojiLevelEnum.BN)),

    /** 队长 */
    LEADER(9, "队长", "", null);



    private int code;
    private String desc;
    private String pythonCode;

    private List<BaojiLevelEnum> baojiLevelEnumList;

    UserIdentityEnum(int code, String desc, String pythonCode,
            List<BaojiLevelEnum> baojiLevelEnumList) {
        this.code = code;
        this.desc = desc;
        this.pythonCode = pythonCode;
        this.baojiLevelEnumList = baojiLevelEnumList;
    }

    public List<BaojiLevelEnum> getBaojiLevelEnumList() {
        return baojiLevelEnumList;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getPythonCode() {
        return pythonCode;
    }

    public static UserIdentityEnum getByCode(int code) {
        UserIdentityEnum[] values = UserIdentityEnum.values();
        for (UserIdentityEnum userIdentityEnum : values) {
            if (userIdentityEnum.getCode() == code) {
                return userIdentityEnum;
            }
        }
        return null;
    }

    public static UserIdentityEnum of(int code) {
        return getByCode(code);
    }
}
