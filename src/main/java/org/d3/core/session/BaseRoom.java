package org.d3.core.session;

import org.d3.core.packet.Packet;

public class BaseRoom extends RoomSession implements Room {
	
	public BaseRoom(String id, String name){
		super(id, name);
	}

	public void broadcast(Packet pkt) {
		sendMassage(pkt);
	}

}
