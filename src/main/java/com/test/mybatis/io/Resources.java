package com.test.mybatis.io;

import java.io.InputStream;

/**
 * 使用类加载器读取配置文件
 */
public class Resources {
    /**
     * 传入一个文件对象，获取一个字节输入流
     * @param filePath
     * @return
     */
    public static InputStream getResourceAsStream(String filePath){
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}
