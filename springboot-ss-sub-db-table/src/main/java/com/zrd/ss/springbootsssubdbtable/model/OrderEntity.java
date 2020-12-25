package com.zrd.ss.springbootsssubdbtable.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <pre>
 * 订单表
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@TableName("t_order")
public class OrderEntity extends Model<OrderEntity> {

    @TableId(value = "id" , type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户地址id
     */
    @TableField(value = "user_adress_id")
    private Long userAdressId;

    /**
     * 物流id
     */
    @TableField(value = "logistics_id")
    private Long logisticsId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Timestamp updateTime;

}
