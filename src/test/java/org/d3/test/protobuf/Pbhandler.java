package org.d3.test.protobuf;

import org.d3.net.packet.protobuf.Example.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class Pbhandler extends SimpleChannelInboundHandler<Message> {

	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Message arg1)
			throws Exception {
		System.out.println(arg1);
	}

}
