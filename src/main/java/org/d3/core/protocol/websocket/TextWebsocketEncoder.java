package org.d3.core.protocol.websocket;

import java.util.List;

import org.d3.core.packet.Packet;
import org.d3.core.util.ObjectConvert;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
@Sharable
public class TextWebsocketEncoder extends MessageToMessageEncoder<Packet>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg,
			List<Object> out) throws Exception {
		String json = ObjectConvert.Me().ojb2json(msg);
		out.add(new TextWebSocketFrame(json));
	}

}
