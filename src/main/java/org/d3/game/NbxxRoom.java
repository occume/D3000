package org.d3.game;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.d3.core.BaseRoom;
import org.d3.core.transfer.Player;
import org.d3.game.bean.Monster;
import org.d3.net.packet.Packet;
import org.d3.net.packet.Packets;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class NbxxRoom extends BaseRoom {
	
	private Set<Monster> monsters;
	private ConcurrentMap<Integer, Boolean> seats;
	private static final int ROOM_SIZE = 4;

	public NbxxRoom(String id, String name) {
		super(id, name);
		monsters = Sets.newHashSet();
		
		seats = Maps.newConcurrentMap();
		for(int i = 1; i <= ROOM_SIZE; i++){
			seats.put(i, false);
		}
	}
	
	private ScheduledFuture future;
	
	public void startGame() {
		sendMassage(Packets.newPacket(Packets.ROOM, Packets.ROOM_START_GAME, null));
		
		future = scheduledService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				Monster m = new Monster();
				monsters.add(m);
				Packet pkt = Packets.newPacket(
						Packets.ROOM,
						Packets.ROOM_MAKE_MONSTER, 
						"ALL", m);
				broadcast(pkt);
			}
		}, 5, 3, TimeUnit.SECONDS);
		
	}
	
	public void stopGame(){
		if(future != null){
			future.cancel(false);
		}
		monsters.clear();
	}
	
	public Monster getMonster(String id){
		for(Monster m: monsters){
			if(id.equals(m.getId())){
				return m;
			}
		}
		return null;
	}

	@Override
	protected int getRoomSize() {
		return ROOM_SIZE;
	}

	@Override
	protected void onLeaveRoom(Player player) {
		seats.put(player.getSeat(), false);
	}

	public int getFreeSeat(){
		for(int i = 1; i <= seats.size(); i++){
			if(!seats.get(i)){
				seats.put(i, true);
				return i;
			}
		}
		return -1;
	}
}
