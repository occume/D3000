package org.d3.net.packet;

import org.d3.module.chat.ChatInfo;
import org.d3.module.user.bean.User;
import org.d3.net.packet.protobuf.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

public class Protobufs {
	
	private static Logger LOG = LoggerFactory.getLogger(Protobufs.class);
	
	public static User getLoginUser(byte[] data){
		User user = null;
		try {
			Game.Login login = Game.Login.parseFrom(data);
			user = new User(login.getName(), login.getPassword());
		} catch (InvalidProtocolBufferException e) {
			LOG.error("parse login user error: " + e.getMessage());
		}
		return user;
	}
	
	public static ChatInfo getChatInfo(byte[] data){
		ChatInfo info = null;
		try {
			Game.Chat chat = Game.Chat.parseFrom(data);
			info = new ChatInfo(chat.getName(), chat.getTarget(), chat.getInfo());
		} catch (InvalidProtocolBufferException e) {
			LOG.error("parse chat info error: " + e.getMessage());
		}
		return info;
	}
	
}
