package com.kaihei.esportingplus.marketing.api.event;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author xiekeqing
 */
public class UserEvent implements Serializable {

    private static final long serialVersionUID = -7552809216063945211L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
