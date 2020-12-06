package com.test.dao;

import com.test.domain.User;
import com.test.mybatis.annotations.Select;

import java.util.List;

public interface UserDao {

  @Select("select * from user")
    List<User> findAll();
}
