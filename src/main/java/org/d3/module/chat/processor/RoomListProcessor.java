package org.d3.module.chat.processor;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.Room;
import org.d3.core.service.RoomService;
import org.d3.module.BaseProcessor;
import org.d3.module.chat.ChatModule;
import org.d3.module.chat.ChatRoom;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.session.Session;
import org.d3.util.ObjectConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomListProcessor  extends BaseProcessor{
	
	@Autowired
	private RoomService roomService;

	public RoomListProcessor() throws Exception {
		super();
	}

	@Override
	public void doProcess(Session session, InPacket pkt) {
		
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

	@Override
	public String getModuleName() {
		return "chatModule";
	}

	@Override
	public int getType() {
		return ChatModule.ROOM_LIST;
	}

	@Override
	public String getDescription() {
		return "RoomListProcessor";
	}

}
