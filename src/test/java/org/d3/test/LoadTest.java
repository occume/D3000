package org.d3.test;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class LoadTest {
	
	public static void main(String...strings){
	
		for(int i = 0; i < 100; i++){
			new Thread(new Runnable() {
				public void run() {
					try {
						new Client().start();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
	}
	
}

class Client{
	
	public void start() throws InterruptedException{
		EventLoopGroup worker = new NioEventLoopGroup(1);
		
		Bootstrap b = new Bootstrap();
		b.group(worker)
		 .channel(NioSocketChannel.class)
		 .option(ChannelOption.SO_KEEPALIVE, true)
		 .handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				
//				ch.pipeline().addLast(new CometorClientHandler());
				ch.pipeline().addLast(new StringEncoder());
			}
			
		});
		
		Channel c = b.connect("10.2.254.205", 8080).sync().channel();
		
		for(;;){
			if(c != null && c.isActive())
				c.writeAndFlush("hi, this is a test!");
			
			TimeUnit.MILLISECONDS.sleep(100000);
		}
	}
	
}
