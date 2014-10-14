package org.d3.net.handler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.d3.net.handler.string.StringHandler;
import org.d3.net.manage.World;
import org.d3.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component
@Scope("prototype")
public class ClientTypeDecoder extends ByteToMessageDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(ClientTypeDecoder.class);
	
	@Resource
	private WSMessageTypeDecoder msgTypeDecoder;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
//		System.out.println(in);
//		for(int i = 0; i < 100; i++){
//			ctx.channel().write(ctx.alloc().buffer().writeByte(i));
//		}
//		ctx.channel().flush();
		int m1 = in.readByte();
		int m2 = in.readByte();
		in.readerIndex(0);
		
		ChannelPipeline pipeline = ctx.pipeline();
		if(ProtocolUtil.isHttp(m1, m2)){
			
			pipeline.addLast("decoder", new HttpRequestDecoder());
	        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
	        pipeline.addLast("encoder", new HttpResponseEncoder());
	        pipeline.addLast("handler", new WebSocketServerProtocolHandler("/d3-server"));
	        pipeline.addLast("msgTypeDecoder", msgTypeDecoder);
	        
	        pipeline.remove(this);
		}
		else{
			pipeline.addLast("string-decoder", new StringDecoder());
			pipeline.addLast("string-encoder", new StringEncoder());
			pipeline.addLast("string-handler", new StringHandler());
			
			World.ALL.add(ctx.channel());
			System.out.println(ctx.channel());
			pipeline.remove(this);
//			LOG.error("invalid protocol");
//			ctx.writeAndFlush(1);
//			ctx.close();
		}
	}
	
	static AtomicInteger clientCount = new AtomicInteger(1);
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.print(clientCount.getAndIncrement() + " >> ");
		System.out.println(ctx.channel().remoteAddress());
		ctx.fireChannelActive();
	}

}
