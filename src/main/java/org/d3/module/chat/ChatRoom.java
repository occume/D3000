package org.d3.module.chat;

import org.d3.net.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatRoom{

	private int id;
	private String name;
	private int number;
	
	public final transient ChannelGroup channels;
	
	private static Logger LOG = LoggerFactory.getLogger(ChatRoom.class);
	
	public ChatRoom(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		channels = new DefaultChannelGroup(name, GlobalEventExecutor.INSTANCE);
	}
	
	public void boradcast(Object msg){
		channels.writeAndFlush(msg);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public synchronized int getNumber() {
		return number;
	}

	public synchronized void setNumber(int number) {
		this.number = number;
	}

	public synchronized void enterRoom(Session session){
		
		session.setRoom(this);
		channels.add(session.channel());
		number = channels.size();
		
		if(LOG.isDebugEnabled()){
			LOG.debug(session.getPlayer().getName() + " enter room " + name + ";number = " + number);
		}
	}
	
	public synchronized void leaveRoom(Session session){
		
		channels.remove(session.channel());
		number = channels.size();
		
		if(LOG.isDebugEnabled()){
			LOG.debug(session.getPlayer().getName() + " leave room " + name + ";number = " + number);
		}
	}
}
