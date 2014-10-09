package org.d3.net.protocol;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.d3.net.packet.BasePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger LOG = LoggerFactory.getLogger(TextWebsocketDecoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg,
			List<Object> out){
		String data = msg.text();
		BasePacket pkt = null;
		try {
			pkt = jackson.readValue(data, BasePacket.class);
		} catch (Exception e) {
			LOG.error("error: " + e.getMessage());
		}
		out.add(pkt);
	}

}
