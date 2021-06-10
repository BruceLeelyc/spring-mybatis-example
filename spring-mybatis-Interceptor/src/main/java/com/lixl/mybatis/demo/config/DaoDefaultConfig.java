package com.lixl.mybatis.demo.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.lixl.mybatis.demo.interceptor.DialectInterceptor;
import com.lixl.mybatis.demo.interceptor.MysqlDialect;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DaoDefaultConfig
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 19:03
 */
@Configuration
public class DaoDefaultConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

//    @Value("${spring.datasource.initialSize}")
//    private int initialSize;
//
//    @Value("${spring.datasource.minIdle}")
//    private int minIdle;
//
//    @Value("${spring.datasource.maxActive}")
//    private int maxActive;
//
//    @Value("${spring.datasource.maxWait}")
//    private int maxWait;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.connection-init-sqls}")
//    private List<String> connectionInitSqls;


    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());

        //添加插件
        DialectInterceptor interceptor = new DialectInterceptor();
        interceptor.setDialect(new MysqlDialect());
        bean.setPlugins(new Interceptor[]{interceptor});

        // 配置映射文件目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setConfigLocation(resolver.getResource("classpath:mybatis.xml"));

            Resource[] mybatisRootMapperXml = resolver.getResources("classpath*:*Mapper.xml");
            Resource[] mybatisMapperXml = resolver.getResources("classpath*:/**/*Mapper.xml");

            bean.setMapperLocations(ArrayUtils.addAll(mybatisMapperXml, mybatisRootMapperXml));
            return bean.getObject();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);

        /*datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);

        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setValidationQuery(validationQuery);

        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setConnectionInitSqls(connectionInitSqls);*/

        // sql监控
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);

        return datasource;
    }

    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        // slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setLogSlowSql(true);
        // SQL合并配置
        statFilter.setMergeSql(true);
        // slowSqlMillis的缺省值为3000，也就是3秒。
        statFilter.setSlowSqlMillis(1000);
        return statFilter;
    }

    /**
     * sql防火墙
     * <p>
     * selectWhereAlwayTrueCheck true 检查SELECT语句的WHERE子句是否是一个永真条件
     * <p>
     * selectHavingAlwayTrueCheck true 检查SELECT语句的HAVING子句是否是一个永真条件
     * <p>
     * deleteWhereAlwayTrueCheck true 检查DELETE语句的WHERE子句是否是一个永真条件
     * <p>
     * deleteWhereNoneCheck false 检查DELETE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
     * <p>
     * updateWhereAlayTrueCheck true 检查UPDATE语句的WHERE子句是否是一个永真条件
     * <p>
     * updateWhereNoneCheck false 检查UPDATE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
     * <p>
     * conditionAndAlwayTrueAllow false 检查查询条件(WHERE/HAVING子句)中是否包含AND永真条件
     * <p>
     * conditionAndAlwayFalseAllow false 检查查询条件(WHERE/HAVING子句)中是否包含AND永假条件
     * <p>
     * conditionLikeTrueAllow true 检查查询条件(WHERE/HAVING子句)中是否包含LIKE永真条件
     *
     * @return
     */
    @Bean
    public WallFilter wallFilter() {

        WallFilter wallFilter = new WallFilter();
        // 对被认为是攻击的SQL进行LOG.error输出
        wallFilter.setLogViolation(true);
        // 对被认为是攻击的SQL抛出SQLExcepton
        wallFilter.setThrowException(true);

        WallConfig config = new WallConfig();
        // 允许执行多条SQL
        config.setMultiStatementAllow(true);
        // 不允许无where删除语句执行
        config.setDeleteWhereNoneCheck(true);
        // 不允许无where更新语句执行
        config.setUpdateWhereNoneCheck(true);

        wallFilter.setConfig(config);
        return wallFilter;
    }
}
