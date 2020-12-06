package com.test.mybatisTest;


import com.test.dao.UserDao;
import com.test.domain.User;
import com.test.mybatis.io.Resources;
import com.test.mybatis.sqlsession.SqlSession;
import com.test.mybatis.sqlsession.SqlSessionFactory;
import com.test.mybatis.sqlsession.SqlSessionFactoryBuilder;

import org.junit.Test;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
//    @Test
//    public static void main(String[] args) throws IOException {
//        //1.读取配置文件
//        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
//        //2.创建sqlsessionFactory 的构建者对象
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        //3.创建sqlsessionFactory 对象
//        SqlSessionFactory sessionFactory = builder.build(is);
//        //4.创建sqlsession 对象
//        SqlSession sqlSession = sessionFactory.openSession();
//        //5.使用sqlsessioin对象创建dao接口的代理对象
//        UserDao mapper = sqlSession.getMapper(UserDao.class);
//        //6.使用代理对象执行查询方法
//        List<User> all = mapper.findAll();
//        for (User user : all) {
//            System.out.println(user);
//        }
//
//        //释放资源
//        sqlSession.close();
//        is.close();
//    }

    @Test
    public  void test01() throws IOException {
        //1.读取配置文件
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建sqlsessionFactory 的构建者对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //3.创建sqlsessionFactory 对象
        SqlSessionFactory sessionFactory = builder.build(is);
        //4.创建sqlsession 对象
        SqlSession sqlSession = sessionFactory.openSession();
        //5.使用sqlsessioin对象创建dao接口的代理对象
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        //6.使用代理对象执行查询方法
        List<User> all = mapper.findAll();
        for (User user : all) {
            System.out.println(user);
        }

        //释放资源
        sqlSession.close();
        is.close();
    }
}
