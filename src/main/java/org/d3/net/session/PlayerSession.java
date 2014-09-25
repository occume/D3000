package org.d3.net.session;

import io.netty.channel.Channel;

import org.d3.module.chat.ChatRoom;
import org.d3.module.user.bean.Player;

public class PlayerSession extends SessionSupport{

	private Channel 	channel;
	
	private Player player;
	
	private ChatRoom room;
	
	private volatile boolean online;
	
	public PlayerSession(Channel channel){
		this.channel = channel;
		setId(channel.id().toString());
	}
	
	public void onMessage(Object pkt){
		System.out.println("session.onMessage()");
	}
	
	public void sendMessage(Object pkt) {
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
	
	public Channel getChannel(){
		return channel;
	}
	
	public void close(){
		channel.close();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer(){
		return this.player;
	}

	public void setRoom(ChatRoom room) {
		this.room = room;
	}

	public ChatRoom getRoom() {
		return room;
	}

	public Channel channel() {
		return channel;
	}

}
