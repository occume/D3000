package org.d3.module.user;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.domain.UserRelation;
import org.d3.core.mybatis.service.UserService;
import org.d3.module.BaseModule;
import org.d3.module.Module;
import org.d3.module.Processor;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.session.Session;
import org.d3.net.session.SessionManager;
import org.d3.util.ObjectConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserModule extends BaseModule{
	
	private static Logger LOG = LoggerFactory.getLogger(UserModule.class);
	
	public static final int LOOK_UP = 2;
	public static final int MESSAGE = 3;
	public static final int FRIEND_LIST = 4;
	public static final int MSG_LIST = 5;
	
	@Autowired
	private UserService userService;
	@Autowired
	private Friend friend;
	public UserModule(){
		
		register(MESSAGE, new Processor() {
			@Override
			public void process(Session session, InPacket pkt) {
				byte[] data = (byte[]) pkt.getTuple();
				UserCmd cmd = Protobufs.getUserCmd(data);
				
				int type = cmd.getType();
				switch(type){
					case MessageType.ASK_ADD_FRIEND:
						friend.askFriend(cmd, session);
						break;
					case MessageType.AGREE_ADD_FRIEND:
						friend.agreeFriend(cmd, pkt, session);
						break;
				}
			}
		});
		
		register(LOOK_UP, new Processor() {
			
			@Override
			public void process(Session session, InPacket pkt) {

				byte[] data = (byte[]) pkt.getTuple();
				UserCmd cmd = Protobufs.getUserCmd(data);
				
				int type = cmd.getType();
				Object result = null;
				List<Integer> ids = new ArrayList<>();
				
				switch(type){
					case 1:
						User user = userService.getByName(cmd.getName());
						if(user == null){
							result = Collections.singletonList("");
						}
						else if(user.getName().equals(session.getUser().getName())){
							user.setRelation(-1);
							result = Collections.singletonList(user);
						}
						else{
							result = Collections.singletonList(user);
							ids.add(user.getId());
							List<UserRelation> relations = userService.getRelationsById(session.getUser().getId(), ids);
							
							if(relations.size() > 0){
								user.setRelation(relations.get(0).getType());
							}
						}
						break;
					case 2:
						result = SessionManager.instance().randomUsers();
				}
				
				Chat ret = Protobufs.makeOkChatPacket(
					"0", "", "", 
					ObjectConvert.Me().ojb2json(result)
				);

				ByteBuf resp = Packets.makeReplyPacket(pkt.getModule(), pkt.getCmd(), ret.toByteArray());
				session.sendMessage(resp);
			}
		});
	}

	public String getDescription() {
		return "Module for User";
	}

	public int getType() {
		return Module.USER;
	}
	
}