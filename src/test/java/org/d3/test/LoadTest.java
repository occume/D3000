package org.d3.test;

import java.util.concurrent.TimeUnit;

import org.d3.std.Generator;
import org.d3.std.StdArrayIO;
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
		 .option(ChannelOption.TCP_NODELAY, true)
		 .option(ChannelOption.SO_SNDBUF, 1)
		 
		 .handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				
//				ch.pipeline().addLast(new CometorClientHandler());
				ch.pipeline().addLast(new StringEncoder());
			}
			
		});
		
		Channel c = b.connect("127.0.0.1", 10086).sync().channel();
//		c.writeAndFlush("11");
//		Thread.sleep(10);
		 final Stopwatch sw = Stopwatch.newStopwatch();
		for(int i = 0; i < 10001; i++){
//			if(c != null && c.isActive())
				String name = "loadTest" + i;
				byte[] body = name.getBytes();
				int length = body.length;
				c.writeAndFlush(c.alloc().buffer().writeInt(length).writeBytes(body));
//			
//			TimeUnit.MILLISECONDS.sleep(100000);
//			if(c.isWritable()){

//				byte[] body = Generator.byteArray(100000);
//				int length = body.length;
//				c.writeAndFlush(c.alloc().buffer().writeInt(length).writeBytes(body));
//			}
//			Thread.sleep(100);
		}
//		byte[] body = ("over").getBytes();
//		int length = body.length;
//		c.writeAndFlush(c.alloc().buffer().writeInt(length).writeBytes(body));
	}
	
}
