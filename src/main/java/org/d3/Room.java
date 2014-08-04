package org.d3;

import java.util.Collection;

import org.d3.core.transfer.Charactor;
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
	
	public boolean joinRoom(Charactor charactor);
	
	public void leaveRoom(Charactor charactor);
	
	public Collection<Player> getPlayers();
	
}
