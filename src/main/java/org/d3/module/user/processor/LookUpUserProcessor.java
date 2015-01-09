package org.d3.module.user.processor;

import java.util.Collection;
import java.util.Collections;

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
public class LookUpUserProcessor extends BaseProcessor {
	
	public LookUpUserProcessor() throws Exception{
		super();
	}
	
	private static Logger LOG = LoggerFactory.getLogger(LookUpUserProcessor.class);
	@Autowired
	private UserService userDao;

	@Override
	public void doProcess(Session session, InPacket ask) {
		
		byte[] data = (byte[]) ask.getTuple();
		UserCmd cmd = Protobufs.getUserCmd(data);
		
		
		int type = cmd.getType();
		Object result = null;
		
		switch(type){
			case 1:
				User user = lookupUserByName(cmd.getName());
				if(user == null){
					result = Collections.singletonList("");
				}
				else{
					result = Collections.singletonList(user);
				}
				break;
			case 2:
				result = SessionManager.instance().randomUsers();
		}
		
		Chat ret = Protobufs.makeOkChatPacket(
			"0",
			"",
			"", 
			ObjectConvert.Me().ojb2json(result)
		);

		ByteBuf resp = Packets.makeReplyPacket(ask.getModule(), ask.getCmd(), ret.toByteArray());
		session.sendMessage(resp);
	}
	
	private User lookupUserByName(String name){
		User user = userDao.getByName(name);
		return user;
	}

	@Override
	public String getModuleName() {
		return "userModule";
	}

	@Override
	public int getType() {
		return UserModule.LOOK_UP;
	}

	@Override
	public String getDescription() {
		return "LookUpUserProcessor";
	}
	
}
