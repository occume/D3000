package org.d3.core.protocol.websocket;

import org.d3.core.packet.Packet;
import org.d3.core.session.PlayerSession;
import org.d3.core.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
	
	private Session session;
	
	public PacketHandler(PlayerSession session){
		this.session = session;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet msg)
			throws Exception {
		session.onMessage(msg);
	}
}
