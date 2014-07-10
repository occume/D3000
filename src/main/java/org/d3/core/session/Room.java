package org.d3.core.session;

import org.d3.core.packet.Packet;

public interface Room {
	
	public void broadcast(Packet pkt);
	
	public void addSession(PlayerSession session);
}
