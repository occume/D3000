package org.d3.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.d3.server.Server;
import org.d3.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TcpServer implements Server {

	private static final Logger LOG = LoggerFactory.getLogger(TcpServer.class);
	
//	private Config config;
	
	private ServerBootstrap b;
	
	public static final ChannelGroup ALL_CHANNELS = new DefaultChannelGroup("D3-CHANNELS", GlobalEventExecutor.INSTANCE);
	
	public void start()
	{
		
		try {
			
			NioEventLoopGroup boss = new NioEventLoopGroup(2, new NamedThreadFactory("D3-BOSS"));
			NioEventLoopGroup worker = new NioEventLoopGroup(2, new NamedThreadFactory("D3-WORKER"));
			
			b = new ServerBootstrap();
			b.group(boss, worker)
			 .channel(NioServerSocketChannel.class)
			 .handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
				}
			})
			 .childHandler(new TcpServerChannelInitializer())
			 .option(ChannelOption.SO_BACKLOG, 128)
			 .option(ChannelOption.TCP_NODELAY, true)
//			 .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//			 .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			 .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
			
			Channel serverChannel = b.bind(10086).sync().channel();
			ALL_CHANNELS.add(serverChannel);
			
		} catch (InterruptedException e) {
			LOG.error("Server not start");
		}
		
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
