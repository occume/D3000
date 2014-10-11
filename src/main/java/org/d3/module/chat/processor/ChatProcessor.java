package org.d3.module.chat.processor;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.core.service.RoomService;
import org.d3.module.BaseProcessor;
import org.d3.module.chat.ChatInfo;
import org.d3.module.chat.ChatModule;
import org.d3.module.chat.ChatRoom;
import org.d3.module.user.bean.User;
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
public class ChatProcessor  extends BaseProcessor{
	
	private static Logger LOG = LoggerFactory.getLogger(ChatProcessor.class);
	@Autowired
	private RoomService roomService;

	public ChatProcessor() throws Exception {
		super();
	}
	
	
	public void validate(Session session, InPacket pkt) {
		// TODO Auto-generated method stub
		
	}
	int count = 0;
	Stopwatch sw = null;
	@Override
	public void doProcess(Session session, InPacket ask) {
		
		byte[] data = (byte[]) ask.getTuple();
		ChatInfo info = Protobufs.getChatInfo(data);
		
		String type = info.getType();

		if(type.equals("ROOM")){
			ChatRoom room = session.getRoom();
			
			Chat ret = Protobufs.makeOkChatPacket(
					type,
					session.getPlayer().getName(),
					"", 
					info.getInfo());

			ByteBuf resp = Packets.makeReplyPacket(ask.getModule(), ask.getCmd(), ret.toByteArray());
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
//			for(int i = 0; i < 10000; i++){
//				room.broadcast(resp);
//				resp.retain();
//			}
		}
		else if(type.equals("ALL")){
			
		}
		else{
			Session targetSession = SessionManager.instance().getByName(info.getTarget());
			Chat ret = Protobufs.makeOkChatPacket(
					type,
					session.getPlayer().getName(),
					info.getTarget(), 
					info.getInfo());

			ByteBuf resp = Packets.makeReplyPacket(ask.getModule(), ask.getCmd(), ret.toByteArray());
			targetSession.sendMessage(resp);
			session.sendMessage(resp.copy());
		}
	}

	@Override
	public String getModuleName() {
		return "chatModule";
	}

	@Override
	public int getType() {
		return ChatModule.CHAT;
	}

	@Override
	public String getDescription() {
		return "ChatProcessor";
	}

}
