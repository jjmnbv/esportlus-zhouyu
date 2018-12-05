package com.kaihei.esportingplus.riskrating.service.impl;

import com.kaihei.esportingplus.common.tools.DateUtil;
import com.kaihei.esportingplus.common.tools.ObjectTools;
import com.kaihei.esportingplus.riskrating.api.enums.FreeTeamEnum;
import com.kaihei.esportingplus.riskrating.api.vo.FreeTeamResponse;
import com.kaihei.esportingplus.riskrating.api.vo.ImMachineListVo;
import com.kaihei.esportingplus.riskrating.api.vo.PageInfo;
import com.kaihei.esportingplus.riskrating.api.vo.TransferBlackListVo;
import com.kaihei.esportingplus.riskrating.domain.entity.ImMachineDeviceBlack;
import com.kaihei.esportingplus.riskrating.domain.entity.TransferBlackRecord;
import com.kaihei.esportingplus.riskrating.repository.TransferBlackRecordRepository;
import com.kaihei.esportingplus.riskrating.service.TransferBlackService;
import com.kaihei.esportingplus.riskrating.util.PageRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransferBlackServiceImpl implements TransferBlackService {

    private static final Logger logger = LoggerFactory.getLogger(TransferBlackServiceImpl.class);

    @Autowired
    private TransferBlackRecordRepository transferBlackRecordRepository;

    @Override
    public PageInfo<TransferBlackListVo> getTransferBlackList(String deviceId, String page, String size) {
        PageInfo<TransferBlackListVo> listVo = new PageInfo<>();

        PageRequest pageRequest = PageRequestUtil.getPageRequest(page, size);

        Specification<TransferBlackRecord> specification = this.getTransferBlackSpecipicationInfo(deviceId);
        //查询列表
        Page<TransferBlackRecord> respPage = transferBlackRecordRepository.findAll(specification, pageRequest);
        long total = respPage.getTotalElements();
        listVo.setTotal(total);

        List<TransferBlackListVo> voList = new ArrayList<TransferBlackListVo>();
        if (total > 0) {
            List<TransferBlackRecord> blackList = respPage.getContent();
            logger.debug("getTransferBlackList >> page ：{} ", blackList);
            for (TransferBlackRecord black : blackList) {
                TransferBlackListVo vo = new TransferBlackListVo();
                vo.setId(black.getId());
                vo.setUserId(black.getUserId());
                vo.setRemark(black.getRemark());
                vo.setCreateDate(DateUtil.fromDate2Str(black.getCreateDate()));

                voList.add(vo);
            }
        }
        listVo.setList(voList);

        return listVo;
    }

    private Specification<TransferBlackRecord> getTransferBlackSpecipicationInfo(String userId) {

        Specification<TransferBlackRecord> querySpecifiction = new Specification<TransferBlackRecord>() {
            @Override
            public Predicate toPredicate(Root<TransferBlackRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (ObjectTools.isNotEmpty(userId)) {
                    predicates.add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return querySpecifiction;
    }

    @Override
    public void insertTransferBlack(String userId, String remark) {
        TransferBlackRecord transferBlackRecord = new TransferBlackRecord();
        transferBlackRecord.setUserId(userId);
        transferBlackRecord.setRemark(remark);
        transferBlackRecordRepository.save(transferBlackRecord);
    }

    @Override
    public void deleteTransferBlack(String ids) {
        List<TransferBlackRecord> recordList = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            List<String> strsToList1= Arrays.asList(idArray);
            for (String idStr : strsToList1) {
                Long id = Long.valueOf(idStr);
                TransferBlackRecord record = new TransferBlackRecord();
                record.setId(id);
                recordList.add(record);
            }
            transferBlackRecordRepository.delete(recordList);
        }
    }

    @Override
    public FreeTeamResponse checkTransfer(String userId) {
        FreeTeamResponse response = new FreeTeamResponse(FreeTeamEnum.SUCCESS.getCode(), FreeTeamEnum.SUCCESS.getMsg());

        TransferBlackRecord record = transferBlackRecordRepository.findOneByUserId(userId);
        if (null != record) {
            response.setRiskCode(FreeTeamEnum.TRANSFER_LIMIT.getCode());
            response.setRiskMsg(FreeTeamEnum.TRANSFER_LIMIT.getMsg());
            return response;
        }

        return response;
    }
}
