package org.d3.core.session;

import java.util.concurrent.atomic.AtomicInteger;

public class Player {
	
	private static AtomicInteger idx = new AtomicInteger(1);
	
	private int id;
	private String name;
	private String color;
	
	public Player(){};
	
	public Player(String name){
		this.id = idx.getAndIncrement();
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
