package org.d3.net.handler.string;

import org.d3.net.manage.World;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class StringHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
		World.ALL.writeAndFlush("FROM:[" + ctx.channel().remoteAddress() + "]; TO:[ALL]");
	}

}
