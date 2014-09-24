package org.d3.net.session;

import org.d3.module.user.bean.Player;
import org.d3.net.packet.Packet;

public interface Session {
	
	public void setId(String id);
	
	public String getId();
	
	public String getName();
	
	public void setName(String name);
	
	public void onMessage(Object pkt);
	
	public void sendMessage(Object pkt);
	
	public void setLastAccessTime(long time);
	
	public long getLastAccessTime();
	
	public void setPlayer(Player player);
	
	public Player getPlayer();
	
	public void close();
}
