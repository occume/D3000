package org.d3.module.user.processor;

import java.util.Collection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.logger.D3Log;
import org.d3.module.BaseProcessor;
import org.d3.module.user.UserCmd;
import org.d3.module.user.UserModule;
import org.d3.module.user.bean.Player;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.packet.protobuf.Game.Login;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.d3.persist.PlayerService;
import org.d3.util.ObjectConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddFriendProcessor extends BaseProcessor {
	
	public AddFriendProcessor() throws Exception{
		super();
	}
	
	private static Logger LOG = LoggerFactory.getLogger(AddFriendProcessor.class);
	@Autowired
	private UserService userService;

	@Override
	public void doProcess(Session session, InPacket ask) {
		
		byte[] data = (byte[]) ask.getTuple();
		UserCmd cmd = Protobufs.getUserCmd(data);
		
		
		int type = cmd.getType();
		switch(type){
			case 1:
				askAddFriend(cmd, ask);
				break;
			case 2:
				agreeAddFriend(cmd, ask, session);
				break;
		}
	}
	
	private void askAddFriend(UserCmd cmd, InPacket ask) {
		
		User target = lookupUserByName(cmd.getTarget());
		Session targetSession = SessionManager.instance().getByName(target.getName());
		
		if(!targetSession.isActive()){
			//缓存消息
		}
		else{
			Chat ret = Protobufs.makeOkChatPacket(
				"1",
				cmd.getName(),
				cmd.getTarget(), 
				""
			);

			ByteBuf resp = Packets.makeReplyPacket(ask.getModule(), ask.getCmd(), ret.toByteArray());
			targetSession.sendMessage(resp);
		}
	}
	
	private void agreeAddFriend(UserCmd cmd, InPacket ask, Session session){
		User target = lookupUserByName(cmd.getTarget());
		Session targetSession = SessionManager.instance().getByName(target.getName());
		if(!targetSession.isActive()){
			//缓存消息
		}
		else{
			Chat ret = Protobufs.makeOkChatPacket(
				"2",
				cmd.getName(),
				cmd.getTarget(), 
				""
			);

			ByteBuf resp = Packets.makeReplyPacket(ask.getModule(), ask.getCmd(), ret.toByteArray());
			session.sendMessage(resp);
			targetSession.sendMessage(resp.copy());
		}
	}

	private User lookupUserByName(String name){
		User user = userService.getByName(name);
		return user;
	}

	@Override
	public String getModuleName() {
		return "userModule";
	}

	@Override
	public int getType() {
		return UserModule.ADD_FRIEND;
	}

	@Override
	public String getDescription() {
		return "AddFriendProcessor";
	}
	
}
