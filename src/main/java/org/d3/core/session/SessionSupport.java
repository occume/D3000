package org.d3.core.session;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import org.d3.core.packet.Packet;
import org.d3.core.util.ObjectConvert;

public abstract class SessionSupport implements Session {

	private String id;
	private String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void onMessage(Packet pkt) {
		
	}

	public void sendMessage(Packet pkt) {
		
	}
	
	protected TextWebSocketFrame wrap(Packet pkt){
		String json = ObjectConvert.Me().ojb2json(pkt);
		return new TextWebSocketFrame(json);
	}

}
