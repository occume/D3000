package org.d3.core.session;

import java.util.Map;
import java.util.Map.Entry;

import org.d3.D3Context;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class SessionManager {
	
	private Map<String, Session> sessions = Maps.newConcurrentMap();
	
	private static SessionManager manager;
//	
//	private SessionManager(){};
//	
	public static SessionManager getInstance(){
		if(manager == null){
			manager = (SessionManager) D3Context.getBean("sessionManager");
		}
		return manager;
	}
	
	public Session getSession(String id){
		return sessions.get(id);
	}
	
	public void putSession(Session session){
		sessions.put(session.getId(), session);
	}
	
	public Session anySession(){
		String key = sessions.keySet().iterator().next();
		return sessions.get(key);
	}
	
	/** session 清理 */
	 @Scheduled(cron = "0/5 * * * * *")  
	protected void startScheduleTask() {
		 
		for(Entry<String, Session> e: sessions.entrySet()){
			
			PlayerSession session = (PlayerSession) e.getValue();
			long lastAccessTime = session.getLastAccessTime();
			long now = System.currentTimeMillis();
			
			if((now - lastAccessTime) > 300000){
				sessions.remove(e.getKey()).close();
			}
			
		}
		
	}
}
