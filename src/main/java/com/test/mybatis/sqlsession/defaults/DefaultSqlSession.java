package com.test.mybatis.sqlsession.defaults;

import com.test.mybatis.cfg.Configuration;
import com.test.mybatis.cfg.Mapper;
import com.test.mybatis.sqlsession.SqlSession;
import com.test.mybatis.sqlsession.proxy.MapperProxy;
import com.test.mybatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class DefaultSqlSession implements SqlSession{
    private Configuration configuration;
    private Connection conn;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        conn = DataSourceUtil.getConnection(configuration);
    }

    public <T> T getMapper(Class<T> daoInterfaceClass) {
      return (T) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),
               new Class[]{daoInterfaceClass},new MapperProxy(configuration.getMappers(),conn));
    }

    public void close() {
        if(conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
