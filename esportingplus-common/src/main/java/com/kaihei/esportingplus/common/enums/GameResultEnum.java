package com.kaihei.esportingplus.common.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 游戏结果枚举
 *
 * @author Orochi-Yzh
 * @dateTime 2018/1/24 18:19
 * @updatetor
 */
public enum GameResultEnum {

    /** 局--失败 */
    ROUNDS_DEFEAT(0, "rounds_fail", "失败", SettlementTypeEnum.ROUND),

    /** 局--胜利 */
    ROUNDS_VICTORY(1, "rounds_victory", "胜利", SettlementTypeEnum.ROUND),

    /** 局--未打 */
    ROUNDS_NOT_PLAY(2, "rounds_notplay", "未打", SettlementTypeEnum.ROUND),

    /** 小时--已打 */
    HOURS_PLAYED(3, "hours_played", "已打", SettlementTypeEnum.HOUR),

    /** 小时--未打 */
    HOURS_NOT_PLAY(4, "hours_notplay", "未打", SettlementTypeEnum.HOUR),

    /** 车队准备中被解散, 比赛结果为空 */
    UNKNOWN(-1, "", "", SettlementTypeEnum.UNKNOWN);

    /**
     * code 码, 与 Python端保持一致
     */
    private int code;

    /**
     * 数据字典的 code
     */
    private String dictCode;

    /**
     * 描述
     */
    private String desc;

    /**
     * 结算类型
     */
    private SettlementTypeEnum settlementTypeEnum;

    GameResultEnum(int code, String dictCode, String desc, SettlementTypeEnum settlementTypeEnum) {
        this.code = code;
        this.dictCode = dictCode;
        this.desc = desc;
        this.settlementTypeEnum = settlementTypeEnum;
    }

    public static List<GameResultEnum> findBySettlementType(SettlementTypeEnum settlementTypeEnum) {
        return Arrays.stream(GameResultEnum.values())
                .filter(it -> it.getSettlementTypeEnum().equals(settlementTypeEnum))
                .collect(
                        Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(fromCode(1));
    }
    public static GameResultEnum findByCodeAndSettlementType(int code, SettlementTypeEnum settlementTypeEnum) {
        for (GameResultEnum c : GameResultEnum.values()) {
            if (c.code == code && c.getSettlementTypeEnum().equals(settlementTypeEnum)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    private static Set<Integer> positiveGameResultCodes = new HashSet<>(Arrays.asList(1, 3));

    public static GameResultEnum findPositiveGameResult(SettlementTypeEnum settlementTypeEnum) {
        return Arrays.stream(GameResultEnum.values())
                .filter(it -> positiveGameResultCodes.contains(it.getCode()))
                .filter(it -> it.getSettlementTypeEnum().equals(settlementTypeEnum))
                .findFirst()
                .orElse(null);
    }

    public static GameResultEnum fromCode(int code) {
        for (GameResultEnum c : GameResultEnum.values()) {
            if (c.code == code) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public static GameResultEnum fromDesc(String desc) {
        for (GameResultEnum c : GameResultEnum.values()) {
            if (c.desc.equals(desc)) {
                return c;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SettlementTypeEnum getSettlementTypeEnum() {
        return settlementTypeEnum;
    }
}