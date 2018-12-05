package com.kaihei.esportingplus.api.params;

import com.kaihei.esportingplus.common.data.Castable;
import lombok.Data;

@Data
public class DictionaryUpdateParam implements Castable {

    /**
     * 字典Id
     */
    private Integer dictId;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典code
     */
    private String code;


    /**
     * 有效状态, 0:禁用，1：启用, 默认值
     */
    private Byte status;

    /**
     * 排序号
     */
    private Integer orderIndex;

    /**
     * 数据字典值
     */
    private Object value;
}
