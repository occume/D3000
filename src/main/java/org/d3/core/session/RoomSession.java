package org.d3.core.session;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.d3.core.packet.Packet;

public class RoomSession extends SessionSupport{
	
	private ChannelGroup group;
	private static final int ROOM_SIZE = 20;
	private int size = 0;
	protected AtomicInteger readyCount = new AtomicInteger();
	
	public RoomSession(String id, String name){
		super(id, name);
		group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	}

	public void onMessage(Packet pkt) {
		
	}

	public void sendMassage(Packet pkt) {
		group.writeAndFlush(pkt);
	}
	
	public boolean addSession(PlayerSession session){
		synchronized (this) {
			if(size > ROOM_SIZE){
				return false;
			}
			size++;
		}
		
		return group.add(session.getChannel());
	}
	
	public void removeSession(PlayerSession session){
		group.remove(session);
	}
	
	public void close(){
		group.close();
	}

	protected int getPlayerCount(){
		return group.size();
	}
}
