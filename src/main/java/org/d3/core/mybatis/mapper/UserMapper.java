package org.d3.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.d3.core.mybatis.domain.User;

public interface UserMapper {

  void addUser(User user);
  
  User getById(int id);

  User getByName(String name);
  
  User auth(User user);
  
  void addFriend(@Param(value="uid1")int uid1, 
		  		 @Param(value="uid2")int uid2, 
		  		 @Param(value="type")int type);
  
  List<User> getFriendsById(@Param(value="id")int id);
  
}
