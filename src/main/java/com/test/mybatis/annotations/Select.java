package com.test.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//作用域
@Target(ElementType.METHOD)//作用于方法
public @interface Select {
    /**
     * 配置的sql语句
     * @return
     */
    String value();
}
