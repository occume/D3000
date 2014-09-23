package org.d3.net.protocol;

import java.util.List;

import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.PbCar;
import org.d3.net.packet.protobuf.PbCar.Car;
import org.d3.net.packet.protobuf.PbCar.Car.Speed;
import org.d3.net.packet.protobuf.PbCar.Car.Vendor;
import org.d3.std.StdArrayIO;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.channel.ChannelHandler.Sharable;

@Component
@Sharable
public class ProtobufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

	@Override
	protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg,
			List<Object> out) throws Exception {
//		out.add(Unpooled.copiedBuffer(msg.content()));
		ByteBuf content = msg.content();
		
		byte[] data = new byte[content.capacity()];
		for(int i = 0; i < data.length; i++){
			data[i] = content.getByte(i);
		}
		
		Game.Login login = Game.Login.parseFrom(data);
		System.out.println(login);
//		Message m = Example.Message.parseFrom(data);
//		long start = System.nanoTime();
//		Car car = PbCar.Car.parseFrom(data);
//		System.out.println(System.nanoTime() - start);
//		
//		Vendor vendor = PbCar.Car.Vendor.newBuilder()
//				.setName("CHINA")
//				.build();
//		
//		Car respCar = PbCar.Car.newBuilder()
//				.setModel("builk")
//				.setVendor(vendor)
//				.setSpeed(Speed.FAST).build();
//		
////		System.out.println(m.getId() + ": " +m.getName());
//		System.out.println(car);
//		System.out.println(msg);
//		StdArrayIO.print(car.toByteArray());
//		ByteBuf resp = Unpooled.copiedBuffer(respCar.toByteArray());
//
//		ctx.writeAndFlush(new BinaryWebSocketFrame(resp));
	}

}
