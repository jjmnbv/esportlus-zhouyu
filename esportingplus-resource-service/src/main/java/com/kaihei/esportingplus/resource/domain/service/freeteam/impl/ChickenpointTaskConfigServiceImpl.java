package com.kaihei.esportingplus.resource.domain.service.freeteam.impl;

import com.kaihei.esportingplus.api.vo.freeteam.ChickenpointTaskConfigVo;
import com.kaihei.esportingplus.common.BeanMapper;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.resource.data.repository.ChickenpointTaskConfigRepository;
import com.kaihei.esportingplus.resource.domain.entity.ChickenpointTaskConfig;
import com.kaihei.esportingplus.resource.domain.service.freeteam.ChickenpointTaskConfigService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChickenpointTaskConfigServiceImpl implements ChickenpointTaskConfigService {

    @Autowired
    private ChickenpointTaskConfigRepository chickenpointTaskConfigRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ChickenpointTaskConfigVo vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, vo);
        ChickenpointTaskConfig chickenpointTaskConfig = BeanMapper
                .map(vo, ChickenpointTaskConfig.class);
        chickenpointTaskConfigRepository.insertSelective(chickenpointTaskConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChickenpointTaskConfigVo vo) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, vo);
        ChickenpointTaskConfig chickenpointTaskConfig = chickenpointTaskConfigRepository
                .selectByPrimaryKey(vo.getTaskId());
        ValidateAssert
                .hasNotNull(BizExceptionEnum.CHICKENPOINT_CONFIG_NOT_EXIST, chickenpointTaskConfig);
        BeanUtils.copyProperties(vo, chickenpointTaskConfig);
        chickenpointTaskConfig.setId(vo.getTaskId());
        chickenpointTaskConfigRepository.updateByPrimaryKeySelective(chickenpointTaskConfig);
    }

    @Override
    public List<ChickenpointTaskConfigVo> findChickpointTaskConfig() {
        return chickenpointTaskConfigRepository.selectAll().stream()
                .map(it -> {
                    ChickenpointTaskConfigVo taskConfigVo = BeanMapper
                            .map(it, ChickenpointTaskConfigVo.class);
                    taskConfigVo.setTaskId(it.getId());
                    return taskConfigVo;
                }).collect(
                        Collectors.toList());
    }

    @Override
    public List<ChickenpointTaskConfigVo> findEfficientChickpointTaskConfig() {
        return chickenpointTaskConfigRepository.selectEfficientChickpointTaskConfig(new Date())
                .stream().map(it -> {
                    ChickenpointTaskConfigVo taskConfigVo = BeanMapper
                            .map(it, ChickenpointTaskConfigVo.class);
                    taskConfigVo.setTaskId(it.getId());
                    return taskConfigVo;
                }).collect(
                        Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        ValidateAssert.allNotNull(BizExceptionEnum.PARAM_VALID_FAIL, id);
        chickenpointTaskConfigRepository.deleteByPrimaryKey(id);
    }
}
