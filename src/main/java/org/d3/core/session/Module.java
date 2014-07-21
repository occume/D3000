package org.d3.core.session;

import java.util.List;
import java.util.Map;

import org.d3.core.packet.Packet;
import org.d3.core.packet.Packets;
import org.testng.collections.Lists;

import com.google.common.collect.Maps;

public interface Module {
	
	void init();
	
	void dispatch(PlayerSession ps, Packet pkt);
	
	abstract class BaseModule implements Module{
		
		protected Map<Integer, Processer>  processers;
		
		{
			processers = Maps.newHashMap();
			init();
		}
		
		public void dispatch(PlayerSession ps, Packet pkt) {
			int act = pkt.getAct_min();
			
			Processer processer = processers.get(act);
			if(processer != null){
				processer.process(ps, pkt);
			}
			else{
				
			}
		}
	}
	
	Module INFO_MODULE = new BaseModule(){
		
		public void init() {
			
			processers.put((int)Packets.INFO_MOVE_TOWER, new Processer() {
				public void process(PlayerSession ps, Packet pkt) {
					
					Packet resp = Packets.newPacket(Packets.INFO,
							Packets.INFO_MOVE_TOWER, "", pkt.getTuple());
					
					ps.getRoom().broadcast(resp);
					
				}
			});
			
			processers.put((int)Packets.INFO_BUILD_TOWER, new Processer() {
				public void process(PlayerSession ps, Packet pkt) {
					
					Packet resp = Packets.newPacket(Packets.INFO, 
							Packets.INFO_BUILD_TOWER, ps.getId(), pkt.getTuple());
					
					ps.getRoom().broadcast(resp);
					
				}
			});
			
		}

		
	};
	
	Module BULLET_MODULE = new BaseModule(){

		public void init() {
			processers = Maps.newHashMap();
			
			processers.put((int)Packets.BULLET_HIT_MONSTER, new Processer() {
				public void process(PlayerSession ps, Packet pkt) {
					try{
					List<Object> list = (List<Object>) pkt.getTuple();
					String playerId = list.get(0).toString();
					String enemyId = list.get(1).toString();
					int hurt = Integer.valueOf(list.get(2).toString());
					
					BaseRoom room = (BaseRoom) ps.getRoom();
					Monster m = room.getMonster(enemyId);
					Packet resp = null;
					
					synchronized (processers) {
						int afterHurt = m.getCurrLife() - hurt;
						m.setCurrLife(afterHurt);
						
						if(afterHurt <= 0){
							resp = Packets.newPacket(Packets.MONSTER, 
									Packets.MONSTER_OVER, playerId, pkt.getTuple());
						}
						else{
							resp = Packets.newPacket(Packets.MONSTER, 
									Packets.MONSTER_DECREMENT_LIFE, playerId, pkt.getTuple());
						}
					}
					
					room.broadcast(resp);
					
					}catch(Throwable e){
						e.printStackTrace();
					};
				}
			});
			
		}

	};
	
	Module MONSTER_MODULE = new BaseModule(){
	
		public void init() {
			processers = Maps.newHashMap();
			
			processers.put((int)Packets.MONSTER_OVER, new Processer() {
				public void process(PlayerSession ps, Packet pkt) {
					
					String enemyId = pkt.getTuple().toString();
					
					List<String> tuple = Lists.newArrayList();
					tuple.add("SITE");
					tuple.add(enemyId);
					tuple.add("OUT_OF_MAP");
					
					Packet resp = Packets.newPacket(Packets.MONSTER, 
							Packets.MONSTER_OVER, ps.getId(), tuple);
					ps.getRoom().broadcast(resp);
				}
			});
			
		}

	};
	
}
