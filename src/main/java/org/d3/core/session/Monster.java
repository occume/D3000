package org.d3.core.session;

import java.util.concurrent.atomic.AtomicInteger;

public class Monster {
	
	private static AtomicInteger idx = new AtomicInteger(1);
	private String id;
	private int maxLife = 50;
	private int currLife = 50;
	
	public Monster(){
		this.id = "m_" + idx.getAndIncrement();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	public int getCurrLife() {
		return currLife;
	}
	public void setCurrLife(int currLife) {
		this.currLife = currLife;
	}
	
}
