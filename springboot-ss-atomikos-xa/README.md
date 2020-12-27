## Spring Boot + MyBatis-plus + shardingsphere 整合分布式事务(基于Atomikos  XA)

Spring Boot 2.4.0 + mybatis-plus3.3.2 + shardingsphere5.0.0-alpha 水平分库分表，共拆分2个库，每个库分2张表，模拟分布式事务操作。

### 创建数据库及表

>创建sub_db_0、sub_db_1两个数据库
>
>每个数据库中分t_order_0  ...  t_order_2 共2张表

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
spring.shardingsphere.rules.sharding.tables.t_order.actual-data-nodes=ds-$->{0..1}.t_order_$->{0..1}
# 分库策略
# 用于单分片键的标准分片场景
spring.shardingsphere.rules.sharding.tables.t_order.database-strategy.standard.sharding-column=user_id
spring.shardingsphere.rules.sharding.tables.t_order.database-strategy.standard.sharding-algorithm-name=database-inline
# 分表策略
spring.shardingsphere.rules.sharding.tables.t_order.table-strategy.standard.sharding-column=id
spring.shardingsphere.rules.sharding.tables.t_order.table-strategy.standard.sharding-algorithm-name=table-inline
# 分片算法配置
spring.shardingsphere.rules.sharding.sharding-algorithms.database-inline.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.database-inline.props.algorithm-expression=ds-$->{user_id % 2}
spring.shardingsphere.rules.sharding.sharding-algorithms.table-inline.type=INLINE
spring.shardingsphere.rules.sharding.sharding-algorithms.table-inline.props.algorithm-expression=t_order_$->{id % 2}

#主键生成策略
spring.shardingsphere.rules.sharding.key-generators.snowflake.type=SNOWFLAKE
# 机器标识位，取值范围[0,1024)
spring.shardingsphere.rules.sharding.key-generators.snowflake.props.worker-id=123

~~~

OrderServiceImpl 添加注解

>@Transactional(rollbackFor = Exception.class)
>
>@ShardingTransactionType(TransactionType.XA)

~~~java
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
~~~

测试类

~~~java
    @Test
    void TestAddOrder() {
        orderService.addOrder();
    }
~~~


