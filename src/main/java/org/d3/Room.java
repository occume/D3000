package org.d3;

import java.util.Collection;

import org.d3.game.bean.Player;
import org.d3.net.packet.Packet;

public interface Room {
	
	public void playerPrepare();
	
	public void playerUnPrepare();
	
	public void startGame();
	
	public void stopGame();
	
	public void broadcast(Packet pkt);
	
//	public boolean addSession(PlayerSession session);
//	
//	public void removeSession(PlayerSession session);
	
	
	public Collection<Player> getPlayers();
	
}
