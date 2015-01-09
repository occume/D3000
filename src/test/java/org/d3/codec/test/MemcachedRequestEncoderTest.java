package org.d3.codec.test;

import org.d3.std.Printer;
import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;

public class MemcachedRequestEncoderTest {
	@Test
	public void testMemcachedRequestEncoder() {
		
		MemcachedRequest request =new MemcachedRequest(Opcode.SET, "key1", "value1");
		EmbeddedChannel channel = new EmbeddedChannel(new MemcachedRequestEncoder());
		
		Assert.assertTrue(channel.writeOutbound(request));
		
		ByteBuf encoded = (ByteBuf) channel.readOutbound();
		
//		System.out.println(request.magic());
//		System.out.println(request.opCode());
//		System.out.println(encoded.readByte());
//		System.out.println(encoded.readByte());
//		System.out.println(encoded.readShort());
		
		Assert.assertNotNull(encoded);
//		Assert.assertEquals(request.magic(), encoded.readByte());
		encoded.readByte();
		Assert.assertEquals(request.opCode(), encoded.readByte());
		Assert.assertEquals(4, encoded.readShort());
		Assert.assertEquals((byte) 0x08, encoded.readByte());
		Assert.assertEquals((byte) 0, encoded.readByte());
		Assert.assertEquals(0, encoded.readShort());
		Assert.assertEquals(4 + 6 + 8, encoded.readInt());
		Assert.assertEquals(request.id(), encoded.readInt()); 
		Assert.assertEquals(request.cas(), encoded.readLong()); 
		Assert.assertEquals(request.flags(), encoded.readInt()); 
		Assert.assertEquals(request.expires(), encoded.readInt());
		
		byte[] data = new byte[encoded.readableBytes()];
		encoded.readBytes(data);
		
		Assert.assertArrayEquals((request.key() + request.body())
		.getBytes(CharsetUtil.UTF_8), data);
		Assert.assertFalse(encoded.isReadable());
		Assert.assertFalse(channel.finish());
		Assert.assertNull(channel.readInbound());
	}
}
