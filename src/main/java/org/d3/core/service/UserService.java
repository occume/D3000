package org.d3.core.service;

import java.util.Collection;
import org.d3.core.mybatis.domain.User;

public interface UserService {
	
	public Collection<User> randomUser();
	
	public User getUserByName(String name);
	
}
