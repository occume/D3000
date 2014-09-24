package org.d3.module;

import org.d3.net.packet.InPacket;
import org.d3.net.session.Session;

public interface Module extends Registry, Registerable {
	
	public static final int LOGIN 	= 1;
	public static final int CHAT 	= 2;
	
	public String getDescription();
	
	public int getType();
	
	public void registerModule();
	
	public void service(Session session, InPacket pkt);
	
}
