package org.d3.core.session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.d3.D3Context;
import org.d3.core.service.RoomService;

import org.d3.core.util.Point;

import org.d3.core.util.astar.LLK;
import org.d3.net.packet.Packet;
import org.d3.net.packet.Packets;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Dispacher extends NonBlockingBladeBase {

	private Map<Integer, Processer>  processers;
	private Map<Integer, Module>  modules;
	private RoomService roomService;
	private PlayerSession ps;
	
	public Dispacher(PlayerSession ps) throws Exception {
		super();
		this.ps = ps;
		roomService = (RoomService) D3Context.getBean("roomService");
		processers = Maps.newHashMap();
		modules = Maps.newHashMap();
		_init();
	}
	
	private void _init(){
		
		modules.put((int)Packets.INFO, Module.INFO_MODULE);
		modules.put((int)Packets.SHELL, Module.SHELL_MODULE);
		modules.put((int)Packets.MONSTER, Module.MONSTER_MODULE);
		modules.put((int)Packets.ROOM, Module.ROOM_MODULE);
		modules.put((int)Packets.CHAT, Module.CHAT_MODULE);	
		
		processers.put((int)Packets.SEEK_PAHT, new Processer(){
			public void process(PlayerSession ps, Packet pkt) {
				Map<String, Object> rstMap = (Map<String, Object>) pkt.getTuple();
				String start = (String) rstMap.get("start");
				String end = (String) rstMap.get("end");
				List<String> passed = (List<String>) rstMap.get("passed");
				
				int x1 = Integer.valueOf(start.split("_")[0]);
				int y1 = Integer.valueOf(start.split("_")[1]);
				int x2 = Integer.valueOf(end.split("_")[0]);
				int y2 = Integer.valueOf(end.split("_")[1]);
				
//				List<Point> ret = AStarTools.searchs(x1, y1, x2, y2, passed);

				List<Point> ret = LLK.search(x1, y1, x2, y2, passed);
				LinkedList<String> lp = getSeekPoint(ret);
				
				if(lp != null){
					lp.addFirst(start);
					lp.addLast(end);
				}
				
				Packet resp = Packets.newPacket(Packets.SEEK_PAHT, lp);
				System.out.println(lp);

				
//				ps.getRoom().broadcast(resp);
				ps.sendMessage(resp);
			}
		});
		
		processers.put((int)Packets.SELECT_ELEM, new Processer(){
			public void process(PlayerSession ps, Packet pkt) {
				
				String tile = pkt.getTuple().toString();
				Packet resp = Packets.newPacket(Packets.SELECT_ELEM, tile);
				ps.getRoom().broadcast(resp);
				
			}
		});
		
		processers.put((int)Packets.HEART_BEAT, new Processer(){
			public void process(PlayerSession ps, Packet pkt) {
				ps.setLastAccessTime(System.currentTimeMillis());
			}
		});
		
	}
	
	private LinkedList<String> getSeekPoint(List<Point> points){
		if(points == null){
			return null;
		}
		
		LinkedList<String> ret = Lists.newLinkedList();
//		Point prev = null;
//		Point next = null;
//		int turn = 0;
		
//		for(int i = 0; i < points.size(); i++){
//			try{
//				prev = points.get(i - 1);
//				next = points.get(i + 1);
//			}
//			catch(Exception e){
//				continue;
//			}
//			if(prev != null && next != null){
//				if(prev.x == next.x || prev.y == next.y);
//				else{
//					turn++;
//					System.out.println("拐弯");
//				}
//			}
//		}

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
			processer.process(ps, pkt);
		}
		else{
			Module unit = modules.get(act);
			unit.dispatch(ps, pkt);
		}
	}

}
