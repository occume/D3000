package org.d3.net.packet;

public interface Packet {
	
	public int getModule();
	
	public void setModule(int module);
	
	public int getCmd();
	
	public void setCmd(int cmd);

	public long getTimeStamp();

	public void setTimeStamp(long timeStamp);

	public Object getTuple();

	public void setTuple(Object tuple);
	
}
