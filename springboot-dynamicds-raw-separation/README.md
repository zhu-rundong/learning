## Spring Boot 和 MyBatis-plus 实现多数据源动态切换 & 读写分离（多数据源轮循）

### 项目描述

通过实现Spring Boot 中 AbstractRoutingDataSource类的抽象方法 determineCurrentLookupKey() ，可以自定义规则选择当前使用的数据源，实现动态切换数据源。

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

### 常见问题

#### 1、org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): 

解决方法

>使用MybatisSqlSessionFactory代替SqlSessionFactory，并配置xml文件路径。
>
>PS：在配置文件配置mybatis-plus.mapper-locations=classpath:mapper/*.xml方式没有解决问题。

~~~java
public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        try {
            /**
             * 将 MyBatis 的 mapper 位置设置到 Bean 的属性中，如果没有使用 *.xml 则可以不用该配置，
             * 否则将会产生 Invalid bound statement (not found) 异常
             * 配置文件方式不生效（mybatis-plus.mapper-locations=classpath:mapper/*.xml）
             */
            sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory.setDataSource(dynamicDataSource());
        return sqlSessionFactory;
    }
~~~



参考项目：https://github.com/helloworlde/SpringBoot-DynamicDataSource



