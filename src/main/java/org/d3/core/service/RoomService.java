package org.d3.core.service;

import java.util.Collection;

import org.d3.core.session.Room;

public interface RoomService {
	
	public Room getRoomById(String id);

	public void createRoom(String id, String name);
	
	public void createRoom();
	
	public Collection<Room> getRoomList();
}
