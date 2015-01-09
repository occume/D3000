package org.d3.module.chat;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.d3.core.service.RoomService;
import org.d3.module.BaseModule;
import org.d3.module.Module;
import org.d3.module.Processor;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.d3.std.Stopwatch;
import org.d3.util.ObjectConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatModule extends BaseModule{
	
	private static Logger LOG = LoggerFactory.getLogger(ChatModule.class);
	
	public static final int ROOM_LIST = 2;
	public static final int ENTER_ROOM = 3;
	public static final int LEAVE_ROOM = 4;
	public static final int CHAT = 5;
	
	@Autowired
	private RoomService roomService;

	public ChatModule(){
		register(CHAT, new Processor() {
			int count = 0;
			Stopwatch sw = null;
			@Override
			public void process(Session session, InPacket pkt) {

				byte[] data = (byte[]) pkt.getTuple();
				ChatInfo info = Protobufs.getChatInfo(data);
				
				String type = info.getType();

				if(type.equals("ROOM")){
					ChatRoom room = session.getRoom();
					
					Chat ret = Protobufs.makeOkChatPacket(
							type,
							session.getPlayer().getName(),
							"", 
							info.getInfo());

					ByteBuf resp = Packets.makeReplyPacket(pkt.getModule(), pkt.getCmd(), ret.toByteArray());
					++count;
					if(count == 1){
						sw = Stopwatch.newStopwatch();
					}
					if(count % 10000 == 0){
						long colisped = sw.longTime();
						System.out.println(colisped);
						System.out.println("count = " + count);
						System.out.println(info.getInfo());
						count = 0;
					}
//					for(int i = 0; i < 10000; i++){
//						room.broadcast(resp);
//						resp.retain();
//					}
				}
				else if(type.equals("ALL")){
					
				}
				else{
					
					Session targetSession = SessionManager.instance().getByName(info.getTarget());
					if(targetSession == null){
						
					}
					else{
					Chat ret = Protobufs.makeOkChatPacket(
							type,
							session.getUser().getName(),
							info.getTarget(),
							info.getInfo());

					ByteBuf resp = Packets.makeReplyPacket(pkt.getModule(), pkt.getCmd(), ret.toByteArray());
					targetSession.sendMessage(resp);
					session.sendMessage(resp.copy());
//					for(int i = 0; i < 100; i++){
//						session.channel().write(resp.copy());
//					}
//					session.channel().flush();
					}
				}
			}
		});
		
		register(ENTER_ROOM, new Processor() {
			@Override
			public void process(Session session, InPacket pkt) {

				byte[] data = (byte[]) pkt.getTuple();
				ChatInfo info = Protobufs.getChatInfo(data);
				
				int roomId;
				try{
					roomId = Integer.valueOf(info.getInfo());
				}
				catch(Exception e){
					LOG.error(e.getMessage());
					return;
				}
				ChatRoom room = roomService.getRoomById(roomId);
				room.enterRoom(session);
				
				Chat ret = Protobufs.makeOkChatPacket(
							"ROOM",
							session.getPlayer().getName(),
							"", 
							ObjectConvert.Me().ojb2json(room)
						);

				ByteBuf resp = Packets.makeReplyPacket(pkt.getModule(), pkt.getCmd(), ret.toByteArray());
//				session.sendMessage(new BinaryWebSocketFrame(resp));
				room.broadcast(resp);
			}
		});
		
		register(ROOM_LIST, new Processor() {
			@Override
			public void process(Session session, InPacket pkt) {

				Collection<ChatRoom> rooms = roomService.getRoomList();
				
				Chat ret = Game.Chat.newBuilder()
						.setName("")
						.setInfo(ObjectConvert.Me().ojb2json(rooms))
						.build();
				byte module = (byte) pkt.getModule();
				byte cmd = (byte) pkt.getCmd();
				ByteBuf resp = Unpooled.wrappedBuffer(new byte[]{module, cmd}, ret.toByteArray());
				session.sendMessage(resp);
			}
		});
	}
	
	public String getDescription() {
		return "Module for Chat";
	}

	public int getType() {
		return Module.CHAT;
	}
	
}
