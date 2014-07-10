package org.d3.core.service;

import org.d3.core.session.Room;

public interface RoomService {
	
	public Room getRoomById(String id);

	public void createRoom();
	
}
