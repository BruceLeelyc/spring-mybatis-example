package com.lixl.mybatis.demo.interceptor;

public class MysqlDialect implements Dialect {

    @Override
    public String name() {
        return "MysqlDialect";
    }

    @Override
    public String getPagedSql(String origSql, int pageIndex, int pageSize) {
        StringBuilder sb = new StringBuilder();
        sb.append(origSql);
        sb.append(" LIMIT ");
        if (pageIndex > 1) {
            sb.append(PagingUtils.getOffset(pageIndex, pageSize));
            sb.append(", ");
        }
        sb.append(pageSize);

        return sb.toString();
    }

    @Override
    public boolean supportAutoIncrement() {
        return true;
    }

    @Override
    public boolean supportSequence() {
        return false;
    }

    @Override
    public String getNextSequenceSql(String seqName) {
        throw new UnsupportedOperationException(name() + " cannot support getNextSequenceSql.");
    }

    @Override
    public String getCurrSequenceSql(String seqName) {
        throw new UnsupportedOperationException(name() + " cannot support getCurrSequenceSql.");
    }

    public static void main(String[] args) {
        MysqlDialect d = new MysqlDialect();
        String origSql = "SELECT * FROM tb";
        String pagedSql = d.getPagedSql(origSql, 1, 10);
        System.out.println(pagedSql);
    }
}
