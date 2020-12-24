## Spring Boot + MyBatis-plus + shardingsphere 实现读写分离

采用Spring Boot 2.4.0 + mybatis-plus3.3.2 + shardingsphere5.0.0-alpha 实现读写分离。

### 环境准备

#### 搭建主从复制环境

搭建主从复制环境（也可以一个mysql服务上创建多个数据库模拟主从）

**主从复制环境参考地址：** https://github.com/zhu-rundong/blog/issues/2

#### 创建数据库及表

>主库创建数据库：ms_test
>
>主库创建数据库表：t_user

~~~sql
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称（用于登录）',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;
~~~

### 核心配置文件

~~~yaml
server:
  port: 8088
spring:
  shardingsphere:
    datasource:
      common:
        driver-class-name: com.mysql.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
        username: repl
        password: 123456
      ds-0:
        jdbc-url: jdbc:mysql://localhost:3316/ms_test?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false
      ds-1:
        jdbc-url: jdbc:mysql://localhost:3317/ms_test?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false
      ds-2:
        jdbc-url: jdbc:mysql://localhost:3318/ms_test?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false
      ds-3:
        jdbc-url: jdbc:mysql://localhost:3319/ms_test?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false
      names: ds-0,ds-1,ds-2,ds-3
    props:
      sql-show: true
    rules:
      replica-query:
        data-sources:
          pr_ds:
            primary-data-source-name: ds-0
            replica-data-source-names: ds-1,ds-2,ds-3
        load-balancers:
          round-robin:
            props:
              workid: 123
            type: ROUND_ROBIN
~~~



### 常见问题

由于shardingsphere5.0版本与之前版本配置方式发生很大的改变，记录一下遇到过问题。

#### 1、Caused by: java.util.NoSuchElementException: No value bound

解决方法：

> 配置 spring.shardingsphere.datasource.common...

~~~yaml
spring.shardingsphere.datasource.common.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.common.password=123456
spring.shardingsphere.datasource.common.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.common.username=repl
~~~

> 配置workid，机器标识位，取值范围[0,1024)

~~~yaml
spring.shardingsphere.rules.replica-query.load-balancers.round-robin.props.workid=123
~~~

2、控制台不打印SQL

解决方法：

>5.x 版本以后，sql.show 参数调整为 sql-show