package org.d3.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.d3.Room;
import org.d3.core.transfer.Charactor;
import org.d3.game.bean.Player;
import org.d3.net.packet.Packet;
import org.d3.net.packet.Packets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class BaseRoom  implements Room {
	
	private String id;
	private String name;
	NonBlockingReactor _reactor;
	RequestBus<Packet> requestBus;
	
	public BaseRoom(String id, String name){
		this.id = id;
		this.name = name;
		players = Maps.newConcurrentMap();
		
		try {
			_reactor = new NonBlockingReactor();
			requestBus = new RequestBus<Packet>(_reactor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcast(Packet pkt) {
		sendMassage(pkt);
	}

	public void playerPrepare() {
		int currReadyCount = readyCount.incrementAndGet();
		System.out.println("ready-----------");
		System.out.println(currReadyCount);
		System.out.println(getPlayerCount());
		synchronized (this) {
			if(currReadyCount == getPlayerCount()){
				startGame();
			}
		}
	}

	public void playerUnPrepare() {
		
		int currReadyCount = readyCount.decrementAndGet();
		System.out.println("unready-----------");
		System.out.println(currReadyCount);
		System.out.println(getPlayerCount());
		synchronized (this) {
			if(currReadyCount == 0){
				stopGame();
			}
		}
		
	}
	
	private ConcurrentMap<String, Charactor> players;
	
	private int size = 0;
//	private int guanQia = 0;
	protected int MONSTER_COUNT =20;
	protected AtomicInteger readyCount = new AtomicInteger();
	protected static ScheduledExecutorService scheduledService = 
			Executors.newScheduledThreadPool(4);
	

	public void sendMassage(Packet pkt) {
//		group.writeAndFlush(pkt);
//		for(Charactor c: players.values()){
//			c.sendMessage(pkt);
//		}
		try {
			requestBus.sendsContentAOp(pkt).signal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract int getRoomSize();
	protected abstract void onLeaveRoom(Player player);
	protected abstract int getFreeSeat();
	
	public boolean joinRoom(final Charactor charactor){
		synchronized (this) {
			if(size > getRoomSize()){
				return false;
			}
			size++;
		}
		int seat = getFreeSeat();
		Player player = charactor.getPlayer();
		player.setSeat(seat);
		players.put(charactor.getId(), charactor);

		charactor.register(requestBus, _reactor);
		return true;
	}
	
	public void leaveRoom(Charactor charactor){
		size--;
		Player player = charactor.getPlayer();
		
//		group.remove(session);
		players.remove(charactor.getId());
		
		Packet ret = Packets.newPacket(Packets.ROOM, Packets.ROOM_LEAVE, getPlayers());
		broadcast(ret);
		onLeaveRoom(player);
	}
	
	
	public Collection<Player> getPlayers(){
		ArrayList<Player> ret = Lists.newArrayList();
		for(Charactor c: players.values()){
			ret.add(c.getPlayer());
		}
		return ret;
	}
	
	public void close(){
		players.clear();
		players = null;
//		group.close();
	}

	protected int getPlayerCount(){
		return players.size();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
