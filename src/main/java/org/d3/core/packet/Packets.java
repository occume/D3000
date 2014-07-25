package org.d3.core.packet;

public class Packets {
	
	public static final byte PROTCOL_VERSION=0x01;
	
	public final static byte ANY = 0x00;
	public final static byte CONNECT = 0x02;
	public static final byte RECONNECT = 0x3;
	public final static byte CONNECT_FAILED = 0x06;
	
	public static final byte LOG_IN = 0x08;
	public static final byte LOG_OUT = 0x0a;
	public static final byte LOG_IN_SUCCESS = 0x0b;
	public static final byte LOG_IN_FAILURE = 0x0c;
	public static final byte LOG_OUT_SUCCESS = 0x0e;
	public static final byte LOG_OUT_FAILURE = 0x0f;
	
	public static final byte CHAT = 0x20;
	public static final byte CHAT_TO_ALL = 0x01;
	public static final byte CHAT_TO_ONE = 0x02;
	
	//public static final byte GAME_LIST = 0x10;
	public static final byte ROOM = 0x30;
	public static final byte ROOM_LIST = 0x01;
	public static final byte ROOM_JOIN = 0x02;
	public static final byte ROOM_LEAVE = 0x03;
	public static final byte ROOM_JOIN_SUCCESS = 0x04;
	public static final byte ROOM_JOIN_FAILURE = 0x05;
	public static final byte ROOM_PREPARE = 0x06;
	public static final byte ROOM_UN_PREPARE = 0x07;
	public static final byte ROOM_START_GAME = 0x08;
	public static final byte ROOM_MAKE_MONSTER = 0x09;
	
	public static final byte ROOM_CREATE = 0x30;
	public static final byte SEEK_PAHT = 0x31;
	public static final byte SELECT_ELEM = 0x33;
	public static final byte MAP_DATA = 0x32;
	public static final byte HEART_BEAT = 0x34;
	
	public static final byte PREPARE_GAME = 0x35;
	public static final byte UN_PREPARE_GAME = 0x36;
	
	public static final byte START = 0x1a;
	public static final byte STOP = 0x1b;
	
	public static final byte INFO = 0x40;
	public static final byte INFO_MOVE_TURRET = 0x01;
	public static final byte INFO_BUILD_TURRET = 0x02;
	
	public static final byte GAME = 0x50;
	public static final byte GAME_MAKE_MONSTER = 0x01;
	
	public static final byte SHELL = 0x60;
	public static final byte SHELL_HIT_MONSTER = 0x01;
	
	public static final byte MONSTER = 0x70;
	public static final byte MONSTER_DECREMENT_LIFE = 0x01;
	public static final byte MONSTER_REMAIN = 0x02;
	public static final byte MONSTER_OVER = 0x03;
	/**
	 * Incoming data from another machine/JVM to this JVM (server or client)
	 */
	public static final byte SESSION_MESSAGE = 0x1c;

	/**
	 * This event is used to send data from the current machine to remote
	 * machines using TCP or UDP transports. It is an out-going event.
	 */
	public static final byte NETWORK_MESSAGE = 0x1d;
//	public static final byte CHANGE_ATTRIBUTE = 0x20;
	
	/**
	 * If a remote connection is disconnected or closed then raise this event.
	 */
	//public static final byte DISCONNECT = 0x22;
	
	/**
	 * A network exception will in turn cause this even to be raised.
	 */
	public static final byte EXCEPTION = 0x24;
	
	public static Packet newPacket(byte act, byte act_min){
		return new BasePacket(1, act, act_min, "", "", null);
	}
	
	public static Packet newPacket(byte act, Object tuple){
		return new BasePacket(1, act, "", tuple);
	}
	
	public static Packet newPacket(byte act, byte act_min, Object tuple){
		return new BasePacket(1, act, act_min, "", "", tuple);
	}
	
	public static Packet newPacket(byte act, byte act_min, String from, Object tuple){
		return new BasePacket(1, act, act_min, from, "", tuple);
	}
	
	public static Packet newLoginPacket(){
		return newPacket(LOG_IN, null);
	}
	
}
