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
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.session.Session;
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

	@Override
	public void doProcess(Session session, InPacket pkt) {
		
		byte[] data = (byte[]) pkt.getTuple();
		ChatInfo info = Protobufs.getChatInfo(data);

		ChatRoom room = session.getRoom();
		
		Chat ret = Game.Chat.newBuilder()
				.setName(session.getPlayer().getName())
				.setState("00")
				.setInfo(info.getMessage())
				.build();
		byte module = (byte) pkt.getModule();
		byte cmd = (byte) pkt.getCmd();
		ByteBuf resp = Unpooled.wrappedBuffer(new byte[]{module, cmd}, ret.toByteArray());

		room.boradcast(new BinaryWebSocketFrame(resp));
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
