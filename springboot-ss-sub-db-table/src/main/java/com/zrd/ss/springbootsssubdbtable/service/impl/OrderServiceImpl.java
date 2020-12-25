package com.zrd.ss.springbootsssubdbtable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.ss.springbootsssubdbtable.mapper.OrderMapper;
import com.zrd.ss.springbootsssubdbtable.model.OrderEntity;
import com.zrd.ss.springbootsssubdbtable.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @Description
 * @Author ZRD
 * @Date 2020/11/30
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    @Resource
    private OrderMapper orderMapper;


    @Override
    public OrderEntity getOrder(String id) {
        return orderMapper.selectById(Long.parseLong(id));
    }

    @Override
    public OrderEntity addOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(1L);
        orderEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        orderMapper.insert(orderEntity);
        return orderEntity;
    }
}
