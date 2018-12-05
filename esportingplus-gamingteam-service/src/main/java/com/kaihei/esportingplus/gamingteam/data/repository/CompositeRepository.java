package com.kaihei.esportingplus.gamingteam.data.repository;

import com.kaihei.esportingplus.common.data.mapper.CommonRepository;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompositeRepository implements CommonRepository {

    @Autowired
    private List<CommonRepository> commonRepositories;

    private CommonRepository findSuportedRepository(Object o) {
        for (CommonRepository commonRepository : commonRepositories) {
            Class<?> genericInterface = (Class<?>) commonRepository.getClass()
                    .getGenericInterfaces()[0];
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface
                    .getGenericInterfaces()[0];

            Class suportedType = (Class) parameterizedType.getActualTypeArguments()[0];
            if (suportedType.isAssignableFrom(o.getClass())) {
                return commonRepository;
            }
        }
        return null;
    }

    @PostConstruct
    private void init() {

    }
    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     */
    @Override
    public int deleteByPrimaryKey(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     */
    @Override
    public int delete(Object record) {
        return 0;
    }

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     */
    @Override
    public int insert(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return 0;
        }
        return suportedRepository.insert(record);
    }

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     */
    @Override
    public int insertSelective(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return 0;
        }
        return suportedRepository.insertSelective(record);
    }

    /**
     * 根据主键字段查询总数，方法参数必须包含完整的主键属性，查询条件使用等号
     */
    @Override
    public boolean existsWithPrimaryKey(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询全部结果
     */
    @Override
    public List selectAll() {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     */
    @Override
    public Object selectByPrimaryKey(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     */
    @Override
    public int selectCount(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return 0;
        }
        return suportedRepository.selectCount(record);
    }

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     */
    @Override
    public List select(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return null;
        }
        return suportedRepository.select(record);
    }

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     */
    @Override
    public Object selectOne(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return null;
        }
        return suportedRepository.selectOne(record);
    }

    /**
     * 根据主键更新实体全部字段，null值会被更新
     */
    @Override
    public int updateByPrimaryKey(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return 0;
        }
        return suportedRepository.updateByPrimaryKey(record);
    }

    /**
     * 根据主键更新属性不为null的值
     */
    @Override
    public int updateByPrimaryKeySelective(Object record) {
        CommonRepository suportedRepository = findSuportedRepository(record);
        if (suportedRepository == null) {
            return 0;
        }
        return suportedRepository.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据Example条件删除数据
     */
    @Override
    public int deleteByExample(Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据Example条件进行查询
     */
    @Override
    public List selectByExample(Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据Example条件进行查询总数
     */
    @Override
    public int selectCountByExample(Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据Example条件进行查询
     */
    @Override
    public Object selectOneByExample(Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     */
    @Override
    public int updateByExample(Object record, Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     */
    @Override
    public int updateByExampleSelective(Object record, Object example) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据example条件和RowBounds进行分页查询
     */
    @Override
    public List selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据实体属性和RowBounds进行分页查询
     */
    @Override
    public List selectByRowBounds(Object record, RowBounds rowBounds) {
        throw new UnsupportedOperationException();
    }
}
