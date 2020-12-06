package com.test.mybatis.sqlsession.proxy;

import com.test.mybatis.cfg.Configuration;
import com.test.mybatis.cfg.Mapper;
import com.test.mybatis.utils.Executor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

public class MapperProxy implements InvocationHandler{

  private Map<String,Mapper> mappers;
  private Connection conn;

    public MapperProxy(Map<String, Mapper> mappers, Connection conn) {
        this.mappers = mappers;
        this.conn = conn;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取方法名
        String name = method.getName();
        //获取方法所在的类名
        String classname = method.getDeclaringClass().getName();
        //组合KEY
        String key = classname+"."+name;
        Mapper mapper = mappers.get(key);
        if (mapper==null){
            throw new IllegalArgumentException("传入的参数有误");
        }
        return new Executor().selectList(mapper,conn);
    }
}
