package org.d3.net.handler;

import org.d3.core.transfer.Charactor;
import org.d3.net.packet.Packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
	
	private Charactor charactor;
	
	public PacketHandler(){}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
//		PlayerSession playerSession = (PlayerSession) session;
//		playerSession.setOnline();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet msg)
			throws Exception {
		System.out.println(msg);
		charactor.onMessage(msg);
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		
//		PlayerSession playerSession = (PlayerSession) session;
		if(charactor.getRoom() != null){
			charactor.getRoom().leaveRoom(charactor);
			if(charactor.getPlayer().isReady())
				charactor.getRoom().playerUnPrepare();
		}
//		playerSession.offLine();
	}
}
