package org.d3.core.login;

import org.d3.core.packet.Packet;
import org.d3.core.session.PlayerSession;
import org.d3.core.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketProcessor extends SimpleChannelInboundHandler<Packet> {
	
	private Session session;
	
	public PacketProcessor(PlayerSession session){
		this.session = session;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet msg)
			throws Exception {
		System.out.println(msg);
		System.out.println(session);
		session.onMessage(msg);
	}
}
