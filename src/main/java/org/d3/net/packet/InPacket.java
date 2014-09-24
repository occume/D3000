package org.d3.net.packet;

public class InPacket extends BasePacket {

	public InPacket() {
		super();
	}

	public InPacket(int modlue, int cmd, Object tuple) {
		super(modlue, cmd, tuple);
	}

}
