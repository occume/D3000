package org.d3.core.transfer;

import java.util.concurrent.atomic.AtomicInteger;

import org.d3.net.session.Session;

public class Player {
	
	private static AtomicInteger idx = new AtomicInteger(1);
	
	private int id;
	private int seat;
	private String name;
	private String sid;
	private boolean ready;
	
	public Player(){};
	
	public Player(Session session, String name, String sid){
		this.id = idx.getAndIncrement();
		this.name = name;
		this.sid = sid;
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

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
}
