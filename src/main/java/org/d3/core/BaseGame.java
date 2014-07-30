package org.d3.core;

import java.util.Map;

import org.d3.Game;
import org.d3.LifeCycle;
import org.d3.Room;
import org.d3.core.transfer.Module;
import org.testng.collections.Maps;

public abstract class BaseGame implements Game, LifeCycle{

	private String name;
	
	private Map<Integer, Module>  modules;
	
	public BaseGame(String name){
		this.name = name;
		this.modules = Maps.newHashMap();
	}
	
	protected void addModule(int key, Module value){
		this.modules.put(key, value);
	}
	
	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return name;
	}

	public Room createRoom() {
		// TODO Auto-generated method stub
		return null;
	}

	public Module getModule(int key) {
		return modules.get(key);
	}

}
