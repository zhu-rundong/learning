package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.entity.UserEntity;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.param.UserQueryParam;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.vo.UserQueryVo;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 用户表 服务类
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
public interface UserService extends IService<UserEntity> {


    /**
     * 保存 用户表
     *
     * @return
     * @throws Exception
     */
    Object addUser() throws Exception;


    /**
     * 修改 用户表
     *
     * @param userQueryVo
     * @return
     * @throws Exception
     */
    boolean updateUser(UserQueryVo userQueryVo) throws Exception;

    /**
     * 删除 用户表
     *
     * @param ids
     * @return
     * @throws Exception
     */
    boolean deleteUser(String ids) throws Exception;


    /**
     * 根据ID获取 用户表 对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    UserQueryVo getUserById(Serializable id) throws Exception;

    /**
     * 获取 用户表 对象列表
     *
     * @param userQueryParam
     * @return
     * @throws Exception
     */
     List<UserQueryVo> getUserList(UserQueryParam userQueryParam) throws Exception;


}