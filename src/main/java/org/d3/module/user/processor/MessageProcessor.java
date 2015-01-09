package org.d3.module.user.processor;

import io.netty.buffer.ByteBuf;

import org.d3.core.mybatis.domain.Message;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.MessageService;
import org.d3.core.mybatis.service.UserService;
import org.d3.module.Processor;
import org.d3.module.user.MessageType;
import org.d3.module.user.UserCmd;
import org.d3.module.user.UserRelationType;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor implements Processor {
	
	private static Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);
	
	@Autowired
	private UserService userDao;
	@Autowired
	private MessageService messageService;

	@Override
	public void process(Session session, InPacket ask) {
		
		byte[] data = (byte[]) ask.getTuple();
		UserCmd cmd = Protobufs.getUserCmd(data);
		
		int type = cmd.getType();
		switch(type){
			case MessageType.ASK_ADD_FRIEND:
				askAddFriend(cmd, ask, session);
				break;
			case MessageType.AGREE_ADD_FRIEND:
				agreeAddFriend(cmd, ask, session);
				break;
		}
	}
	
	private void askAddFriend(UserCmd cmd, InPacket ask, Session session) {
		
		User target = lookupUserByName(cmd.getTarget());
		Session targetSession = SessionManager.instance().getByName(target.getName());
		
		Message message = new Message();
		message.setUid1(session.getUser().getId());
		message.setUid2(targetSession.getUser().getId());
		message.setType(MessageType.ASK_ADD_FRIEND);
		
		messageService.addMessage(message);
		
		if(!targetSession.isActive()){
			//缓存消息
		}
		else{
			Chat ret = Protobufs.makeOkChatPacket(
				"" + MessageType.ASK_ADD_FRIEND,
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
		
		userDao.addFriend(session.getUser().getId(),
					targetSession.getUser().getId(), 
					UserRelationType.FRIEND);
		
		if(!targetSession.isActive()){
			//缓存消息
		}
		else{
			Chat ret = Protobufs.makeOkChatPacket(
					"" + MessageType.AGREE_ADD_FRIEND,
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
		User user = userDao.getByName(name);
		return user;
	}

}
