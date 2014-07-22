package org.d3.core.packet;

public interface Packet {
	public String getCid();

	public void setCid(String cid);

	public int getAct();

	public void setAct(int act);
	
	public int getAct_min();

	public void setAct_min(int act_min);

	public String getVs() ;

	public long getTimeStamp();

	public void setTimeStamp(long timeStamp);

	public void setVs(String vs);

	public Object getTuple();

	public void setTuple(Object tuple);
}
