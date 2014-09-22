package org.d3.net.handler;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.D3Context;
import org.d3.core.service.RoomService;
import org.d3.core.service.UniqueIdService;
import org.d3.core.transfer.Charactor;
import org.d3.game.bean.Player;
import org.d3.net.packet.BasePacket;
import org.d3.net.packet.Packet;
import org.d3.net.packet.Packets;
import org.d3.net.packet.json.PacketHandler;
import org.d3.net.packet.json.TextWebsocketDecoder;
import org.d3.net.packet.json.TextWebsocketEncoder;
import org.d3.net.session.PlayerSession;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.d3.net.session.Sessions;
import org.d3.persist.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateHandler;

@Component
@Sharable
public class Loginhandler extends SimpleChannelInboundHandler<WebSocketFrame> {

	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private UniqueIdService idService;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			WebSocketFrame msg) throws Exception {
		System.out.println(msg.getClass().getName());
		String inData = "";//msg.text();
		BasePacket pkt = null;
		try{
			pkt = jackson.readValue(inData, BasePacket.class);
		}catch(Exception e){
			e.printStackTrace();
		}
//		System.out.println(inData);
		if(pkt.getAct() == Packets.CONNECT){
			
			Packet pkt1 = Packets.newLoginPacket();
			
			String json = jackson.writeValueAsString(pkt1);
			
			ChannelFuture cf = ctx.writeAndFlush(new TextWebSocketFrame(json));
			cf.addListener(new ChannelFutureListener() {
				
				public void operationComplete(ChannelFuture future) throws Exception {
					if(!future.isSuccess()){
						System.out.println(future.cause().getMessage());
					}
				}
			});
			
		}
		else if(pkt.getAct() == Packets.LOG_IN){
			
			Map<String, String> rstMap = (Map<String, String>) pkt.getTuple();
			String userName = rstMap.get("username");
			String password = rstMap.get("password");
			
			if(playerService.auth()){
				
				Session session = Sessions.newSession(ctx.channel());
				Charactor c = new Charactor(session, new Player(session, userName, session.getId()));
				
				applyProtocol(ctx, c);
				
				String reconnKey = idService.generate();
				Packet pkt1 = Packets.newPacket(Packets.LOG_IN_SUCCESS, session.getId());
				String json = jackson.writeValueAsString(pkt1);
				ctx.writeAndFlush(new TextWebSocketFrame(json));
			}
			else{
				Packet pkt1 = Packets.newPacket(Packets.LOG_IN_FAILURE, null);
				String json = jackson.writeValueAsString(pkt1);
				
				ctx.writeAndFlush(new TextWebSocketFrame(json))
					.addListener(ChannelFutureListener.CLOSE);
			}
		}
		else if(pkt.getAct() == Packets.RECONNECT){
			SessionManager sessionManager = (SessionManager) D3Context.getBean("sessionManager");
			String reconnKey = pkt.getTuple().toString();
			Session session = sessionManager.getSession(reconnKey);
			
			if(session != null){
				PlayerSession ps = (PlayerSession) session;
//				ps.setChannle(ctx.channel());
//				applyProtocol(ctx, ps);
			}
		}
		
	}
	
	private void applyProtocol(ChannelHandlerContext ctx, Charactor c){
		
		ChannelPipeline pipeline = ctx.pipeline();
		TextWebsocketDecoder decoder = (TextWebsocketDecoder) D3Context.getBean("textWebsocketDecoder");
		TextWebsocketEncoder encoder = (TextWebsocketEncoder) D3Context.getBean("textWebsocketEncoder");
		pipeline.addLast(decoder);
		pipeline.addLast(encoder);
		pipeline.addLast("idleStateCheck", new IdleStateHandler(
				300, 300, 300));
		pipeline.addLast(new PacketHandler(c));
		
		pipeline.remove(this);
	}

}
