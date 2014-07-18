package org.d3.core.session;

import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;

public class BaseRoom extends RoomSession implements Room {
	
	public BaseRoom(String id, String name){
		super(id, name);
	}

	public void broadcast(Packet pkt) {
		sendMassage(pkt);
	}

	public void startGame() {
		sendMassage(Packets.newPacket(Packets.START, null));
	}

	public void playerPrepare() {
		int currReadyCount = readyCount.incrementAndGet();
		System.out.println(currReadyCount);
		System.out.println(getPlayerCount());
		synchronized (this) {
			if(currReadyCount == getPlayerCount()){
				startGame();
			}
		}
	}

	public void playerUnPrepare() {
		readyCount.decrementAndGet();
	}

}
