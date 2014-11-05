package org.d3.core.mybatis.service;

import java.util.List;

import org.codehaus.jackson.map.util.BeanUtil;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private static Logger LOG = LoggerFactory.getLogger(UserService.class);
  @Autowired
  private UserMapper userMapper;
  
  @Transactional
  public void addUser(User user) {
	  userMapper.addUser(user);
  }
  @Transactional
  public void batchAddUser(List<User> users){
	  userMapper.batchAddUser(users);
  }
  
//  @ReadThroughSingleCache(namespace = "occume", expiration = 3600)
  public User getById(int id) {
	  System.out.println(123);
	  return  userMapper.getById(id);
  }
  
  public User getByName(String name) {
	  return  userMapper.getByName(name);
  }
  
  public boolean auth(User user){
	  System.out.println(user);
	  
	  try{
		  User ret = userMapper.auth(user);
		  boolean result = ret != null;
		  if(result){
			  BeanUtils.copyProperties(ret, user);
		  }
		  return result;
	  }catch(Throwable e){
		  LOG.error("error occu when auth; " + e.getMessage());
	  }
	  return false;
  }
  
  @Transactional
  public void addFriend(int uid1, int uid2, int type){
	  userMapper.addFriend(uid1, uid2, type);
  }
  
  public List<User> getFriendsById(int id){
	  return userMapper.getFriendsById(id);
  }
  
}
