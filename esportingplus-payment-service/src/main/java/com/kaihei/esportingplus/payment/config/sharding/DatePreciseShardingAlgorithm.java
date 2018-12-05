package com.kaihei.esportingplus.payment.config.sharding;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * @author: tangtao
 **/
public class DatePreciseShardingAlgorithm implements PreciseShardingAlgorithm {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        LocalDateTime dateTime;
        if (preciseShardingValue.getValue() instanceof String) {
            dateTime = LocalDateTime.parse((String) preciseShardingValue.getValue(), dateTimeFormatter);
        } else {
            dateTime = ((Timestamp) preciseShardingValue.getValue()).toLocalDateTime();
        }

        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        if (year > DateConstant.MAX_YEAR || year < DateConstant.MIN_YEAR) {
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TRADE_BILL_DATA_OUT_OF_RANGE);
        }
        String postfix = "_" + year + "_" + (month <= 6 ? "0" : "1");
        return preciseShardingValue.getLogicTableName() + postfix;
    }
}
