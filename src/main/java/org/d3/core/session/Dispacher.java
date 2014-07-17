package org.d3.core.session;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;
import org.d3.core.service.RoomService;
import org.d3.core.util.AStarTools;
import org.d3.core.util.Point;
import org.d3.game.map.MapUtil;
import org.testng.collections.Lists;

import com.google.common.collect.Maps;

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
				Packet pkt1 = Packets.newPacket(Packets.ROOM_LIST, rooms);
				ps.sendMessage(pkt1);
			}
		});
		
		processers.put((int)Packets.GAME_ROOM_JOIN, new Processer() {
			public void process(Packet pkt) {
				Map<String, String> rstMap = (Map<String, String>) pkt.getTuple();
				String id = rstMap.get("id");
				Room room = roomService.getRoomById(id);
				
				Packet ret = null;
				if(room.addSession(ps)){
					ps.setRoom(room);
					ret = Packets.newPacket(Packets.GAME_ROOM_JOIN_SUCCESS, ps);
					room.broadcast(ret);
					room.broadcast(Packets.newPacket(Packets.START, null));
					room.broadcast(Packets.newPacket(Packets.MAP_DATA, MapUtil.getDefaultMap()));
				}
				else{
					ret = Packets.newPacket(Packets.GAME_ROOM_JOIN_FAILURE, null);
					ps.sendMessage(ret);
				}
			}
		});
		
		processers.put((int)Packets.SEEK_PAHT, new Processer(){
			public void process(Packet pkt) {
				Map<String, String> rstMap = (Map<String, String>) pkt.getTuple();
				String start = rstMap.get("start");
				String end = rstMap.get("end");
				
				int x1 = Integer.valueOf(start.split("_")[0]);
				int y1 = Integer.valueOf(start.split("_")[1]);
				int x2 = Integer.valueOf(end.split("_")[0]);
				int y2 = Integer.valueOf(end.split("_")[1]);
				
				List<Point> ret = AStarTools.searchs(x1, y1, x2, y2);
				Packet resp = Packets.newPacket(Packets.SEEK_PAHT, getSeekPoint(ret));
				System.out.println(ret);
				
				ps.getRoom().broadcast(resp);
			}
		});
		
		processers.put((int)Packets.SELECT_ELEM, new Processer(){
			public void process(Packet pkt) {
				
				String tile = pkt.getTuple().toString();
				Packet resp = Packets.newPacket(Packets.SELECT_ELEM, tile);
				ps.getRoom().broadcast(resp);
				
			}
		});
		
		processers.put((int)Packets.HEART_BEAT, new Processer(){
			public void process(Packet pkt) {
				ps.setLastAccessTime(System.currentTimeMillis());
			}
		});
		
	}
	
	private List<String> getSeekPoint(List<Point> points){
		if(points == null){
			return null;
		}
		List<String> ret = Lists.newArrayList();
		for(Point p: points){
			ret.add(p.getX() + "_" + p.getY());
		}
		return ret;
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
