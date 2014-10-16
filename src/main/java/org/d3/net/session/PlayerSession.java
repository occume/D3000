package org.d3.net.session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import org.d3.core.mybatis.domain.User;
import org.d3.module.chat.ChatRoom;
import org.d3.module.user.bean.Player;

public class PlayerSession extends SessionSupport{

	private Channel 	channel;
	
	private Player player;
	
	private User user;
	
	private ChatRoom room;
	
	private volatile boolean online;
	
	public PlayerSession(Channel channel){
		this.channel = channel;
		setId(channel.id().toString());
	}
	
	public void onMessage(Object pkt){
		System.out.println("session.onMessage()");
	}
	
	public void sendMessage(ByteBuf pkt) {
		if(channel.isActive()){
//			ChannelPromise promise = new DefaultChannelPromise(channel);
//			promise.addListener(new ChannelFutureListener() {
//				
//				public void operationComplete(ChannelFuture future) throws Exception {
//					future.cause().printStackTrace();
//				}
//				
//			});
			if(channel.isWritable())
			channel.writeAndFlush(pkt);
		}
	}
	
	public boolean isOnline(){
		return online;
	}
	
	public void setOnline(){
		online = true;
	}
	
	public void offLine(){
		online = false;
	}
	
	public Channel getChannel(){
		return channel;
	}
	
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	public void close(){
		channel.close();
		if(getRoom() != null)
			getRoom().leaveRoom(this);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer(){
		return this.player;
	}

	public void setRoom(ChatRoom room) {
		this.room = room;
	}

	public ChatRoom getRoom() {
		return room;
	}

	public Channel channel() {
		return channel;
	}

	public boolean isActive() {
		return channel.isActive();
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

}
