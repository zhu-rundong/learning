package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.entity.UserEntity;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.param.UserQueryParam;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.vo.UserQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 用户表 Mapper 接口
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据ID获取 用户表 对象
     *
     * @param id
     * @return
     */
    UserQueryVo getUserById(Serializable id);

    /**
     * 获取 用户表 对象列表
     *
     * @param userQueryParam
     * @return
     */
    List<UserQueryVo> getUserList(@Param("param") UserQueryParam userQueryParam);

}