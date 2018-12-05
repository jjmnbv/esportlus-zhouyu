package com.kaihei.esportingplus.common.file;

import java.io.UnsupportedEncodingException;

/**
 * 敏感字过滤
 * @author linruihe
 * @date 2018/10/26
 */
public interface WordFilterService {

    public boolean checkWord(String works) throws UnsupportedEncodingException;
}
