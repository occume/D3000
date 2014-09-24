package org.d3.module;

import org.d3.net.packet.InPacket;
import org.d3.net.session.Session;

public interface Processor extends Registerable{

	public void process(Session session, InPacket pkt);
	
	public void register();
	
}
