package org.d3.core.session;

import org.d3.core.packet.Packet;

public interface Session {
	
	public void setId(String id);
	
	public String getId();
	
	public String getName();
	
	public void setName(String name);
	
	public void onMessage(Packet pkt);
	
	public void sendMessage(Packet pkt);
	
	public void setLastAccessTime(long time);
	
	public long getLastAccessTime();
	
	public void close();
}
