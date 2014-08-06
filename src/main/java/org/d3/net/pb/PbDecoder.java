package org.d3.net.pb;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.net.pb.Example;
import org.d3.net.pb.Example.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class PbDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

	@Autowired
	private ObjectMapper jackson;
	@Override
	protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg,
			List<Object> out) throws Exception {
//		out.add(Unpooled.copiedBuffer(msg.content()));
		ByteBuf content = msg.content();
		
		byte[] data = new byte[content.capacity()];
		for(int i = 0; i < data.length; i++){
			data[i] = content.getByte(i);
		}
		
		Message m = Example.Message.parseFrom(data);
		System.out.println(m.getText());
		System.out.println(msg);
	}

}
