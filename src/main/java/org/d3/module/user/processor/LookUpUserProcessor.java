package org.d3.module.user.processor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.core.mybatis.domain.User;
import org.d3.core.service.UserService;
import org.d3.logger.D3Log;
import org.d3.module.BaseProcessor;
import org.d3.module.user.bean.Player;
import org.d3.net.packet.InPacket;
import org.d3.net.packet.Packets;
import org.d3.net.packet.Protobufs;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.Game.Login;
import org.d3.net.session.Session;
import org.d3.persist.PlayerService;
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
	private UserService userService;

	@Override
	public void doProcess(Session session, InPacket ask) {
		
		User user = userService.getUserByName("");
		
	}

	@Override
	public String getModuleName() {
		return "userModule";
	}

	@Override
	public int getType() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "LookUpUserProcessor";
	}
	
}
