package org.d3.module.login;

import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.logger.D3Log;
import org.d3.module.BaseModule;
import org.d3.module.Module;
import org.d3.module.Processor;
import org.d3.net.packet.InPacket;
import org.d3.net.session.Sender;
import org.d3.net.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginModule extends BaseModule{
	
	private static Logger LOG = LoggerFactory.getLogger(LoginModule.class);
	
	public static final int LOGIN = 1;
	
	@Autowired
	private UserService userDao;
	
	public LoginModule(){
		register(LOGIN, new Processor() {
			
			@Override
			public void process(Session session, InPacket pkt) {

				User user = (User) pkt.getTuple();

				if(userDao.auth(user)){
					
					session.setUser(user);
					
					Sender.instance().loginOk(session, pkt);
					Sender.instance().refreshFriendList(session);
					Sender.instance().refreshMessageList(session);
				}
				else{
					LOG.debug(D3Log.D3_LOG_DEBUG + "User auth fail: " + user);
					session.close();
				}
				
			}
		});
	}

	public String getDescription() {
		return "Module for Login";
	}

	public int getType() {
		return Module.LOGIN;
	}

}
