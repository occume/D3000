package org.d3.net.handler.string;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;

public class D3StringDecoder extends StringDecoder {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("active");
		ctx.fireChannelActive();
	}

}
