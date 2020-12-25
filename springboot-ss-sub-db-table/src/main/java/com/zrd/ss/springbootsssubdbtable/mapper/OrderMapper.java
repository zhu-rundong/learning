package com.zrd.ss.springbootsssubdbtable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrd.ss.springbootsssubdbtable.model.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Author ZRD
 * @Date 2020/11/30
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {

}
