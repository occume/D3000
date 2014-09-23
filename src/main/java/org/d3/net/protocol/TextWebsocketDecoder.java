package org.d3.net.protocol;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.net.packet.BasePacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class TextWebsocketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

	@Autowired
	private ObjectMapper jackson;
	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg,
			List<Object> out) throws Exception {
		String data = msg.text();
//		System.out.println(data);
		BasePacket pkt = jackson.readValue(data, BasePacket.class);
		out.add(pkt);
	}

}
