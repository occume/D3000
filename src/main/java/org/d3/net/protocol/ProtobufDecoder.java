package org.d3.net.protocol;

import java.util.List;

import org.d3.net.packet.InPacket;
import org.d3.net.packet.protobuf.Game;
import org.d3.net.packet.protobuf.PbCar;
import org.d3.net.packet.protobuf.PbCar.Car;
import org.d3.net.packet.protobuf.PbCar.Car.Speed;
import org.d3.net.packet.protobuf.PbCar.Car.Vendor;
import org.d3.std.Printer;
import org.d3.std.StdArrayIO;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Bytes;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors.Descriptor;

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

		ByteBuf content = msg.content();
		int module = content.readByte();
		int cmd = content.readByte();

		byte[] data = new byte[content.capacity() - 2];
		content.getBytes(2, data);
//		Printer.printByteArray(data);
		InPacket pkt = new InPacket(module, cmd, data);
		out.add(pkt);

//		List<Descriptor> des = DescriptorProtos.getDescriptor().getMessageTypes();//findMessageTypeByName("Game");
//		System.out.println(des.get(0).getFullName());
//		Descriptor des = Game.getDescriptor().findMessageTypeByName("Game.Login");
//		System.out.println(des.toProto().parseFrom(data));
//		messag
//		Game.Login login = Game.Login.parseFrom(data);
//		System.out.println(login.getPassword());
		
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
