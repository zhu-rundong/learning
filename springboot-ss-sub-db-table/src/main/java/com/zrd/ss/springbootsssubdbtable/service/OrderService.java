package com.zrd.ss.springbootsssubdbtable.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zrd.ss.springbootsssubdbtable.model.OrderEntity;


/**
 * <pre>
 * 订单表 服务类
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
public interface OrderService  extends IService<OrderEntity> {

    OrderEntity getOrder(String id);

    OrderEntity addOrder();
}