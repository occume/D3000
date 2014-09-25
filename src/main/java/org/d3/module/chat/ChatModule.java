package org.d3.module.chat;

import org.d3.module.BaseModule;
import org.d3.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatModule extends BaseModule{
	
	private static Logger LOG = LoggerFactory.getLogger(ChatModule.class);
	
	public static final int ROOM_LIST = 2;

	public String getDescription() {
		return "Module for Chat";
	}

	public int getType() {
		return Module.CHAT;
	}
	
}
