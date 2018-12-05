package com.kaihei.esportingplus.payment.data.jpa.repository;

import com.kaihei.esportingplus.payment.domain.entity.DeductRatioSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 系统扣除费用比率数据仓库
 *
 * @author haycco
 */
public interface DeductRatioSettingRepository extends JpaRepository<DeductRatioSetting, Long>, JpaSpecificationExecutor<DeductRatioSetting> {

    DeductRatioSetting findByTag(String tag);
}
