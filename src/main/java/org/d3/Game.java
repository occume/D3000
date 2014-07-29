package org.d3;

public interface Game extends LifeCycle{
	
	String getName();
	
	Room createRoom();
	
}
