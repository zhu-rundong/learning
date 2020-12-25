package com.zrd.ss.springbootsssubdbtable.controller;

import com.zrd.ss.springbootsssubdbtable.model.OrderEntity;
import com.zrd.ss.springbootsssubdbtable.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * 订单表 前端控制器
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 添加 订单表 对象 (测试方便，使用get请求)
     */
    @GetMapping("/add")
    public Long addOrder() throws Exception {
        OrderEntity orderEntity = orderService.addOrder();
        return orderEntity.getId();
    }

    /**
     * 获取 订单表 对象详情
     */
    @GetMapping("/info/{id}")
    public OrderEntity getTOrder(@PathVariable("id") String id) throws Exception {
        return orderService.getOrder(id);
    }



}