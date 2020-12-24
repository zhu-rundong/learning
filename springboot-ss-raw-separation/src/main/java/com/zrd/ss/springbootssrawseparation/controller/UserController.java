package com.zrd.ss.springbootssrawseparation.controller;

import com.zrd.ss.springbootssrawseparation.param.UserQueryParam;
import com.zrd.ss.springbootssrawseparation.service.UserService;
import com.zrd.ss.springbootssrawseparation.vo.UserQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>
 * 用户表 前端控制器
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;


    /**
     * 添加 用户表 对象
     */
    @GetMapping("/add")
    public String addTUser() throws Exception {
        Object id = userService.addUser();
        return id.toString();
    }

    /**
     * 获取 用户表 对象详情
     */
    @GetMapping("/info/{id}")
    public UserQueryVo getTUser(@PathVariable("id") String id) throws Exception {
        UserQueryVo userQueryVo = userService.getUserById(id);
        return userQueryVo;
    }

    /**
     * 获取 用户表 列表
     */
    @GetMapping("/list")
    public List<UserQueryVo> getTUserList() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
         List<UserQueryVo> paging = userService.getUserList(userQueryParam);
        return paging;
    }

}