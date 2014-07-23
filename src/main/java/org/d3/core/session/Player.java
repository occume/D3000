package org.d3.core.session;

public class Player {
	
	int id;
	String name;
	
	public Player(){};
	
	public Player(String name){
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
