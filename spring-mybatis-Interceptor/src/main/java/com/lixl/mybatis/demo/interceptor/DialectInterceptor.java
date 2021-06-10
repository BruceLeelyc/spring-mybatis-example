package com.lixl.mybatis.demo.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DialectInterceptor implements Interceptor {

    /**
     * 数据库方言
     */
    private Dialect dialect;

    /**
     * master sql hint
     */
    private String masterSqlHint = "/*FORCE_MASTER*/";

    /**
     * // 获取原始sql语句
     * MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
     * Object parameter = invocation.getArgs()[1];
     * BoundSql boundSql = mappedStatement.getBoundSql(parameter);
     * String oldsql = boundSql.getSql();
     * log.info("old:"+oldsql);
     * <p>
     * // 改变sql语句
     * BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), oldsql + " where id=1",
     * boundSql.getParameterMappings(), boundSql.getParameterObject());
     * MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
     * invocation.getArgs()[0] = newMs;
     * <p>
     * // 继续执行
     * Object result = invocation.proceed();
     * return result;
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        //当前环境 MappedStatement，BoundSql，及sql取得
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String origSql = boundSql.getSql().trim();
        Object parameterObject = boundSql.getParameterObject();
        int count = -1;

        if (isPagedSql(mappedStatement, parameterObject)) {
            // 如果是分页查询生成分页SQL
            PagedParameter pagedParameter = (PagedParameter) parameterObject;

            // 计算总数
            if (needTotal(mappedStatement)) {
                // 需要查询count
                count = count(invocation, mappedStatement, boundSql, parameterObject);
            }

            // 替换分页参数
            String pagedSql = dialect.getPagedSql(origSql, pagedParameter.getPageIndex(), pagedParameter.getPageSize());
            BoundSql pagedBoundSql = copyFromBoundSql(mappedStatement, boundSql, dbRouterSql(pagedSql));
            MappedStatement pagedMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(pagedBoundSql));
            invocation.getArgs()[0] = pagedMs;
        } else if (needToAddMasterSqlHint(origSql)) {
            BoundSql dbRouterBoundSql = copyFromBoundSql(mappedStatement, boundSql, dbRouterSql(origSql));
            MappedStatement dbRouterMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(dbRouterBoundSql));
            invocation.getArgs()[0] = dbRouterMs;
        }


        Object orgiPagedList = invocation.proceed();
        if (count >= 0) {
            // 计算了COUNT值和总页数TOTAL
            int total = count / ((PagedParameter) parameterObject).getPageSize();
            if ((count % ((PagedParameter) parameterObject).getPageSize()) > 0) {
                total++;
            }

            if (orgiPagedList instanceof PagedResult) {
                ((PagedResult) orgiPagedList).setTotalCount(count);
                ((PagedResult) orgiPagedList).setTotalPages(total);
            } else {
                PagedResult pagedResult = new PagedResult<>();
                pagedResult.setTotalCount(count);
                pagedResult.setTotalPages(total);
                pagedResult.setData((List) orgiPagedList);
                return pagedResult;
            }
        }
        return orgiPagedList;
    }

    private int count(Invocation invocation, MappedStatement mappedStatement,
                      BoundSql boundSql, Object parameterObject) throws Exception {
        Connection connection = getConnection(invocation, mappedStatement.getStatementLog());
        String countSql = getCountSql(boundSql.getSql());
        return count(mappedStatement, boundSql, countSql, connection, parameterObject);
    }

    private int count(MappedStatement mappedStatement, BoundSql boundSql, String countSql, Connection connection, Object parameterObject) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int count = 0;
        try {
            BoundSql countBoundSql = copyFromBoundSql(mappedStatement, boundSql, dbRouterSql(countSql));
            ParameterHandler handler = mappedStatement.getConfiguration().newParameterHandler(mappedStatement, parameterObject, countBoundSql);

            stmt = connection.prepareStatement(countSql);

            handler.setParameters(stmt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return count;
    }

    private Connection getConnection(Invocation invocation, Log statementLog) throws SQLException {
        Executor exe = (Executor) invocation.getTarget();
        Connection connection = exe.getTransaction().getConnection();
        if (statementLog.isDebugEnabled()) {
            return ConnectionLogger.newInstance(connection, statementLog, 0);
        } else {
            return connection;
        }
    }

    private String getCountSql(String orgiSql) {
        return "select count(*) from (" + orgiSql + ") A";
    }

    private boolean needTotal(MappedStatement mappedStatement) {
        String id = mappedStatement.getId();
        String[] idArray = id.split("\\.");
        if (idArray != null && idArray[idArray.length - 1].startsWith("findPage")) {
            return true;
        }
        return false;
    }

    private boolean isPagedSql(MappedStatement mappedStatement, Object parameterObject) {
        if (parameterObject instanceof PagedParameter) {
            PagedParameter pagedParameter = (PagedParameter) parameterObject;

            // 页号和页大小大于零才需要分页
            if (!isNatural(pagedParameter.getPageIndex()) || !isNatural(pagedParameter.getPageSize())) {
                return false;
            }

            List<ResultMap> resultMaps = mappedStatement.getResultMaps();
            if (resultMaps != null && resultMaps.size() > 0 && resultMaps.get(0) != null) {
                ResultMap resultMap = resultMaps.get(0);
                if (!Integer.class.equals(resultMap.getType()) && !Long.class.equals(resultMap.getType()) && !Short.class.equals(resultMap.getType()) && !Byte.class.equals(resultMap.getType())) {
                    // 不是求COUNT的SQL
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isNatural(Integer num) {
        if (null != num && num >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (null != ms.getKeyProperties()) {
            if (ms.getKeyProperties().length > 0) {
                builder.keyProperty(ms.getKeyProperties()[0]);
            }
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private boolean needToAddMasterSqlHint(String sql) {
        /*Role role = DynamicDataSourceContext.getRole();
        if (role != null && Role.WRITABLE.equals(role) && !StringUtils.isEmpty(masterSqlHint) && !StringUtils.contains(sql, masterSqlHint)) {
            return true;
        }*/
        return false;
    }

    private String dbRouterSql(String sql) {
        String dbRouterSql = sql;
        if (needToAddMasterSqlHint(sql)) {
            dbRouterSql = masterSqlHint + " " + sql;
        }

        return dbRouterSql;
    }

    /**
     * 拦截器对应的封装原始对象的方法
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置注册拦截器时设定的属性
     */
    @Override
    public void setProperties(Properties p) {

    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public String getMasterSqlHint() {
        return masterSqlHint;
    }

    public void setMasterSqlHint(String masterSqlHint) {
        this.masterSqlHint = masterSqlHint;
    }

    private class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
