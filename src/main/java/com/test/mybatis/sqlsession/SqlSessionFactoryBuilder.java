package com.test.mybatis.sqlsession;

import com.test.mybatis.cfg.Configuration;
import com.test.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import com.test.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * 用于创建一个SqlsessionFactory对象
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream){
        Configuration configuration = XMLConfigBuilder.loadConfiguration(inputStream);
        return new DefaultSqlSessionFactory(configuration);
    }
}
