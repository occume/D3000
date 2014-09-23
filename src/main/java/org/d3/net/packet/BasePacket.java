package org.d3.net.packet;

import java.io.Serializable;

public class BasePacket implements Packet, Serializable{

//	private int 		cid;
	private int 		module;
	private	int			cmd;
//	private String		from;
//	private String 		vs;
//	private String 		target;
	private Object		tuple;
	private long 		timeStamp;
	
	public BasePacket(){}
	
	public BasePacket(int modlue, int cmd, Object tuple) {
		super();
		this.module = modlue;
		this.cmd = cmd;
		this.tuple = tuple;
		this.timeStamp = System.currentTimeMillis();
	}
	
	public int getModule() {
		return module;
	}

	public void setModule(int module) {
		this.module = module;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
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

	private static final long serialVersionUID = -38147247115869567L;
	
}
