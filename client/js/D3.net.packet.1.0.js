(function (D3) {
    "use strict";

    D3.ANY = 0x00;
    D3.PROTOCOL_VERSION = 0x01;

    D3.CONNECT = 0x02;
    D3.RECONNECT = 0x03;
    D3.CONNECT_FAILED = 0x06;
    D3.LOG_IN = 0x08;
    D3.LOG_OUT = 0x0a;
    D3.LOG_IN_SUCCESS = 0x0b;
    D3.LOG_IN_FAILURE = 0x0c;
    D3.LOG_OUT_SUCCESS = 0x0e;
    D3.LOG_OUT_FAILURE = 0x0f;
    
    D3.CHAT = 0x20;
    D3.CHAT_TO_ALL = 0x01;
    D3.CHAT_TO_ONE = 0x02;

    D3.GAME_LIST = 0x10;
    D3.ROOM = 0x30;
    D3.ROOM_LIST = 0x01;
    D3.ROOM_JOIN = 0x02;
    D3.ROOM_LEAVE = 0x03;
    D3.ROOM_JOIN_SUCCESS = 0x04;
    D3.ROOM_JOIN_FAILURE = 0x05;
    D3.ROOM_PREPARE = 0x06;
    D3.ROOM_UN_PREPARE = 0x07;
    D3.ROOM_START_GAME = 0x08;
    D3.ROOM_MAKE_MONSTER = 0x09;

	D3.SEEK_PAHT = 0x31;
	D3.SELECT_ELEM = 0x33;
	D3.PREPARE_GAME = 0x35;

    D3.START = 0x1a;
    D3.STOP = 0x1b;
    D3.SESSION_MESSAGE = 0x1c;
    
    D3.NETWORK_MESSAGE = 0x1d;
    D3.CHANGE_ATTRIBUTE = 0x20;
    D3.DISCONNECT = 0x22;
    D3.EXCEPTION = 0x24;
	D3.CLOSED = 0x22;
	
	D3.INFO = 0x40;
	D3.INFO_MOVE_TURRET = 0x01;
	D3.INFO_BUILD_TURRET = 0x02;
	
	D3.GAME = 0x50;
	
	D3.SHELL = 0x60;
	D3.SHELL_HIT_MONSTER = 0x01;
	
	D3.MONSTER = 0x70;
	D3.MONSTER_DECREMENT_LIFE = 0x01;
	D3.MONSTER_REMAIN = 0x02;
	D3.MONSTER_OVER = 0x03;

    D3.makePacket = function (act, act_min, session, tuple) {
        return {
            cid : D3.cid,
            act : act,
			act_min: act_min,
			from: D3.from,
			vs	: D3.vs,
            target: session,
			tuple: tuple,
            timeStamp : new Date().getTime()
        };
    };

	D3.makePacketByType = function (act, act_min, tuple) {
        return D3.Codecs.encoder.transform(D3.makePacket(act, act_min, null, tuple));
    };

    D3.loginPacket = function (loginInfo) {
        return D3.Codecs.encoder.transform(D3.makePacket(D3.LOG_IN, 0, null, loginInfo));
    };

	D3.reqConnPacket = function () {
        return D3.Codecs.encoder.transform(D3.makePacket(D3.CONNECT, 0));
    };

}( window.D3 = window.D3 || {}));