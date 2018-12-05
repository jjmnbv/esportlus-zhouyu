package com.kaihei.esportingplus.payment.migrate.constant;

import com.google.common.collect.Sets;
import com.kaihei.esportingplus.payment.api.enums.ExternalPayStateEnum;
import java.util.List;
import java.util.Set;
import org.assertj.core.util.Lists;

/**
 * @author: tangtao
 **/
public class TypeMapping {

    public static final Set<Integer> recharge_bill_type = Sets.newHashSet(4, 13, 15);

    public static final Set<Integer> payment_bill_type = Sets
            .newHashSet(7, 15, 16, 18, 19, 33, 34, 40, 41, 57, 60, 62, 64, 71, 82, 83, 85, 87, 88, 89);
    public static final Set<Integer> refund_bill_type = Sets
            .newHashSet(6, 14, 17, 21, 22, 30, 31, 37, 38, 56, 58, 61, 63, 73, 74, 75, 79, 80, 86, 90, 94);
    public static final Set<Integer> withdraw_bill_type = Sets.newHashSet(5, 65, 66);

    public static final Set<Integer> withdraw_yun_bill_type = Sets.newHashSet(65, 66);

//
//    public static final Set<Integer> qq_bill_type = Sets.newHashSet(55, 60, 62, 64, 71, 85, 56, 57, 58, 61, 63, 75, 86);
//    public static final Set<Integer> wechat_bill_type = Sets.newHashSet(4, 7, 19, 34, 41, 87, 83, 89, 17, 16, 22, 31, 38, 73, 80, 94, 90, 5, 6, 66);
//    public static final Set<Integer> ali_bill_type = Sets.newHashSet(13, 18, 33, 40, 88, 82, 14, 15, 21, 30, 37, 74, 79, 65);

    /**
     * 0	预支付 1	已付款 2	支付失败 3	已发货 4	已确认
     */
    private static final List<String> status_mapping = Lists.newArrayList(
            ExternalPayStateEnum.UNPAIED.getCode(),
            ExternalPayStateEnum.SUCCESS.getCode(),
            ExternalPayStateEnum.CLOSED.getCode(),
            ExternalPayStateEnum.SUCCESS.getCode(),
            ExternalPayStateEnum.SUCCESS.getCode()
    );

    // TODO 待确认
    public static String getSourceAppId(String packageName) {
        switch (packageName.toLowerCase()) {
            case "kaihei":
                return "IOS_BJDJ";
            case "liudu":
                return "IOS_BJ";
            case "miniprogram":
                return "WECHAT_MP_BJDJ";
            case "app":
                return "ANDROID_BJDJ";
            default:
                return "ANDROID_BJDJ";
        }
    }

    public static String getStatus(Integer status) {
        return status_mapping.get(status);
    }


    public static boolean isRefund(Integer billType) {
        return refund_bill_type.contains(billType);
    }

    public static boolean isPayment(Integer billType) {
        return payment_bill_type.contains(billType);
    }

    public static boolean isRecharge(Integer billType) {
        return recharge_bill_type.contains(billType);
    }

    public static boolean isWithdraw(Integer billType) {
        return withdraw_bill_type.contains(billType);
    }
}
