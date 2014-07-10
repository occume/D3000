package org.d3.core.session;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SessionManager {
	
	private Multimap<String, Session> sessions = ArrayListMultimap.create();
	
	private static SessionManager manager = new SessionManager();
	
	private SessionManager(){};
	
	public static SessionManager getInstance(){
		return manager;
	}
	
	public Session getSession(String id){
		return sessions.get(id).iterator().next();
	}
	
	public void putSession(Session session){
		sessions.put(session.getId(), session);
	}
	
	public Session anySession(){
		String key = sessions.keySet().iterator().next();
		return sessions.get(key).iterator().next();
	}
}
