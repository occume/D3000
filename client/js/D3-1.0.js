(function (D3) {
    "use strict";

    // Event code Constants
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

    D3.GAME_LIST = 0x10;
    D3.ROOM_LIST = 0x12;
    D3.GAME_ROOM_JOIN = 0x14;
    D3.GAME_ROOM_LEAVE = 0x16;
    D3.GAME_ROOM_JOIN_SUCCESS = 0x18;
    D3.GAME_ROOM_JOIN_FAILURE = 0x19;

	D3.SEEK_PAHT = 0x31;
	D3.SELECT_ELEM = 0x33;
	D3.PREPARE_GAME = 0x35;

    //Event sent from server to client to start message sending from client to server.
    D3.START = 0x1a;
    // Event sent from server to client to stop messages from being sent to server.
    D3.STOP = 0x1b;
    // Incoming data from server or from another session.
    D3.SESSION_MESSAGE = 0x1c;
    // This event is used to send data from the current machine to remote server
    D3.NETWORK_MESSAGE = 0x1d;
    D3.CHANGE_ATTRIBUTE = 0x20;
    D3.DISCONNECT = 0x22;// Use this one for handling close event of ws.
    D3.EXCEPTION = 0x24;
	D3.CLOSED = 0x22;
	
	D3.INFO = 0x40;
	D3.INFO_MOVE_TOWER = 0x01;
	D3.INFO_BUILD_TOWER = 0x02;
	
	D3.GAME = 0x50;
	
	D3.BULLET = 0x60;
	D3.BULLET_HIT_MONSTER = 0x01;
	
	D3.MONSTER = 0x70;
	D3.MONSTER_DECREMENT_LIFE = 0x01;
	D3.MONSTER_REMAIN = 0x02;
	D3.MONSTER_OVER = 0x03;
    // Functions
    // Creates a new event object
    D3.makePacket = function (cid, act, act_min, vs, session, tuple) {
        return {
            cid : cid,
            act : act,
			act_min: act_min,
			from: D3.from,
			vs	: vs,
            target: session,
			tuple: tuple,
            timeStamp : new Date().getTime()
        };
    };

	D3.makePacketByType = function (act, act_min, tuple) {
        return D3.Codecs.encoder.transform(D3.makePacket(39600, act, act_min, "1.0.0", null, tuple));
    };

    //Special event creation function to send the json class name to D3ron server.
    D3.CNameEvent = function (className) {
        return {
            type : D3.NETWORK_MESSAGE,
            cName : className,
            timeStamp : new Date().getTime()
        };
    };
    
    // Creates a login event object to login to remote D3ron server
    D3.loginPacket = function (config) {
        return D3.makePacket(config.cid, D3.LOG_IN, 0, config.vs, null, config.tuple);
    };

	D3.reqConnPacket = function () {
        return D3.makePacket(39600, D3.CONNECT, 0, "1.0.0");
    };

    // If using a differnt protocol, then use this codec chain, to decode and encode incoming and outgoing requests. Something like a Chain of Responsibility pattern.
    D3.CodecChain = function () {
        this.chain = [];
    };

    D3.CodecChain.prototype.add = function (func) {
        if (func && typeof (func) === 'function') {
            this.chain.push(func);
        } else {
            throw new Error("Parameter:" + func + " is not of type function.");
        }
        return this;
    };

    D3.CodecChain.prototype.remove = function (func) {
        removeFromArray(this.chain, func);
    };

    D3.CodecChain.prototype.tranform = function (message) {
        var i = 0;
        for(;i<this.chain.length(); i++){
            message = this.chain[i].transform(message);
        }
        return message;
    };

    // Default codes which use JSON to decode and encode messages.
    D3.Codecs = {
        encoder : {transform: function (e){ return JSON.stringify(e)}},
        decoder : {transform: function (e){
                        var evt = JSON.parse(e);
                        if((typeof evt.type !== 'undefined') && (evt.type === D3.NETWORK_MESSAGE)){
                            evt.type = D3.SESSION_MESSAGE;
                        }
                        return evt;
                    }
                  }
    };

    // Generally a client needs only one session to the server. But this function can be used to create more.
    D3.sessionFactory = function (url, config, callback) {
        new Session(url,config,callback);
    };

    D3.reconnect = function (session, reconnectPolicy, callback) {
        reconnectPolicy(session, callback);
    };

    // Used to create a session. Once START event is received from the remote D3ron server then the callback is invoked with the created session.
    function Session(url, config, callback) {
        var me = this;
        var onStart = callback;
        var callbacks = {};
        me.inCodecChain = (typeof config.inCodecChain === 'undefined') ? D3.Codecs.decoder : config.inCodecChain;
        me.outCodecChain = (typeof config.outCodecChain === 'undefined') ? D3.Codecs.encoder : config.outCodecChain;
        var message = getReqConnPacket();
        var ws = connectWebSocket(url);
        var state = 0;// 0=CONNECTING, 1=CONNECTED, 2=NOT CONNECTED, 3=CLOSED
        var reconnectKey;

        function connectWebSocket(url) {
            ws = new WebSocket(url);            
            ws.onopen = function() {
                if (state === 0) {
                    ws.send(message);
                } else if (state === 2) {
                    ws.send(getReconnect(config));
                } else {
                    var evt = D3.NEvent(D3.EXCEPTION,"Cannot reconnect when session state is: " + state);
                    me.onerror(evt);
                    dispatch(D3.EXCEPTION, evt);
                }
            };
        
            // Login to D3ron server when the start event is received the callback will return the session.
            ws.onmessage = function (e) {
				console.log(e);
                var loginDecoder = (typeof config.loginDecoder === 'undefined') ? D3.Codecs.decoder : config.loginDecoder;
                var evt = loginDecoder.transform(e.data);
                if(!evt.act){
                    throw new Error("Event object missing 'type' property.");
                }
				if(evt.act === D3.LOG_IN){
                    ws.send(getLoginPacket(config));
                }
                if(evt.act === D3.LOG_IN_FAILURE || evt.type === D3.GAME_ROOM_JOIN_FAILURE){
                    ws.close();
                }
				if(evt.act === D3.LOG_IN_SUCCESS){
					if (onStart && typeof(onStart) === 'function') {
                        state = 1;
                        applyProtocol(config);
                        onStart(me);
                    }
					D3.from = evt.tuple;
                    ws.send(D3.makePacketByType(D3.ROOM_LIST, 0));
                }
            };

            ws.onclose = function (e) {
                state = 2;  
                dispatch(D3.DISCONNECT, D3.makePacket(D3.DISCONNECT, e, me));
            };

            ws.onerror = function (e) {
                state = 2;
                dispatch(D3.EXCEPTION, D3.makePacket(D3.EXCEPTION, e, me));
            };
            return ws;
        }

        me.onmessage = doNothing;
        me.onerror = doNothing;
        me.onclose = doNothing;

        me.onevent = function (evt) {
            dispatch(evt.type, evt);
        };
        me.on = me.onevent;// alias for onevent

        me.send = function (evt) {
            if(state !== 1 && !((evt.type === D3.RECONNECT) && (state === 2))){
               throw new Error("Session is not in connected state"); 
            }
            //ws.send( me.outCodecChain.transform(evt) ); // <= send JSON/Binary data to socket server
			ws.send(evt);
            return me; // chainable
        };

        me.addHandler = function(eventName, callback) {
            callbacks[eventName] = callbacks[eventName] || [];
            callbacks[eventName].push(callback);
            return me;// chainable
        };
        
        me.removeHandler = function(eventName, handler) {
            removeFromArray(callbacks[eventName], handler);
        };

        me.clearHandlers = function () {
            callbacks = {};
            me.onerror = doNothing;
            me.onmessage = doNothing;
            me.onclose = doNothing;
        };
        
        me.onclose = function () {
			console.log("close");
            state = 3;
            ws.close();
            //dispatch(D3.CLOSED, D3.NEvent(D3.CLOSED));
        };
        
        me.disconnect = function () {
            state = 2;
            ws.close();
        };
        
        me.reconnect = function (callback) {
            if (state !== 2) {
                throw new Error("Session is not in not-connected state. Cannot reconnect now"); 
            }
            onStart = callback;
            ws = connectWebSocket(url);      
        };
        
        me.setState = function (newState) {state = newState};
        
        function dispatch(eventName, evt) {
            if (typeof evt.target === 'undefined') {
                evt.target = me;
            }
            //if (eventName === D3.SESSION_MESSAGE){
                
            //}
            if (eventName === D3.CLOSED){
                me.onclose(evt);
            }
			me.onmessage(evt);
            dispatchToEventHandlers(callbacks[D3.ANY], evt);
            dispatchToEventHandlers(callbacks[eventName], evt);
        }
        
        function dispatchToEventHandlers(chain, evt) {
            if(typeof chain === 'undefined') return; // no callbacks for this event
            for(var i = 0; i < chain.length; i++){
              chain[i]( evt );
            }
        }
        
        function getLoginPacket(config) {
            var loginEncoder = (typeof config.loginEncoder === 'undefined') ? D3.Codecs.encoder : config.loginEncoder;
            return loginEncoder.transform(D3.loginPacket(config));
        }

		function getReqConnPacket(config) {
            var encoder =  D3.Codecs.encoder;
            return encoder.transform(D3.reqConnPacket());
        }
        
        function getReconnect(config) {
            if (typeof reconnectKey === 'undefined') throw new Error("Session does not have reconnect key");
            var loginEncoder = (typeof config.loginEncoder === 'undefined') ? D3.Codecs.encoder : config.loginEncoder;
            return loginEncoder.transform(D3.NEvent(D3.RECONNECT, reconnectKey));
        }
        
        function applyProtocol(config) {
            ws.onmessage = (typeof config.protocol === 'undefined') ? protocol : config.protocol;
        }
        
        function protocol(e) {
            var evt = me.inCodecChain.transform(e.data);
            dispatch(evt.type, evt);
        }
        
        function doNothing(evt) {}
    }

    D3.ReconnetPolicies = {
        noReconnect : function (session, callback) { session.close() } ,
        reconnectOnce : function (session, callback) {
            session.reconnect(callback);
        }
    };

    function removeFromArray(chain, func) {
        if(chain instanceof Array){
            var index = chain.indexOf(func);
            while(index !== -1){
                chain.splice(index,1);
                index = chain.indexOf(func);
            }
        }
    }

}( window.D3 = window.D3 || {}));