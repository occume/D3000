package org.d3.core.transfer;

//import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
//import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAOp;
//import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.d3.Game;
import org.d3.Room;
import org.d3.game.Nbxx;
import org.d3.game.bean.Player;
import org.d3.net.packet.Packet;
import org.d3.net.session.Session;

public class Charactor {
	
	private String id;
	
	private Session session;
	
	private Player player;
	
	private Game	game;
	
	private Room		room;
	
	private Dispacher 	dispacher;
	
	public Charactor(Session session, Player player){
		this.session = session;
		this.player = player;
		this.id = session.getId();
		try {
			dispacher = new Dispacher(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.game = new Nbxx();
	}
	
//	public void register(RequestBus<Packet> requestBus, NonBlockingReactor reactor){
//		 try {
//			new SubscribeAOp<Packet>(requestBus, reactor) {
//			     @Override
//			     protected void processContent(Packet _content) throws Exception {
//			         sendMessage(_content);
//			     }
//			 }.signal();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public void onMessage(Packet pkt){
//		try {
//			dispacher.onMessage(pkt).call();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void sendMessage(Packet pkt){
//		session.sendMessage(pkt);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void close(){
		if(room != null)
			room.leaveRoom(this);
	}

	public void setLastAccessTime(long currentTimeMillis) {
		session.setLastAccessTime(currentTimeMillis);
	}
	
}
