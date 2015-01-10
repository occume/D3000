package org.d3.net.session;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.d3.D3Context;
import org.d3.core.mybatis.domain.User;
import org.d3.module.user.bean.Player;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class SessionManager {
	
	private Map<String, Session> sessions = Maps.newConcurrentMap();
	
	private static SessionManager manager;
	
	public static SessionManager instance(){
		if(manager == null){
			manager = (SessionManager) D3Context.getBean("sessionManager");
		}
		return manager;
	}
	
	public int getChanneCount(){
		return sessions.size();
	}
	
	public Session getById(String id){
		return sessions.get(id);
	}
	
	public Session getByName(String name){
		Session ret = null;
		for(Session s: sessions.values()){
			if(s.getUser().getName().equals(name)){
				ret = s;
				break;
			}
		}
		return ret;
	};
	/**
	 * 随机获取用户
	 * @return
	 */
	public Collection<User> randomUsers(){
		List<String> keys = Lists.newArrayList(sessions.keySet());
		Collections.shuffle(keys);
		List<User> users = Lists.newArrayList();
		
		int limit = sessions.size();
		if(limit >= 10){
			limit = 10;
		}
		
		for(int i = 0; i < limit; i++){
			users.add(sessions.get(keys.get(i)).getUser());
		}
		return users;
	}
	
	public void put(Session session){
		sessions.put(session.getId(), session);
	}
	
	public Session anySession(){
		String key = sessions.keySet().iterator().next();
		return sessions.get(key);
	}
	
	public void removeSession(Session session){
		sessions.remove(session.getId());
	}
	
	/** session 清理 */
	@Scheduled(cron = "0/5 * * * * *")
	protected void startScheduleTask() {
		
//		for(Entry<String, Session> e: sessions.entrySet()){
//			
//			PlayerSession session = (PlayerSession) e.getValue();
//			long lastAccessTime = session.getLastAccessTime();
//			long now = System.currentTimeMillis();
//			
//			if((now - lastAccessTime) > 300000){
//				sessions.remove(e.getKey()).close();
//			}
//			
//		}
		
	}
	
	@Scheduled(cron = "0/1 * * * * *")
	protected void worldBroadcast() {
		
//		World.ALL.writeAndFlush("This is a world broadcast; player: " + World.ALL.size() + "; message: " + World.MSG_COUNT.get());
		
	}
}
