package org.d3.module.login.processor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.logger.D3Log;
import org.d3.module.BaseProcessor;
import org.d3.module.user.bean.Player;
import org.d3.module.user.bean.User;
import org.d3.net.packet.InPacket;
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
public class LoginProcessor extends BaseProcessor {
	
	public LoginProcessor() throws Exception{
		super();
	}
	
	private static Logger LOG = LoggerFactory.getLogger(LoginProcessor.class);
	@Autowired
	private PlayerService playerService;

	@Override
	public void doProcess(Session session, InPacket pkt) {
		
		byte[] data = (byte[]) pkt.getTuple();
		User user = Protobufs.getLoginUser(data);

		if(playerService.auth()){
			session.setPlayer(new Player(user.getName(), user.getPassword()));
			
			Login ret = Game.Login.newBuilder()
						.setState("00")
						.build();
			byte module = (byte) pkt.getModule();
			byte cmd = (byte) pkt.getCmd();
			ByteBuf resp = Unpooled.wrappedBuffer(new byte[]{module, cmd}, ret.toByteArray());
			session.sendMessage(new BinaryWebSocketFrame(resp));
//			System.out.println("pass auth");
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
	
}
