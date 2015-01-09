package org.d3.module.login.processor;

import java.util.List;

import io.netty.buffer.ByteBuf;

import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.logger.D3Log;
import org.d3.module.BaseProcessor;
import org.d3.module.Module;
import org.d3.module.user.UserModule;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Chat;
import org.d3.net.packet.protobuf.Game.Login;
import org.d3.net.session.Sender;
import org.d3.net.session.Session;
import org.d3.util.ObjectConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginProcessor extends BaseProcessor {
	
	public LoginProcessor() throws Exception{
		super();
	}
	
	private static Logger LOG = LoggerFactory.getLogger(LoginProcessor.class);
	@Autowired
	private UserService userService;

	@Override
	public void doProcess(Session session, InPacket ask) {
		
		User user = (User) ask.getTuple();

		if(userService.auth(user)){
			
			session.setUser(user);
			
			Sender.instance().loginOk(session, ask);
			Sender.instance().refreshFriendList(session);
		}
		else{
			LOG.debug(D3Log.D3_LOG_DEBUG + "User auth fail: " + user);
			session.close();
		}
		
	}

	@Override
	public String getModuleName() {
		return "loginModule";
	}

	@Override
	public int getType() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "LoginProcessor";
	}
	
}
