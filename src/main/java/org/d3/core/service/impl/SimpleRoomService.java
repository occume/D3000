package org.d3.core.service.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.d3.Room;
import org.d3.core.service.RoomService;
import org.d3.core.session.BaseRoom;

public class SimpleRoomService implements RoomService {

	private ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
	
	public void createRoom(String id, String name){
		Room room = new BaseRoom(id, name);
		rooms.put(id, room);
	}
	
	public void createRoom(){
		
	}
	
	public Room getRoomById(String id) {
		return rooms.get(id);
	}

	public Collection<Room> getRoomList() {
		return rooms.values();
	}

}
