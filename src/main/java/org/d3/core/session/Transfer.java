package org.d3.core.session;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;
import org.d3.core.service.RoomService;

public class Transfer extends NonBlockingBladeBase {

	public Transfer() throws Exception {
		super();
	}
	
	public AOp<Packet> onMessage(final Packet pkt){
		return new AOp<Packet>("transfer-onMessage", getReactor()) {

			public void processAsyncOperation(
					AsyncRequestImpl _asyncRequestImpl,
					AsyncResponseProcessor<Packet> _asyncResponseProcessor)
					throws Exception {
				Thread.sleep(1000000);
				System.out.println(Thread.currentThread().getName());
//				System.out.println(pkt);
//				Session s = SessionManager.getInstance().anySession();
				RoomService rs = (RoomService) D3Context.getBean("roomService");
				Room room = rs.getRoomById("0001");
				System.out.println("===" + room);
				Packet pkt = Packets.newPacket(Packets.RECONNECT, null);
				room.broadcast(pkt);
				_asyncResponseProcessor.processAsyncResponse(null);
			}
		};
	}

}
