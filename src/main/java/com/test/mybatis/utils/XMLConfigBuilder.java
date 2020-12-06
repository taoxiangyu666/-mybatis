package com.test.mybatis.utils;


import com.test.mybatis.annotations.Select;
import com.test.mybatis.cfg.Configuration;
import com.test.mybatis.cfg.Mapper;
import com.test.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户解析配置文件
 */
public class XMLConfigBuilder {

    public static Configuration loadConfiguration(InputStream config) {
        try {
            //定义mybatis的配置文件 SqlMapCondfig.xml
            Configuration configuration = new Configuration();
            //使用dom4j解析xml
            //1.创建SAXReader 对象
            SAXReader reader = new SAXReader();
            //根据字节流输入对象获取Document对象
            Document document = reader.read(config);
            //获取根节点
            Element rootElement = document.getRootElement();
            //选择指定节点方式，获取所有property节点
            List<Element> selectNodes = rootElement.selectNodes("//property");
            //遍历节点
            for (Element element : selectNodes) {
                //获取name属性的值
                String name = element.attributeValue("name");
                if ("driver".equals(name)) {
                    //驱动
                    String driver = element.attributeValue("value");
                    configuration.setDriver(driver);
                }
                if ("url".equals(name)) {
                    //连接信息
                    String url = element.attributeValue("value");
                    configuration.setUrl(url);
                }
                if ("username".equals(name)) {
                    //用户名
                    String username = element.attributeValue("value");
                    configuration.setUsername(username);
                }
                if ("password".equals(name)) {
                    //密码
                    String password = element.attributeValue("value");
                    configuration.setPassword(password);
                }
            }

            //去除Mappers中的mapper标签 判断是resoues 还是 class  xml配置/注解方式
            List<Element> mapperElements = rootElement.selectNodes("//mappers/mapper");
            for (Element mapperElement : mapperElements) {
                Attribute attribute = mapperElement.attribute("resource");
                //判断是xml配置方式还是注解方式
                if (attribute != null) {
                        System.out.println("使用xml方式");
                        //取出属性的值 xxx/xxx/xx.xml
                        String mapperPath = attribute.getValue();
                        //把映射配置文件内容取出，封装成map
                        Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
                        //
                        configuration.setMappers(mappers);

                    } else {
                        System.out.println("使用注解方式");
                        String value = mapperElement.attributeValue("class");
                        Map<String, Mapper> mappers = loadMapperAnnotation(value);
                        configuration.setMappers(mappers);
                    }
                }

            return configuration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                config.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 基于xml配置的方式
     *
     * @param mapperPath
     * @return
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) throws IOException {
        InputStream in = null;
        try {
            //返回值
            Map<String, Mapper> mappers = new HashMap<String, Mapper>();
            //根据路径获取字节流对象
            in = Resources.getResourceAsStream(mapperPath);
            //根据字节流对象获取Document对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            //获取根节点
            Element element = document.getRootElement();
            //获取根节点的namespace属性取值
            String namespace = element.attributeValue("namespace");
            List<Element> list = element.selectNodes("//select");
            for (Element element1 : list) {
                //获取id属性值
                String id = element1.attributeValue("id");
                //获取返回值resultType属性值
                String resultType = element1.attributeValue("resultType");
                //获取sql语句
                String sql = element1.getText();
                //创建key 全限定类名+id的值
                String key = namespace + "." + id;
                Mapper mapper = new Mapper();
                mapper.setQueryString(sql);
                mapper.setResultType(resultType);

                //封装参数
                mappers.put(key, mapper);
            }

            return mappers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            in.close();
        }
    }


    /**
     * 基于注解的方式
     *
     * @param mapperPath
     * @return
     * @throws IOException
     */
    private static Map<String, Mapper> loadMapperAnnotation(String mapperPath) throws IOException, ClassNotFoundException {
        //返回值
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        //反射获取字节码对象
        Class daoClass = Class.forName(mapperPath);
        //获取接口的方法数组
        Method[] methods = daoClass.getMethods();
        for (Method method : methods) {
            //取出每一个方法，判断是否有select注解
            boolean flag = method.isAnnotationPresent(Select.class);
            if (flag) {
                //创建Mapper对象
                Mapper mapper = new Mapper();
                //取出注解的value属性值
                Select selectAnno = method.getAnnotation(Select.class);
                String queryString = selectAnno.value();
                mapper.setQueryString(queryString);
                //获取当前方法的返回值，还要求必须带有泛型信息
                Type type = method.getGenericReturnType();//List<User>
                //判断type是不是参数化的类型
                if (type instanceof ParameterizedType) {
                    //强转
                    ParameterizedType ptype = (ParameterizedType) type;
                    //得到参数化类型中的实际类型参数
                    Type[] types = ptype.getActualTypeArguments();
                    //取出第一个
                    Class domainClass = (Class) types[0];
                    //获取domainClass的类名
                    String resultType = domainClass.getName();
                    //给Mapper赋值
                    mapper.setResultType(resultType);
                }
                //组装key的信息
                //获取方法的名称
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + "." + methodName;
                //给map赋值
                mappers.put(key, mapper);
            }
        }
        return mappers;
    }
}