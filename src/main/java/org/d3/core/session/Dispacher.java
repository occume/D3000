package org.d3.core.session;

import java.util.Collection;
import java.util.Map;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;
import org.d3.core.service.RoomService;
import org.testng.collections.Maps;

public class Dispacher extends NonBlockingBladeBase {

	private Map<Integer, Processer>  processers;
	private RoomService roomService;
	private PlayerSession ps;
	
	public Dispacher(PlayerSession ps) throws Exception {
		super();
		this.ps = ps;
		roomService = (RoomService) D3Context.getBean("roomService");
		processers = Maps.newHashMap();
		_init();
	}
	
	private void _init(){
		
		processers.put((int)Packets.ROOM_LIST, new Processer() {
			public void process(Packet pkt) {
				Collection<Room> rooms = roomService.getRoomList();
				Packet pkt1 = Packets.newPacket(Packets.RECONNECT, rooms);
				ps.sendMessage(pkt1);
			}
		});
		
		processers.put((int)Packets.GAME_ROOM_JOIN, new Processer() {
			public void process(Packet pkt) {
				String id = pkt.getTuple();
				Room room = roomService.getRoomById(id);
				
				session.
				
				Collection<Room> rooms = roomService.getRoomList();
				Packet pkt1 = Packets.newPacket(Packets.RECONNECT, rooms);
				ps.sendMessage(pkt1);
			}
		});
		
	}
	
	public AOp<Packet> onMessage(final Packet pkt){
		return new AOp<Packet>("transfer-onMessage", getReactor()) {

			public void processAsyncOperation(
					AsyncRequestImpl _asyncRequestImpl,
					AsyncResponseProcessor<Packet> _asyncResponseProcessor)
					throws Exception {
				
				dispach(pkt);
				
				_asyncResponseProcessor.processAsyncResponse(null);
			}
		};
	}
	
	private void dispach(Packet pkt){
		int act = pkt.getAct();
		
		Processer processer = processers.get(act);
		if(processer != null){
			processer.process(pkt);
		}
		else{
			
		}
//		
////		Thread.sleep(1000000);
//		System.out.println(Thread.currentThread().getName());
////		System.out.println(pkt);
////		Session s = SessionManager.getInstance().anySession();
//		RoomService rs = (RoomService) D3Context.getBean("roomService");
//		Room room = rs.getRoomById("0001");
//		System.out.println("===" + room);
//		Packet pkt = Packets.newPacket(Packets.RECONNECT, null);
//		room.broadcast(pkt);
	}

}
