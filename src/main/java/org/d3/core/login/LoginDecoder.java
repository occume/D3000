package org.d3.core.login;

import java.util.List;

import javax.annotation.Resource;

import org.d3.core.util.ProtocolUtil;
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

@Component
@Scope("prototype")
public class LoginDecoder extends ByteToMessageDecoder {

	private static final Logger LOG = LoggerFactory.getLogger(LoginDecoder.class);
	
	@Resource
	private WebsocketLoginhandler websocketLoginhandler;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		
		int m1 = in.readByte();
		int m2 = in.readByte();
		in.readerIndex(0);
		
		ChannelPipeline pipeline = ctx.pipeline();
		if(ProtocolUtil.isHttp(m1, m2)){
			
			pipeline.addLast("decoder", new HttpRequestDecoder());
	        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
	        pipeline.addLast("encoder", new HttpResponseEncoder());
	        pipeline.addLast("handler", new WebSocketServerProtocolHandler("/d3socket"));
	        pipeline.addLast("loginhandler", websocketLoginhandler);
	        
	        pipeline.remove(this);
		}
		else{
			LOG.error("invalid protocol");
			ctx.writeAndFlush(1);
			ctx.close();
		}
	}

}
