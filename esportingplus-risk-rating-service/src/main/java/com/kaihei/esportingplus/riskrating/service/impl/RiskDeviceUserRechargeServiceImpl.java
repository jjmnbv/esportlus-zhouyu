package com.kaihei.esportingplus.riskrating.service.impl;

import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceUserLogQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteQueryParams;
import com.kaihei.esportingplus.riskrating.api.params.RiskDeviceWhiteUpdateParams;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceUserRechargeLogVo;
import com.kaihei.esportingplus.riskrating.api.vo.RiskDeviceWhiteVo;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceUserRechargeLog;
import com.kaihei.esportingplus.riskrating.domain.entity.RiskDeviceWhite;
import com.kaihei.esportingplus.riskrating.repository.RiskDeviceUserRechargeLogRepository;
import com.kaihei.esportingplus.riskrating.repository.RiskDeviceWhiteRepository;
import com.kaihei.esportingplus.riskrating.service.RiskDeviceUserRechargeService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiskDeviceUserRechargeServiceImpl implements RiskDeviceUserRechargeService {

    @Autowired
    private RiskDeviceUserRechargeLogRepository riskDeviceUserRechargeLogRepository;
    @Autowired
    private RiskDeviceWhiteRepository riskDeviceWhiteRepository;
    @Override
    public Page<RiskDeviceUserRechargeLogVo> findRiskDeviceRechargeLogByPage(
            RiskDeviceUserLogQueryParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,params);
        Pageable pageRequest = new PageRequest(params.getOffset() - 1, params.getLimit());
        Specification<RiskDeviceUserRechargeLog> spec = (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            if (ObjectTools.isNotNull(params.getIsWhite())) {
                Path<Integer> isWhite = root.get("isWhite");
                Predicate isWhiteEqual = builder.equal(isWhite, params.getIsWhite());
                predicates.add(isWhiteEqual);
            }
            if (ObjectTools.isNotEmpty(params.getDeviceNo())) {
                Path<String> name = root.get("deviceNo");
                Predicate deviceNoEqual = builder.equal(name, params.getDeviceNo());
                predicates.add(deviceNoEqual);
            }
            if (ObjectTools.isNotNull(params.getStartDate())) {
                Path<Date> name = root.get("createDate");
                Predicate startDatePredicate = builder
                        .greaterThanOrEqualTo(name, params.getStartDate());
                predicates.add(startDatePredicate);
            }
            if (ObjectTools.isNotNull(params.getEndDate())) {
                Path<Date> name = root.get("createDate");
                Predicate endDatePredicate = builder
                        .lessThan(name, params.getEndDate());
                predicates.add(endDatePredicate);
            }
            query.orderBy(builder.desc(root.get("id")));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<RiskDeviceUserRechargeLog> page = riskDeviceUserRechargeLogRepository
                .findAll(spec, pageRequest);

        PageImpl<RiskDeviceUserRechargeLogVo> pageVo = new PageImpl<RiskDeviceUserRechargeLogVo>(
                convertToRiskDeviceLogVoList(page.getContent()), pageRequest,
                page.getTotalElements());
        return pageVo;
    }

    @Override
    public Page<RiskDeviceWhiteVo> findRiskDeviceBindByPage(RiskDeviceWhiteQueryParams params) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,params);
        Pageable pageRequest = new PageRequest(params.getOffset() - 1, params.getLimit());
        Specification<RiskDeviceWhite> spec = (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            if (ObjectTools.isNotEmpty(params.getDeviceNo())) {
                Path<String> name = root.get("deviceNo");
                Predicate deviceNoEqual = builder.equal(name, params.getDeviceNo());
                predicates.add(deviceNoEqual);
            }
            if (ObjectTools.isNotNull(params.getStartDate())) {
                Path<Date> name = root.get("createDate");
                Predicate startDatePredicate = builder
                        .greaterThanOrEqualTo(name, params.getStartDate());
                predicates.add(startDatePredicate);
            }
            if (ObjectTools.isNotNull(params.getEndDate())) {
                Path<Date> name = root.get("createDate");
                Predicate endDatePredicate = builder
                        .lessThan(name, params.getEndDate());
                predicates.add(endDatePredicate);
            }
            query.orderBy(builder.desc(root.get("id")));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<RiskDeviceWhite> page = riskDeviceWhiteRepository.findAll(spec, pageRequest);
        PageImpl<RiskDeviceWhiteVo> pageVo = new PageImpl<RiskDeviceWhiteVo>(
                convertToRiskDeviceBindVoList(page.getContent()), pageRequest,
                page.getTotalElements());
        return pageVo;
    }
    @Override
    public RiskDeviceWhiteVo findRiskDeviceWhiteVoById(Long id){
        RiskDeviceWhite riskDeviceWhite = riskDeviceWhiteRepository.findOne(id);
        if(riskDeviceWhite!=null){
            return BeanMapper.map(riskDeviceWhite,RiskDeviceWhiteVo.class);
        }
        return new RiskDeviceWhiteVo();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRiskDeviceWhite(RiskDeviceWhiteUpdateParams params){
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,params);
        RiskDeviceWhite riskDeviceWhite = riskDeviceWhiteRepository.findByDeviceNo(params.getDeviceNo());
        ValidateAssert.allNull(BizExceptionEnum.FREE_TEAM_DEVICE_WHITE_ERROR,riskDeviceWhite);
        riskDeviceWhite = BeanMapper.map(params, RiskDeviceWhite.class);
        riskDeviceWhiteRepository.save(riskDeviceWhite);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRiskDeviceWhite(RiskDeviceWhiteUpdateParams params){
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL,params);
        ValidateAssert.hasNotNull(BizExceptionEnum.PARAM_VALID_FAIL,params.getId());
        RiskDeviceWhite riskDeviceWhite = riskDeviceWhiteRepository.findOne(params.getId());
        ValidateAssert.hasNotNull(BizExceptionEnum.FREE_TEAM_DEVICE_WHITE_NOT_EXIST,riskDeviceWhite);
        //比较设备号，如果不相同，则查询是否此号已被添加，如果添加，拒绝修改
        if(!riskDeviceWhite.getDeviceNo().equals(params.getDeviceNo())){
            ValidateAssert.allNull(BizExceptionEnum.FREE_TEAM_DEVICE_WHITE_ERROR,riskDeviceWhiteRepository.findByDeviceNo(params.getDeviceNo()));
        }
        riskDeviceWhite.setDeviceNo(params.getDeviceNo());
        riskDeviceWhite.setDeviceDesc(params.getDeviceDesc());
        riskDeviceWhite.setWhiteStatus(params.getWhiteStatus());
        riskDeviceWhiteRepository.save(riskDeviceWhite);
    }
    private List<RiskDeviceWhiteVo> convertToRiskDeviceBindVoList(List<RiskDeviceWhite> contents) {
        return contents.stream().map(it -> BeanMapper.map(it, RiskDeviceWhiteVo.class))
                .collect(
                        Collectors.toList());
    }

    private List<RiskDeviceUserRechargeLogVo> convertToRiskDeviceLogVoList(
            List<RiskDeviceUserRechargeLog> contents) {
        return contents.stream().map(it -> BeanMapper.map(it, RiskDeviceUserRechargeLogVo.class))
                .collect(
                        Collectors.toList());
    }
}
