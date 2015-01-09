package org.d3.codec.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MemcachedRequestEncoder extends
		MessageToByteEncoder<MemcachedRequest> {

	/**
	 * In detail that is:
		• Write magic byte
		• Write opcode byte
		• Write key length (2 byte)
		• Write extras length (1 byte)
		• Write data type (1 byte)
		• Write null bytes for reserved bytes (2 bytes)
		• Write total body length ( 4 bytes - 32 bit int)
		• Write opaque ( 4 bytes) - a 32 bit int that is returned in the response
		• Write CAS ( 8 bytes)
		• Write extras (flags and expiry, 4 bytes each) - 8 bytes total
		• Write key
		• Write value
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, MemcachedRequest msg,
			ByteBuf out) throws Exception {
		
		byte[] key = msg.key().getBytes(CharsetUtil.UTF_8);
		byte[] body = msg.body().getBytes(CharsetUtil.UTF_8);
		int bodySize = key.length + body.length + (msg.hasExtras() ? 8 : 0);
		
		out.writeByte(msg.magic());
		out.writeByte(msg.opCode());
		out.writeShort(key.length);
		
		int extraSize = msg.hasExtras() ? 8 : 0;
		out.writeByte(extraSize);
		
		out.writeByte(0);
		out.writeShort(0);
		out.writeInt(bodySize);
		
		out.writeInt(msg.id());
		out.writeLong(msg.cas());
		
		if(msg.hasExtras()){
			out.writeInt(msg.flags());
			out.writeInt(msg.expires());
		}
		
		out.writeBytes(key);
		out.writeBytes(body);
	}

}
