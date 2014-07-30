package org.d3;

import org.d3.core.transfer.Module;

public interface Game extends LifeCycle{
	
	String getName();
	
	Room createRoom();
	
	Module getModule(int key);
}
