(function (D3) {

	D3.processors = {};
	
	D3.addProcessor = function(act, act_min, processor){
		D3.processors[act + "_" + act_min] = processor;
	};
	
	D3.getProcessor = function(act, act_min){
		return D3.processors[act + "_" + act_min];
	};
	
    D3.Codecs = {
        encoder : {transform: function (e){ return JSON.stringify(e);}},
        decoder : {transform: function (e){
                        var evt = JSON.parse(e);
                        if((typeof evt.type !== 'undefined') && (evt.type === D3.NETWORK_MESSAGE)){
                            evt.type = D3.SESSION_MESSAGE;
                        }
                        return evt;
                    }
                  }
    };

   
    D3.createSession = function (url, config, callback) {
        return new Session(url, config, callback);
    };

    D3.reconnect = function (session, reconnectPolicy, callback) {
        reconnectPolicy(session, callback);
    };

    
    function Session(url, config, callback) {
        var me = this;
       
        var message = getReqConnPacket();
        var ws = connectWebSocket(url);
        var state = 0;// 0=CONNECTING, 1=CONNECTED, 2=NOT CONNECTED, 3=CLOSED
        var loginState = 0;
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
            
            ws.onmessage = function (e) {
				console.log(e.data);
                var evt = D3.Codecs.decoder.transform(e.data);
                if(!evt.act){
                    throw new Error("Event object missing 'type' property.");
                }
				if(evt.act === D3.LOG_IN){
					state = 1;
                }
                if(evt.act === D3.LOG_IN_FAILURE || evt.type === D3.ROOM_JOIN_FAILURE){
                    ws.close();
                }
				if(evt.act === D3.LOG_IN_SUCCESS){
					
                    applyProtocol();
                      
					D3.playerId = evt.tuple;
					loginState = 1;
                    ws.send(D3.makePacketByType(D3.ROOM, D3.ROOM_LIST));
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
            if(state !== 1){
               throw new Error("Session is not in connected state"); 
            }
            //ws.send( me.outCodecChain.transform(evt) ); // <= send JSON/Binary data to socket server
			ws.send(evt);
            return me; // chainable
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
        
        me.setState = function (newState) {state = newState;};
        
        function getLoginPacket(config) {
            return D3.Codecs.encoder.transform(D3.loginPacket(config));
        }

		function getReqConnPacket(config) {
            return D3.reqConnPacket();
        }
        
        function getReconnect(config) {
            if (typeof reconnectKey === 'undefined') throw new Error("Session does not have reconnect key");
            var loginEncoder = (typeof config.loginEncoder === 'undefined') ? D3.Codecs.encoder : config.loginEncoder;
            return loginEncoder.transform(D3.NEvent(D3.RECONNECT, reconnectKey));
        }
        
        function applyProtocol(config) {
            ws.onmessage = protocol;
        }
        
        function protocol(e) {
            var pkt = D3.Codecs.decoder.transform(e.data);
            console.log(pkt);
            dispatch(pkt);
        }
        
        function dispatch(pkt) {
            if (typeof pkt.target === 'undefined') {
            	pkt.target = me;
            }
            if (pkt.act === D3.CLOSED){
                me.onclose(pkt);
            }
            
            var processor = D3.getProcessor(pkt.act, pkt.act_min);
            if(processor){
            	processor(pkt);
            }
            else{
            	throw new Error("no such processor exist");
            }
        }
        
        function doNothing(pkt) {}
    }

    D3.ReconnetPolicies = {
        noReconnect : function (session, callback) { session.close();} ,
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