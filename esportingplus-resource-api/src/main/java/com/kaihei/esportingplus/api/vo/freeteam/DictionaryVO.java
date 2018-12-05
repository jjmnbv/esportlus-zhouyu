package com.kaihei.esportingplus.api.vo.freeteam;

import com.kaihei.esportingplus.common.data.Castable;
import lombok.Data;

@Data
public class DictionaryVO<T> implements Castable {

    /**
     * 字典Id
     */
    private Integer dictId;

    /**
     * 字典名
     */
    private String name;

    /**
     * 字典Code
     */
    private String code;

    /**
     * 1启用 0不启用
     */
    private Integer status;

    /**
     * 由数据库Value反序列化的对象
     */
    private T value;
}
