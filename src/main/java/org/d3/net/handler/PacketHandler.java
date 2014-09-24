package org.d3.net.handler;

import org.d3.core.transfer.Charactor;
import org.d3.module.Dispatcher;
import org.d3.net.packet.InPacket;
import org.d3.net.session.Session;
import org.d3.net.session.Sessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Scope("prototype")
public class PacketHandler extends SimpleChannelInboundHandler<InPacket> {
	
//	private Charactor charactor;
	Session session;
	
	@Autowired
	private Dispatcher dispatcher;
	
	public PacketHandler(){}

	/**
	 * 为什么调用不到?
	 * 因为active的时候,这个handler还没进入pipeline
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		session = Sessions.newSession(ctx.channel());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, InPacket pkt)
			throws Exception {
//		charactor.onMessage(msg);
		dispatcher.dispatch(session, pkt);
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
//		if(charactor.getRoom() != null){
//			charactor.getRoom().leaveRoom(charactor);
//			if(charactor.getPlayer().isReady())
//				charactor.getRoom().playerUnPrepare();
//		}
//		playerSession.offLine();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		System.err.println(cause.getMessage());
	}
}
