package org.d3.net.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

@Component
@Sharable
public class ProtobufEncoder extends MessageToMessageEncoder<ByteBuf>{

	private static Logger LOG = LoggerFactory.getLogger(ProtobufEncoder.class);
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		if(LOG.isDebugEnabled()){
			LOG.debug(msg.toString());
		}
		out.add(new BinaryWebSocketFrame(msg));
		
	}

}
