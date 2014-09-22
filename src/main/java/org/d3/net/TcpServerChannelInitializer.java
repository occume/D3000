package org.d3.net;

import org.d3.D3Context;
import org.d3.core.concurrent.D3Gate;
import org.d3.net.handler.ClientTypeDecoder;

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
		
		gate.acquire();
		
		ChannelPipeline pipeline = ch.pipeline();
		ClientTypeDecoder clientTypeDecoder = (ClientTypeDecoder) D3Context.getBean("clientTypeDecoder");
		pipeline.addLast("initHandler", clientTypeDecoder);
		
	}
	
}