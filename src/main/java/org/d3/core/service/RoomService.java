package org.d3.core.service;

import java.util.Collection;

import org.d3.Room;
import org.d3.module.chat.ChatRoom;

public interface RoomService {
	
	public ChatRoom getRoomById(int id);

	public void createRoom(int id, String name);
	
	public void createRoom();
	
	public Collection<ChatRoom> getRoomList();
}
