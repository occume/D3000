package org.d3.net.session;

import io.netty.channel.Channel;
import org.d3.D3Context;

public class Sessions {
	
	public static Session newSession(Channel channel){
		Session session = new PlayerSession(channel);
		
		SessionManager sessionManager = (SessionManager) D3Context.getBean("sessionManager");
		sessionManager.putSession(session);
		return session;
	}
	
}
