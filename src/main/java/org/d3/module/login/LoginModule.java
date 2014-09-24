package org.d3.module.login;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.d3.D3Context;
import org.d3.module.Module;
import org.d3.module.Processor;
import org.d3.module.Registerable;
import org.d3.module.Registry;
import org.d3.net.packet.InPacket;
import org.d3.net.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class LoginModule implements Module{
	
	private static Logger LOG = LoggerFactory.getLogger(LoginModule.class);
	private Map<Integer, Processor> processors = Maps.newHashMap();
	

	public String getDescription() {
		return "Module for Login";
	}

	public int getType() {
		return Module.LOGIN;
	}
	
	@PostConstruct
	public void registerModule() {
		Registry registry = (Registry) D3Context.getBean("dispatcher");
		if(registry != null){
			if(LOG.isDebugEnabled()){
				LOG.debug("register LoginModule");
			}
			registry.register(getType(), this);
		}
	}

	public void service(Session session, InPacket pkt) {
		int cmd = pkt.getCmd();
		Processor processor = getProcessor(cmd);
		if(processor != null){
			processor.process(session, pkt);
		}
	}

	public Processor getProcessor(int cmd){
		return processors.get(cmd);
	}
	
	public void register(int type, Registerable module) {
		processors.put(type, (Processor)module);
	}

}
