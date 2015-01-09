package org.d3.module.user;

import org.d3.core.mybatis.domain.Message;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.MessageService;
import org.d3.core.mybatis.service.UserService;
import org.d3.net.packet.InPacket;
import org.d3.net.session.Sender;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Friend {
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	public void askFriend(UserCmd cmd, Session session) {
		
		User target = lookupUserByName(cmd.getTarget());
		Session targetSession = SessionManager.instance().getByName(target.getName());
		
		Message message = new Message();
		message.setUid1(session.getUser().getId());
		message.setUid2(target.getId());
		message.setType(MessageType.ASK_ADD_FRIEND);
		
		messageService.addMessage(message);
		
		if(targetSession == null || !targetSession.isActive()){
			System.out.println("not online");
			//缓存消息
		}
		else{
			Sender.instance().askFriend(targetSession, cmd);
		}
	}
	
	public void agreeFriend(UserCmd cmd, InPacket ask, Session session){
		
		User target = lookupUserByName(cmd.getTarget());
		Session targetSession = SessionManager.instance().getByName(target.getName());
		
		userService.addFriend(session.getUser().getId(),
					targetSession.getUser().getId(), UserRelationType.FRIEND);
		
		Sender.instance().refreshFriendList(session);
		
		if(targetSession != null && targetSession.isActive()){
			Sender.instance().agreeFriend(targetSession, cmd);
			Sender.instance().refreshFriendList(targetSession);
		}
	}

	private User lookupUserByName(String name){
		User user = userService.getByName(name);
		return user;
	}
}
