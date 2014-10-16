package org.d3.module.user;

import org.d3.module.BaseModule;
import org.d3.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserModule extends BaseModule{
	
	private static Logger LOG = LoggerFactory.getLogger(UserModule.class);
	
	public static final int LOOK_UP = 2;
	public static final int ADD_FRIEND = 3;

	public String getDescription() {
		return "Module for User";
	}

	public int getType() {
		return Module.USER;
	}
	
}