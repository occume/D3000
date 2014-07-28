package org.d3.core;

import org.d3.D3Context;
import org.d3.core.concurrent.D3Gate;
import org.d3.core.login.LoginDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TcpServerChannelInitializer extends
		ChannelInitializer<SocketChannel> {
	
	private D3Gate gate;
	
	public TcpServerChannelInitializer(){
		gate = (D3Gate) D3Context.getBean("gate");
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		System.out.println(Thread.currentThread().getName());
		gate.acquire();
		
		ChannelPipeline pipeline = ch.pipeline();
		LoginDecoder loginDecoder = (LoginDecoder) D3Context.getBean("loginDecoder");
		pipeline.addLast("initHandler", loginDecoder);
		
	}
	
}