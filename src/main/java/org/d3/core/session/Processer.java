package org.d3.core.session;

import org.d3.net.packet.Packet;

public interface Processer {

	public void process(PlayerSession ps, Packet pkt);
	
}
