package org.d3.core.session;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.d3.core.packet.Packet;

public class RoomSession extends SessionSupport{
	
	private ChannelGroup group;
	
	public RoomSession(){
		group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	}

	public void onMessage(Packet pkt) {
		
	}

	public void sendMassage(Packet pkt) {
		group.writeAndFlush(wrap(pkt));
	}
	
	public void addSession(PlayerSession session){
		group.add(session.getChannel());
	}

}
