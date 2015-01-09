package org.d3.net.handler.string;

import org.d3.D3Context;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.net.manage.World;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class StringHandler extends SimpleChannelInboundHandler<String> {
	

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
//		String resp = "FROM:[" + ctx.channel().remoteAddress() + "]; TO:[ALL]";
//		World.MSG_COUNT.incrementAndGet();
//		UserService us = (UserService) D3Context.getBean("userService");
//		User user = new User(msg, msg);
//		us.addUser(user);
//		World.ALL.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println(cause.getMessage());
		ctx.close();
	}
	
}
