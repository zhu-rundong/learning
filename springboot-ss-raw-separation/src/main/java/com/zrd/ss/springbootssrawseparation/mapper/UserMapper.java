package com.zrd.ss.springbootssrawseparation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrd.ss.springbootssrawseparation.entity.UserEntity;
import com.zrd.ss.springbootssrawseparation.param.UserQueryParam;
import com.zrd.ss.springbootssrawseparation.vo.UserQueryVo;
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