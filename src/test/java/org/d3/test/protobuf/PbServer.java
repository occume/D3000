package org.d3.test.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import org.d3.core.NamedThreadFactory;
import org.d3.net.packet.json.TextWebsocketDecoder;
import org.d3.net.packet.protobuf.Example;
import org.d3.net.packet.protobuf.ProtobufDecoder;

public class PbServer {

	public static void main(String[] args) {
		
		try {
			
			NioEventLoopGroup boss = new NioEventLoopGroup(2, new NamedThreadFactory("D3-BOSS"));
			NioEventLoopGroup worker = new NioEventLoopGroup(2, new NamedThreadFactory("D3-WORKER"));
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
			 .channel(NioServerSocketChannel.class)
			 .handler(new ChannelInitializer<ServerSocketChannel>() {

				@Override
				protected void initChannel(ServerSocketChannel ch) throws Exception {
					System.out.println(Thread.currentThread().getName());
					throw new RuntimeException("get out 111");
				}
				
			})
			 .childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						
						p.addLast("decoder", new HttpRequestDecoder());
				        p.addLast("aggregator", new HttpObjectAggregator(65536));
				        p.addLast("encoder", new HttpResponseEncoder());
				        p.addLast("handler", new WebSocketServerProtocolHandler("/ws"));
						
				        p.addLast("protobuf-decoder", new ProtobufDecoder());
//				        
//						p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//						p.addLast("protobufDecoder",
//						        new ProtobufDecoder(Example.Message.getDefaultInstance()));
//						p.addLast("pb", new Pbhandler());
					}
				})
			 .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
			
//			Channel serverChannel = b.bind(8080).sync().channel();
//			Thread.dumpStack();
			b.bind(8080);
			System.out.println("server start");
			
		} catch (Exception e) {
		}
		
	}

}
