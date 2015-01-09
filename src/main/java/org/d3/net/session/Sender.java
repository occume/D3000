package org.d3.net.session;

import io.netty.buffer.ByteBuf;

import java.util.List;

import org.d3.D3Context;
import org.d3.core.mybatis.domain.Message;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.MessageService;
import org.d3.core.mybatis.service.UserService;
import org.d3.module.Module;
import org.d3.module.login.LoginModule;
import org.d3.module.user.MessageType;
import org.d3.module.user.UserCmd;
import org.d3.module.user.UserModule;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.packet.protobuf.Game.Login;
import org.d3.util.ObjectConvert;

public class Sender {
	
	private static class SenderHolder{
		private static Sender instance = new Sender();
	}
	
	private UserService userService;
	private MessageService messageService;
	
	private Sender(){
		userService = (UserService) D3Context.getBean("userService");
		messageService = (MessageService) D3Context.getBean("messageService");
	}
	
	public static Sender instance(){
		return SenderHolder.instance;
	}

	public void refreshFriendList(Session session){
		
		int id = session.getUser().getId();
		List<User> friends = userService.getFriendsById(id);
		
		Chat chat = Protobufs.makeOkChatPacket("", "", "", ObjectConvert.Me().ojb2json(friends));
		
		ByteBuf resp = Packets.makeReplyPacket(Module.USER, UserModule.FRIEND_LIST, chat.toByteArray());
		session.sendMessage(resp);
	}
	public void refreshMessageList(Session session){
		
		int id = session.getUser().getId();
		List<Message> msgs = messageService.getMsgsByRcvId(id);
		
		Chat chat = Protobufs.makeOkChatPacket("", "", "", ObjectConvert.Me().ojb2json(msgs));
		
		ByteBuf resp = Packets.makeReplyPacket(Module.USER, UserModule.MSG_LIST, chat.toByteArray());
		session.sendMessage(resp);
	}
	
	public void loginOk(Session session, InPacket ask){
		
		Login ret = Game.Login.newBuilder()
					.setName(session.getUser().getName())
					.setState("00")
					.build();
		ByteBuf resp = Packets.makeReplyPacket(Module.LOGIN, LoginModule.LOGIN, ret.toByteArray());
		session.sendMessage(resp);
	}
	
	public void askFriend(Session session, UserCmd cmd){
		Chat ret = Protobufs.makeOkChatPacket(
				"" + MessageType.ASK_ADD_FRIEND,
				cmd.getName(),
				cmd.getTarget(),
				""
			);

		ByteBuf resp = Packets.makeReplyPacket(Module.USER, UserModule.MESSAGE, ret.toByteArray());
		session.sendMessage(resp);
	}

	public void agreeFriend(Session session, UserCmd cmd){
		Chat ret = Protobufs.makeOkChatPacket(
				"" + MessageType.AGREE_ADD_FRIEND,
				cmd.getName(),
				cmd.getTarget(),
				""
			);

		ByteBuf resp = Packets.makeReplyPacket(Module.USER, UserModule.MESSAGE, ret.toByteArray());
		session.sendMessage(resp.copy());
	}
}
