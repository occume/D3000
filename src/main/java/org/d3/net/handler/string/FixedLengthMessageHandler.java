package org.d3.net.handler.string;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import org.d3.D3Context;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.std.Stopwatch;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FixedLengthMessageHandler extends SimpleChannelInboundHandler<ByteBuf> {

	AtomicInteger totalCount = new AtomicInteger();
	AtomicInteger count = new AtomicInteger();
//	int count;
	private Stopwatch sw;
	private UserService us = (UserService) D3Context.getBean("userService");

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("complete");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.err.println(cause.getMessage());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		if(count.compareAndSet(0, 1)){
			System.out.println(this);
			sw = Stopwatch.newStopwatch();
		}
		else{
			count.incrementAndGet();
		}
		totalCount.addAndGet(msg.readableBytes());
		
//		System.out.println(count.get());
		if(count.get() == 10000){
			System.out.println(ctx.channel().getClass() + " " + sw.longTime());
			System.out.println(msg.toString(Charset.defaultCharset()));
			System.out.println("total: " + totalCount);
			System.out.println("rate: " + ((totalCount.get() / 1024) / sw.elapsedTime()));
			return;
		}
//		System.out.println(msg.toString(Charset.defaultCharset()));
		
		String name = msg.toString(Charset.defaultCharset());
//		if(name.contains("over")){
//			System.out.println(ctx.channel().getClass() + " " + sw.longTime());
//			System.out.println(msg.toString(Charset.defaultCharset()));
//			System.out.println("total: " + totalCount);
//			System.out.println("rate: " + ((totalCount.get() / 1024) / sw.elapsedTime()));
//			return;
//		}
//		User user = new User(name, name);
//		us.addUser(user);
		
	}

	

}
