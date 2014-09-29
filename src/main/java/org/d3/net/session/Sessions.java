package org.d3.net.session;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.Channel;

import org.d3.D3Context;
import org.d3.logger.D3Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sessions {
	
	private static AtomicLong sessionCount = new AtomicLong();
	private static Logger LOG = LoggerFactory.getLogger(Sessions.class);
	
	public static Session newSession(Channel channel){
		
		Session session = new PlayerSession(channel);
		sessionCount.incrementAndGet();
		if(LOG.isDebugEnabled()){
			LOG.debug(D3Log.D3_LOG_DEBUG + "sessionCount: " + sessionCount.get());
		}
		
		SessionManager sessionManager = (SessionManager) D3Context.getBean("sessionManager");
		sessionManager.put(session);
		return session;
	}
	
}
