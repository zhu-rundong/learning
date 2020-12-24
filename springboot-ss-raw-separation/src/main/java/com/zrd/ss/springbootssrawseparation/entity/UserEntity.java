package com.zrd.ss.springbootssrawseparation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 用户表
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@TableName("t_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -6043590011087607359L;
    /**
     * 主键
     */
    @TableId(value = "id" , type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名称（用于登录）
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

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