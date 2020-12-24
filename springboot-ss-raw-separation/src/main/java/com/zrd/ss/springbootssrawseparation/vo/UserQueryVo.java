package com.zrd.ss.springbootssrawseparation.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <pre>
 * 用户表 查询结果对象
 * </pre>
 *
 * @author ZRD
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class UserQueryVo implements Serializable {

    private static final long serialVersionUID = -5797092538606221631L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名称（用于登录）
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

}