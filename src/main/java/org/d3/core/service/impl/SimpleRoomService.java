package org.d3.core.service.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.d3.Room;
import org.d3.core.service.RoomService;
import org.d3.game.NbxxRoom;
import org.d3.module.chat.ChatRoom;

public class SimpleRoomService implements RoomService {

	private ConcurrentHashMap<Integer, ChatRoom> rooms = new ConcurrentHashMap<Integer, ChatRoom>();
	
	public void createRoom(int id, String name){
		ChatRoom room = new ChatRoom(id, name);
		rooms.put(id, room);
	}
	
	public void createRoom(){
		
	}
	
	public ChatRoom getRoomById(int id) {
		return rooms.get(id);
	}

	public Collection<ChatRoom> getRoomList() {
		return rooms.values();
	}

}
