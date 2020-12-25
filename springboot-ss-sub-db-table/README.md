## Spring Boot + MyBatis-plus + shardingsphere 实现水平分库分表

采用Spring Boot 2.4.0 + mybatis-plus3.3.2 + shardingsphere5.0.0-alpha 实现水平分库分表，共拆分2个库，每个库分16张表

### 创建数据库及表

>创建sub_db_0、sub_db_1两个数据库
>
>每个数据库中分t_order_0  ...  t_order_15 共16张表

~~~sql
CREATE TABLE `t_order_0`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_adress_id` bigint(20) NULL DEFAULT NULL COMMENT '用户地址id',
  `logistics_id` bigint(20) NULL DEFAULT NULL COMMENT '物流id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;
~~~

### 核心配置文件

~~~properties
#mybatis-plus
mybatis-plus.mapper-locations=classpath:mapper/*.xml
# 打印sql
spring.shardingsphere.props.sql-show=true
# 配置数据源名称
spring.shardingsphere.datasource.names=ds-0,ds-1
# 配置数据源
spring.shardingsphere.datasource.common.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.common.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.common.username=root
spring.shardingsphere.datasource.common.password=admin
spring.shardingsphere.datasource.ds-0.jdbc-url=jdbc:mysql://localhost:3308/sub_db_0?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false
spring.shardingsphere.datasource.ds-1.jdbc-url=jdbc:mysql://localhost:3308/sub_db_1?serverTimezone=UTC&characterEncoding=utf-8&useSSL=false

# 标准分片表配置
spring.shardingsphere.rules.sharding.tables.t_order.actual-data-nodes=ds-$->{0..1}.t_order_$->{0..15}
# 分库策略
# 用于单分片键的标准分片场景
spring.shardingsphere.rules.sharding.tables.t_order.database-strategy.standard.sharding-column=id
spring.shardingsphere.rules.sharding.tables.t_order.database-strategy.standard.sharding-algorithm-name=database-inline
# 分表策略
spring.shardingsphere.rules.sharding.tables.t_order.table-strategy.standard.sharding-column=id
spring.shardingsphere.rules.sharding.tables.t_order.table-strategy.standard.sharding-algorithm-name=table-inline
# 分片算法配置
spring.shardingsphere.rules.sharding.sharding-algorithms.database-inline.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.database-inline.props.algorithm-expression=ds-$->{id % 2}
spring.shardingsphere.rules.sharding.sharding-algorithms.table-inline.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.table-inline.props.algorithm-expression=t_order_$->{id % 15}

#主键生成策略
spring.shardingsphere.rules.sharding.key-generators.snowflake.type=SNOWFLAKE
# 机器标识位，取值范围[0,1024)
spring.shardingsphere.rules.sharding.key-generators.snowflake.props.worker-id=123
# 最大抖动上限值，范围[0, 4096)。注：若使用此算法生成值作分片值，建议配置此属性。此算法在不同毫秒内所生成的 key 取模 2^n (2^n一般为分库或分表数) 之后结果总为 0 或 1。为防止上述分片问题，建议将此属性值配置为 (2^n)-1
# 5.0版本没生效，取模结果一直是0和1，暂时将t_order_$->{id % 16}改成了 t_order_$->{id % 15}
#spring.shardingsphere.rules.sharding.key-generators.snowflake.props.max-vibration-offset=15

~~~

### 常见问题

#### 1、分片取模结果总为 0 或 1

解决方法：

~~~properties
# 配置为 (2^n)-1
spring.shardingsphere.rules.sharding.sharding-algorithms.table-inline.props.algorithm-expression=t_order_$->{id % 15}

# 最大抖动上限值，范围[0, 4096)。注：若使用此算法生成值作分片值，建议配置此属性。此算法在不同毫秒内所生成的 key 取模 2^n (2^n一般为分库或分表数) 之后结果总为 0 或 1。为防止上述分片问题，建议将此属性值配置为 (2^n)-1
# 5.0版本没生效，取模结果一直是0和1，暂时将t_order_$->{id % 16}改成了 t_order_$->{id % 15}
#spring.shardingsphere.rules.sharding.key-generators.snowflake.props.max-vibration-offset=15
~~~