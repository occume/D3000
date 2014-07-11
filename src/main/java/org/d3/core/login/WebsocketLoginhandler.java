package org.d3.core.login;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.D3Context;
import org.d3.core.packet.BasePacket;
import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;
import org.d3.core.protocol.websocket.PacketHandler;
import org.d3.core.protocol.websocket.TextWebsocketDecoder;
import org.d3.core.protocol.websocket.TextWebsocketEncoder;
import org.d3.core.service.RoomService;
import org.d3.core.session.PlayerSession;
import org.d3.core.session.Room;
import org.d3.core.session.SessionManager;
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

@Component
@Sharable
public class WebsocketLoginhandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Autowired
	private ObjectMapper jackson;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private RoomService roomService;
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		
		String inData = msg.text();
		BasePacket pkt = null;
		try{
			pkt = jackson.readValue(inData, BasePacket.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(inData);
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
				
				PlayerSession session = new PlayerSession(ctx.channel());
				
//				session.setRoom(room);
				SessionManager.getInstance().putSession(session);
				
				ChannelPipeline pipeline = ctx.pipeline();
				TextWebsocketDecoder decoder = (TextWebsocketDecoder) D3Context.getBean("textWebsocketDecoder");
				TextWebsocketEncoder encoder = (TextWebsocketEncoder) D3Context.getBean("textWebsocketEncoder");
				pipeline.addLast(decoder);
				pipeline.addLast(encoder);
				pipeline.addLast(new PacketHandler(session));
				
				pipeline.remove(this);
				
				Packet pkt1 = Packets.newPacket(Packets.LOG_IN_SUCCESS, null);
				String json = jackson.writeValueAsString(pkt1);
				ctx.writeAndFlush(new TextWebSocketFrame(json));
			}
			else{
				
				ctx.close();
			}
		}
		
	}

}
