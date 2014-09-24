package org.d3.module;

import java.util.Map;

import org.d3.net.packet.InPacket;
import org.d3.net.session.Session;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class Dispatcher implements Registry{
	
	private Map<Integer, Module> modules = Maps.newHashMap();

	public void register(int type, Registerable module) {
		modules.put(type, (Module)module);
	}
	
	public Module getModule(int type){
		return modules.get(type);
	}
	
	public void dispatch(Session session, InPacket pkt){
		Module module = getModule(pkt.getModule());
		if(module != null){
			module.service(session, pkt);
		}
	}
	
}
