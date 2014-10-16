package org.d3.net.session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import org.d3.core.mybatis.domain.User;
import org.d3.module.chat.ChatRoom;
import org.d3.module.user.bean.Player;

public interface Session {
	
	public void setId(String id);
	
	public String getId();
	
	public String getName();
	
	public void setName(String name);
	
	public void onMessage(Object pkt);
	
	public void sendMessage(ByteBuf pkt);
	
	public void setLastAccessTime(long time);
	
	public long getLastAccessTime();
	
	public void setPlayer(Player player);
	
	public Player getPlayer();
	
	public void setUser(User user);
	
	public User getUser();
	
	public void setRoom(ChatRoom room);
	
	public ChatRoom getRoom();
	
	public Channel channel();
	
	public void setChannel(Channel channel);
	
	public boolean isActive();
	
	public void close();
}
