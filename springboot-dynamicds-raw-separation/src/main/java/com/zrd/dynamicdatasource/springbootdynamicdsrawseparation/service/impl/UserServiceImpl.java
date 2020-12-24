package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.entity.UserEntity;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.mapper.UserMapper;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.param.UserQueryParam;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.service.UserService;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.vo.UserQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author ZRD
 * @Date 2020/11/30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public Object addUser() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(System.currentTimeMillis());
        int i = new Random().nextInt(100);
        userEntity.setUserName("ceshi"+i);
        userEntity.setNickName("测试"+i);
        userEntity.setEmail("1@qq.com");
        userEntity.setGender("1");
        userEntity.setPhone("18611112222");
        userEntity.setStatus("1");
        userMapper.insert(userEntity);
        return userEntity.getId();
    }


    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public boolean updateUser(UserQueryVo userQueryVo) throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userQueryVo, userEntity);
        return super.updateById(userEntity);
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public boolean deleteUser(String ids) throws Exception {
        String[] idArray = ids.split(",");
        return super.removeByIds(Arrays.asList(idArray));
    }

    @Override
    public UserQueryVo getUserById(Serializable id) throws Exception {
        UserQueryVo userQueryVo = userMapper.getUserById(id);
        return userQueryVo;
    }

    @Override
    public List<UserQueryVo> getUserList(UserQueryParam userQueryParam) throws Exception {
        List<UserQueryVo> UserQueryVoList = userMapper.getUserList(userQueryParam);
        return UserQueryVoList;
    }


}
