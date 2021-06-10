package com.lixl.mybatis.demo.interceptor;

public interface Dialect {

    /**
     * 方言名称
     *
     * @return
     */
    String name();

    /**
     * 生成分页SQL
     *
     * @param origSql
     * @param page
     * @param rows
     * @return
     */
    String getPagedSql(String origSql, int page, int rows);

    boolean supportAutoIncrement();

    boolean supportSequence();

    String getNextSequenceSql(String seqName);

    String getCurrSequenceSql(String seqName);
}
