package org.d3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

public class SimpleServer {

	public static void main(String[] args) throws Exception {
		
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
			 .channel(NioServerSocketChannel.class)
			 .handler(new ChannelInitializer<ServerSocketChannel>() {

				@Override
				protected void initChannel(ServerSocketChannel ch) throws Exception {
					System.out.println(Thread.currentThread().getName());
				}
				
			})
			 .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new SimpleHandler());
				}
				
			})
			 .option(ChannelOption.SO_BACKLOG, 128)
			 .childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(10085);//.sync();
//			f.channel().closeFuture().sync();
			
		}finally{
//			boss.shutdownGracefully();
//			worker.shutdownGracefully();
		}
		
	}

}
