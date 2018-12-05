package com.kaihei.esportingplus.payment.config.sharding;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * @author: tangtao
 **/
public class DateRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {

        String tableName = rangeShardingValue.getLogicTableName();

        LocalDateTime lowerTime = new Timestamp(rangeShardingValue.getValueRange().lowerEndpoint().getTime())
                .toLocalDateTime();
        LocalDateTime upperTime = new Timestamp(rangeShardingValue.getValueRange().upperEndpoint().getTime())
                .toLocalDateTime();

        int lowerYear = lowerTime.getYear();
        int lowerMonth = lowerTime.getMonthValue();
        int upperYear = upperTime.getYear();
        int upperMonth = upperTime.getMonthValue();

        Collection<String> tableNames = new LinkedList<>();

        if (lowerYear > DateConstant.MAX_YEAR || upperYear < DateConstant.MIN_YEAR) {
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TRADE_BILL_DATA_OUT_OF_RANGE);
        }

        if (lowerYear < DateConstant.MIN_YEAR) {
            lowerYear = DateConstant.MIN_YEAR;
            lowerMonth = 1;
        }

        if (upperYear > DateConstant.MAX_YEAR) {
            upperYear = DateConstant.MAX_YEAR;
            upperMonth = 12;
        }

        if (lowerYear < upperYear) {
            if (lowerMonth > 6) {
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.LAST_HALF);
                ++lowerYear;
            }
            for (; lowerYear < upperYear; ++lowerYear) {
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.FIRST_HALF);
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.LAST_HALF);
            }
            if (upperMonth > 6) {
                tableNames.add(tableName + "_" + upperYear + "_" + DateConstant.FIRST_HALF);
                tableNames.add(tableName + "_" + upperYear + "_" + DateConstant.LAST_HALF);
            } else {
                tableNames.add(tableName + "_" + upperYear + "_" + DateConstant.FIRST_HALF);
            }
        } else if (lowerYear == upperYear) {
            if (lowerMonth > 6 && upperMonth > 6) {
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.LAST_HALF);
            } else if (lowerMonth <= 6 && upperMonth > 6) {
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.FIRST_HALF);
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.LAST_HALF);
            } else if (lowerMonth <= 6 && upperMonth <= 6) {
                tableNames.add(tableName + "_" + lowerYear + "_" + DateConstant.FIRST_HALF);
            }
        }

        return tableNames;
    }
}
