package org.d3.game;

import org.d3.core.BaseGame;
import org.d3.core.transfer.Module;
import org.d3.net.packet.Packets;


public class Nbxx extends BaseGame{
	
	public Nbxx(){
		super("Nbxx");
		_init();
	};
	
	public Nbxx(String name) {
		super(name);
		_init();
	}

	private void _init(){
		addModule((int)Packets.INFO, Module.INFO_MODULE);
		addModule((int)Packets.SHELL, Module.SHELL_MODULE);
		addModule((int)Packets.MONSTER, Module.MONSTER_MODULE);
		addModule((int)Packets.ROOM, Module.ROOM_MODULE);
		addModule((int)Packets.CHAT, Module.CHAT_MODULE);	
	}

}
