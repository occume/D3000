package org.d3.core.session;

import io.netty.channel.Channel;
import org.d3.core.packet.Packet;

public class PlayerSession extends SessionSupport{

	private Dispacher 	t;
	private Channel 	channel;
	private Room		room;
	
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
		channel.writeAndFlush(pkt);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		room.addSession(this);
	}
	
	public Channel getChannel(){
		return channel;
	}

}
