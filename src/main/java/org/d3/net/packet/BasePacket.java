package org.d3.net.packet;

import java.io.Serializable;

public class BasePacket implements Packet, Serializable{

	private int 		cid;
	private int 		act;
	private	int			act_min;
	private String		from;
	private String 		vs;
	private String 		target;
	private Object		tuple;
	private long 		timeStamp;
	
	public BasePacket(){}
	
	public BasePacket(int cid, int act, String vs, Object tuple) {
		super();
		this.cid = cid;
		this.act = act;
		this.vs = vs;
		this.tuple = tuple;
		this.timeStamp = System.currentTimeMillis();
	}
	
	public BasePacket(int cid, int act, int act_min, String from, String vs, Object tuple) {
		super();
		this.cid = cid;
		this.act = act;
		this.act_min = act_min;
		this.from = from;
		this.vs = vs;
		this.tuple = tuple;
		this.timeStamp = System.currentTimeMillis();
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public int getAct_min() {
		return act_min;
	}

	public void setAct_min(int act_min) {
		this.act_min = act_min;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getVs() {
		return vs;
	}

	public void setVs(String vs) {
		this.vs = vs;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Object getTuple() {
		return tuple;
	}

	public void setTuple(Object tuple) {
		this.tuple = tuple;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "BasePacket [cid=" + cid + ", act=" + act + ", vs=" + vs
				+ ", source=" + tuple + ", timeStamp=" + timeStamp + "]";
	}
	
	private static final long serialVersionUID = -38147247115869567L;
	
}
