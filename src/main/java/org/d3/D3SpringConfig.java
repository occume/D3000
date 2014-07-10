package org.d3;

import org.d3.core.service.RoomService;
import org.d3.core.service.impl.SimpleRoomService;
import org.d3.core.session.Room;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:beans.xml")
public class D3SpringConfig
{
	
//	@Autowired
//	@Qualifier("messageBufferProtocol")
//	private Protocol messageBufferProtocol;

//	public @Bean
//	Game zombieGame()
//	{
//		Game game = new SimpleGame(1, "Zombie");
//		return game;
//	}
//
//	
//	public @Bean(name = "Zombie_Room_Websocket")
//	Room anyRoom()
//	{
//		GameRoomSessionBuilder sessionBuilder = new GameRoomSessionBuilder();
//		sessionBuilder.parentGame(zombieGame())
//				.gameRoomName("Zombie_Room_Websocket")
//				.protocol(webSocketProtocol);
//		Room room = new ZombieRoom(sessionBuilder);
//		room.setDefender(defender());
//		room.setZombie(zombie());
//
//		return room;
//	}
	
	@Bean(name="roomService")
	public RoomService roomService(){
		RoomService rs = new SimpleRoomService();
		rs.createRoom();
		return rs;
	}

}
