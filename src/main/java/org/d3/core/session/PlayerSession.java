package org.d3.core.session;

import io.netty.channel.Channel;

import org.d3.Room;
import org.d3.net.packet.Packet;

public class PlayerSession extends SessionSupport{

	private Dispacher 	t;
	private Channel 	channel;
	private Room		room;
	private Player		player;
	private volatile boolean online;
	
	public PlayerSession(Channel channel){
		this.channel = channel;
		setId(channel.id().toString());
		try {
			t = new Dispacher(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onMessage(Packet pkt){
		try {
			t.onMessage(pkt).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Packet pkt) {
		if(channel.isActive())
			channel.writeAndFlush(pkt);
	}
	
	public boolean isOnline(){
		return online;
	}
	
	public void setOnline(){
		online = true;
	}
	
	public void offLine(){
		online = false;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Channel getChannel(){
		return channel;
	}
	
	public void setChannle(Channel channel){
		this.channel = channel;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void close(){
		channel.close();
		if(room != null)
			room.leaveRoom(this);
	}

}
