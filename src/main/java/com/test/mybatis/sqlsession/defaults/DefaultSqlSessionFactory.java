package com.test.mybatis.sqlsession.defaults;

import com.test.mybatis.cfg.Configuration;
import com.test.mybatis.sqlsession.SqlSession;
import com.test.mybatis.sqlsession.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
