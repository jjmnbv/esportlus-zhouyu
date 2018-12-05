package com.kaihei.esportingplus.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author liuyang
 * @Description 模板格式化
 * @Date 2018/10/25 15:16
 **/
public interface TemplateFormat<T> {

    String formatSpecifier = "\\{[a-zA-Z]+\\}";

    Pattern pattern = Pattern.compile(formatSpecifier);

    enum Type {
        SMSTEMPLATE (EXPRESSIONFORMAT);
        TemplateFormat format;

        Type(TemplateFormat format) {
            this.format = format;
        }

        public TemplateFormat getFormat() {
            return format;
        }
    }

    T format(String source, String... args);

    /**
     * 格式化字符串， 替换{} 内容
     *
     * @param source
     * @param args
     * @return
     */
    TemplateFormat<String> EXPRESSIONFORMAT = (source, args) -> {
        Matcher matcher = pattern.matcher(source);
        String sourceCopy = new String(source);
        int i = 0;
        while (matcher.find()) {
            sourceCopy = sourceCopy.replace(matcher.group(), args[i++]);
        }

        return sourceCopy;
    };
}
