package org.d3.test.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class MonitorClient {

	public static void main(String...strings){
		
		EventLoopGroup worker = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(worker)
		 .channel(NioSocketChannel.class)
		 .option(ChannelOption.SO_KEEPALIVE, true)
		 .handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				
				ch.pipeline().addLast(new StringDecoder());
				ch.pipeline().addLast(new StringEncoder());
				ch.pipeline().addLast(new MonitorClientHandler());
				
			}
			
		});
		
		try {
			b.connect("112.124.115.136", 10086).sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
