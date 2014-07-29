package org.d3.core.session;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import org.d3.core.util.ObjectConvert;
import org.d3.net.packet.Packet;

public abstract class SessionSupport implements Session {

	private String id;
	private String name;
	private long lastAccessTime = System.currentTimeMillis();
	
	public SessionSupport(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	public SessionSupport(){}
	
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

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
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
