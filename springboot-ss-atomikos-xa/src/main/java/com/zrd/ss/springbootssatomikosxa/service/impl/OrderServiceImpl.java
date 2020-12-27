package com.zrd.ss.springbootssatomikosxa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrd.ss.springbootssatomikosxa.mapper.OrderMapper;
import com.zrd.ss.springbootssatomikosxa.model.OrderEntity;
import com.zrd.ss.springbootssatomikosxa.service.OrderService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @Description
 * @Author ZRD
 * @Date 2020/11/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ShardingTransactionType(TransactionType.XA)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public OrderEntity getOrder(String id) {
        return orderMapper.selectById(Long.parseLong(id));
    }

    @Override
    public OrderEntity addOrder() {
        for(int i = 0 ; i < 10 ; i++){
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserId(System.currentTimeMillis());
            orderEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            orderMapper.insert(orderEntity);
            if(i == 4 ){
                //i == 4 , 发生异常
                int ex  = 1 /0 ;
            }
        }
        return new OrderEntity();
    }
}
