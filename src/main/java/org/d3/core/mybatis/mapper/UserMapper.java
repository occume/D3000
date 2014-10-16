package org.d3.core.mybatis.mapper;

import org.d3.core.mybatis.domain.User;

public interface UserMapper {

  void addUser(User user);
  
  User getById(int id);

  User getByName(String name);
  
  User auth(User user);
  
}
