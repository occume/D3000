package org.d3.core.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.d3.core.packet.Packet;

import com.google.common.collect.Maps;

public abstract class RoomSession extends SessionSupport{
	
	private ChannelGroup group;
	private ConcurrentMap<String, Player> players;
	private ConcurrentMap<Integer, Boolean> seats;
	private static final int ROOM_SIZE = 4;
	private int size = 0;
//	private int guanQia = 0;
	protected int MONSTER_COUNT =20;
	protected AtomicInteger readyCount = new AtomicInteger();
	protected static ScheduledExecutorService scheduledService = 
			Executors.newScheduledThreadPool(4);
	
	public RoomSession(String id, String name){
		super(id, name);
		group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		players = Maps.newConcurrentMap();
		
		seats = Maps.newConcurrentMap();
		for(int i = 1; i <= ROOM_SIZE; i++){
			seats.put(i, false);
		}
	}

	public void onMessage(Packet pkt) {
		
	}

	public void sendMassage(Packet pkt) {
		group.writeAndFlush(pkt);
	}
	
	public boolean joinRoom(PlayerSession session){
		synchronized (this) {
			if(size > ROOM_SIZE){
				return false;
			}
			size++;
		}
		int seat = getFreeSeat();
		Player player = session.getPlayer();
		player.setSeat(seat);
		players.put(session.getId(), player);
		return group.add(session.getChannel());
	}
	
	public void leaveRoom(PlayerSession session){
		size--;
		Player player = session.getPlayer();
		seats.put(player.getSeat(), false);
		group.remove(session);
		players.remove(session.getId());
		onLeaveRoom();
	}
	
	abstract void onLeaveRoom();
	
	private int getFreeSeat(){
		for(int i = 1; i <= seats.size(); i++){
			if(!seats.get(i)){
				seats.put(i, true);
				return i;
			}
		}
		return -1;
	}
	
	public Collection<Player> getPlayers(){
		return players.values();
	}
	
	public void close(){
		group.close();
	}

	protected int getPlayerCount(){
		return group.size();
	}
}
