package org.d3.test;

import java.util.concurrent.TimeUnit;

import org.d3.std.Stopwatch;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class LoadTest {
	
	public static void main(String...strings){
	
		for(int i = 0; i < 10; i++){
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
		 .option(ChannelOption.TCP_NODELAY, false)
		 .option(ChannelOption.SO_SNDBUF, 1024)
		 .handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				
//				ch.pipeline().addLast(new CometorClientHandler());
				ch.pipeline().addLast(new StringEncoder());
			}
			
		});
		
		Channel c = b.connect("127.0.0.1", 8080).sync().channel();
		 final Stopwatch sw = Stopwatch.newStopwatch();
		for(int i = 0; i < 20000; i++){
//			if(c != null && c.isActive())
//				c.writeAndFlush("hi, this is a test!");
//			
//			TimeUnit.MILLISECONDS.sleep(100000);
			if(c.isWritable()){
				ChannelFuture f = c.writeAndFlush("hi, this is a msg " + i);
//				if(i == 9999){
//				f.addListener(new FutureListener<Object>() {
//
//					public void operationComplete(Future future)
//							throws Exception {
//						future.get();
////						System.out.println(sw.longTime());
//					}
//				});
//				}
			}
			Thread.sleep(1);
		}
	}
	
}
