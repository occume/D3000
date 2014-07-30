package org.d3.net.session;

import io.netty.channel.Channel;
import org.d3.net.packet.Packet;

public class PlayerSession extends SessionSupport{

	private Channel 	channel;
	
	private volatile boolean online;
	
	public PlayerSession(Channel channel){
		this.channel = channel;
		setId(channel.id().toString());
	}
	
	public void onMessage(Packet pkt){
		System.out.println("session.onMessage()");
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
	
	public Channel getChannel(){
		return channel;
	}
	
	public void close(){
		channel.close();
	}

}
