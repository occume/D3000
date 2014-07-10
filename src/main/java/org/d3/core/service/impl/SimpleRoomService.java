package org.d3.core.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.d3.core.service.RoomService;
import org.d3.core.session.BaseRoom;
import org.d3.core.session.Room;

public class SimpleRoomService implements RoomService {

	private ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
	
	public void createRoom(){
		Room room = new BaseRoom();
		rooms.put("0001", room);
	}
	
//	public void 
	
	public Room getRoomById(String id) {
		return rooms.get(id);
	}

}
