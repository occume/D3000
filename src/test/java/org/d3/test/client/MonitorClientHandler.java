package org.d3.test.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MonitorClientHandler extends SimpleChannelInboundHandler<String>{
	
	
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println(msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().writeAndFlush("aa");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

}
