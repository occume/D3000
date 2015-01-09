package org.d3.net.handler.string;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.d3.D3Context;
import org.d3.core.mybatis.domain.User;
import org.d3.core.mybatis.service.UserService;
import org.d3.std.Stopwatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class FixedLengthMessageHandler extends SimpleChannelInboundHandler<ByteBuf> {

	AtomicInteger totalCount = new AtomicInteger();
	AtomicInteger count = new AtomicInteger();
	
	private Stopwatch sw;
	@Autowired
	private UserService us;
	
	private List<User> users = new ArrayList<User>();

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("complete");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
//		System.err.println(cause.getMessage());
		cause.printStackTrace();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
//		System.out.println(ctx.channel().remoteAddress());
		if(count.compareAndSet(0, 1)){
			System.out.println(this);
			sw = Stopwatch.newStopwatch();
		}
		else{
			count.incrementAndGet();
//			System.out.println(count.incrementAndGet());
		}
		totalCount.addAndGet(msg.readableBytes());
//		
//		count.get());
		
//		System.out.println(msg.toString(Charset.defaultCharset()));
		
		String name = msg.toString(Charset.defaultCharset());
//		System.out.println(name);
//		if(name.contains("over")){
//			System.out.println(ctx.channel().getClass() + " " + sw.longTime());
//			System.out.println(msg.toString(Charset.defaultCharset()));
//			System.out.println("total: " + totalCount);
//			System.out.println("rate: " + ((totalCount.get() / 1024) / sw.elapsedTime()));
//			return;
//		}
		User user = new User(name, name);
//		us.addUser(user);
		
		synchronized(users){
		users.add(user);
//		if(users.size() == 1000){
//			us.batchAddUser(users);
//			users.clear();
//		}
		}
		if(count.compareAndSet(100000, 0)){
			System.out.println(ctx.channel().getClass() + " " + sw.longTime());
			System.out.println(msg.toString(Charset.defaultCharset()));
			System.out.println("total: " + totalCount);
			System.out.println("rate: " + ((totalCount.get() / 1024) / sw.elapsedTime()));
			count.set(0);
		}
	}

	

}
