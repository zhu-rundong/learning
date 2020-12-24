package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.config;

import com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.common.DataSourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 动态数据源上下文配置
 * @Author ZRD
 * @Date 2020/12/19
 */
public class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    private static Lock lock = new ReentrantLock();

    private static int counter = 0;

    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.primary::name);

    /**
     * 所有数据源 key 集合
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * 只读数据源 key 集合
     */
    public static List<Object> secondaryDataSourceKeys = new ArrayList<>();

    /**
     * 切换数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @param key:DataSource Key
     * @return: void
     **/
    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

   /**
    * 使用主数据源
    * @Author: ZRD
    * @Date: 2020/12/19
    * @return: void
    **/
    public static void usePrimaryDataSource() {
        CONTEXT_HOLDER.set(DataSourceKey.primary.name());
    }

    /**
     * 通过轮循方式选择要使用的只读数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: void
     **/
    public static void useSecondaryReadDataSource() {
        lock.lock();
        try {
            int datasourceKeyIndex = counter % secondaryDataSourceKeys.size();
            CONTEXT_HOLDER.set(String.valueOf(secondaryDataSourceKeys.get(datasourceKeyIndex)));
            counter++;
        } catch (Exception e) {
            logger.error("Switch secondary read datasource failed, error message is {}", e.getMessage());
            usePrimaryDataSource();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取当前数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: java.lang.String
     **/
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 设置当前数据源为默认数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: void
     **/
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 判断当前是否存在指定的 key 数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @param key: DataSource Key
     * @return: boolean
     **/
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }

}
