package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.common.DataSourceKey;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 数据源配置类
 * @Author ZRD
 * @Date 2020/12/19
 */
@Configuration
public class DataSourceConfigurer {

    /**
     * 主数据源
     * @Bean("primary") 注解用于标识默认使用的 DataSource Bean,该注解可用于 primary 或 secondary read DataSource Bean, 但不能用于 dynamicDataSource Bean, 否则会产生循环调用
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: javax.sql.DataSource
     **/
    @Bean("primary")
    @Primary
    @ConfigurationProperties(prefix = "application.server.db.primary")
    public DataSource primary() {
        return DataSourceBuilder.create().build();
    }
    /**
     * 只读数据源 1
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: javax.sql.DataSource
     **/
    @Bean("secondaryRead1")
    @ConfigurationProperties(prefix = "application.server.db.secondary-read1")
    public DataSource secondary1() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 只读数据源 2
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: javax.sql.DataSource
     **/
    @Bean("secondaryRead2")
    @ConfigurationProperties(prefix = "application.server.db.secondary-read2")
    public DataSource secondary2() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 只读数据源 3
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: javax.sql.DataSource
     **/
    @Bean("secondaryRead3")
    @ConfigurationProperties(prefix = "application.server.db.secondary-read3")
    public DataSource secondary3() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 配置动态数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: javax.sql.DataSource
     **/
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.primary.name(), primary());
        dataSourceMap.put(DataSourceKey.secondary_read1.name(), secondary1());
        dataSourceMap.put(DataSourceKey.secondary_read2.name(), secondary2());
        dataSourceMap.put(DataSourceKey.secondary_read3.name(), secondary3());

        //将 Primary 数据源作为默认指定的数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(primary());
        //将 Primary 和 Secondary-read 数据源作为指定的数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        //将数据源的 key 放到数据源上下文的 key 集合中，用于切换时判断数据源是否有效
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());
        //将 Secondary-read 数据源的 key 放在集合中，用于轮循
        DynamicDataSourceContextHolder.secondaryDataSourceKeys.addAll(dataSourceMap.keySet());
        DynamicDataSourceContextHolder.secondaryDataSourceKeys.remove(DataSourceKey.primary.name());
        return dynamicRoutingDataSource;
    }

    /**
     * 配置 SqlSessionFactoryBean
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: org.mybatis.spring.SqlSessionFactoryBean
     **/
    @Bean
    //@ConfigurationProperties(prefix = "mybatis-plus")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        try {
            /**
             * 将 MyBatis 的 mapper 位置设置到 Bean 的属性中，如果没有使用 *.xml 则可以不用该配置，
             * 否则将会产生 Invalid bound statement (not found) 异常
             * 配置文件方式不生效（mybatis-plus.mapper-locations=classpath:mapper/*.xml）
             */
            sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory.setDataSource(dynamicDataSource());
        return sqlSessionFactory;
    }

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: org.springframework.transaction.PlatformTransactionManager
     **/
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
