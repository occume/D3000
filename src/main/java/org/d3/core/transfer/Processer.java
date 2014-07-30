package org.d3.core.transfer;

import org.d3.net.packet.Packet;

public interface Processer {

	public void process(Charactor charactor, Packet pkt);
	
}
