package org.d3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleHandler extends ChannelInboundHandlerAdapter{
	
	public SimpleHandler(){
		
	}

	Semaphore s = new Semaphore(0);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

}
