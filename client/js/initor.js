;
(function() {

	var jOneUtil = {
		/**
		 * 数组包含函数
		 */
		ArrayContains : function(arr, elem) {
			var len = arr.length, i = 0;
			for (; i < len; i++) {
				if (arr[i] === elem) {
					return true;
				}
			}
			return false;
		},
		/**
		 * 函数中提取html字符串
		 */
		funHtml : function(fn) {
			/**
			 * %20 空格 %0D 换行 %0A 回车 %09 制表
			 */
			var origHtml = fn.toString(), removeOut = /.*{\/(.*)\/}/;
			var html = origHtml.match(removeOut),
			/**
			 * 替换所有 空格， 回车， 换行， 制表
			 */
			htmlNoGap = encodeURIComponent(origHtml).replace(/(%0D|%0A|%09)/g,
					""),
			/**
			 * 还原
			 */
			html2 = decodeURIComponent(htmlNoGap),
			/**
			 * 去除函数格式
			 */
			html3 = html2.match(removeOut)[1],
			/**
			 * 最终html字符串
			 */
			ret = html3.replace(/\*/g, "");
			return ret;
		},
		offset : function(elem) {

			if (!elem)
				return {
					left : 0,
					top : 0
				};
			var top = 0, left = 0;
			if ("getBoundingClientRect" in document.documentElement) {
				// jquery方法
				var box = elem.getBoundingClientRect(), doc = elem.ownerDocument, body = doc.body, docElem = doc.documentElement, clientTop = docElem.clientTop
						|| body.clientTop || 0, clientLeft = docElem.clientLeft
						|| body.clientLeft || 0, top = box.top
						+ (self.pageYOffset || docElem && docElem.scrollTop || body.scrollTop)
						- clientTop, left = box.left
						+ (self.pageXOffset || docElem && docElem.scrollLeft || body.scrollLeft)
						- clientLeft;
			} else {
				do {
					top += elem.offsetTop || 0;
					left += elem.offsetLeft || 0;
					elem = elem.offsetParent;
				} while (elem);
			}
			return {
				left : left,
				top : top
			};
		}
	};

	window.jOneUtil = jOneUtil;

	var pipeline = function() {
		var valves = [], currIndex = 0;

		return {
			add : function(fn) {
				valves.push(fn);
			},
			next : function() {
				if (currIndex < valves.length) {
					valves[currIndex++].apply();
				}
			},
			run : function() {
				valves[currIndex++].apply();
			}
		};
	};

	window.pipeline = pipeline;
})();
$(function() {
	/**
	 * 
	 * 
	 */
	var bodyWidth = $(document.body).width(), 
		boxes = $(".box"), 
		box11 = $("#box11"), 
		box12 = $("#box12"), 
		box13 = $("#box13"), 
		box14 = $("#box14"), 
		menu = $("#menu"), 
		loginRow = $("#box11 .row"), 
		loginForm = loginRow.find(".login-form"), 
		slideWrapper = $("#wrapper1");

	var Loading = {
		init : function() {
			var me = this;
			var mgLeft = (bodyWidth - menu.width()) / 2;
			menu.css({
				marginLeft : mgLeft - 20
			});

			var paper = Raphael("paper1", 1200, 600);
			this.pathCircle = "M100,5  a69,69 0 1,1 -1,0 z";
			this.pathRect = "M10 10h400v200h-400z";

			paper.text(0, 0, "NBXX").attr({
				fill : "#FAA732",
				"font-size" : 120
			}).transform("t400,200");

			$(paper.canvas).css({
				top : 0,
				left : 0
			});

			this.baseProgress = paper.path("M100,5 a100,100 0 1,1 -1,0 z")
					.attr({
						stroke : "#fff",
						"stroke-width" : 5
					}).transform("t700,100");

			paper.customAttributes.along = function(a) {
				return {
					path : me.baseProgress.getSubpath(0, a
							* me.baseProgress.getTotalLength())
				};
			};

			this.loadingProgress = paper.path(0, 0, 100, 100).attr({
				along : 0,
				stroke : "#FAA732",
				"stroke-dasharray" : "--",
				"stroke-width" : 1
			});
			this.percent = paper.text(0, 0, "0%").attr({
				fill : "#FAA732",
				"font-size" : 80
			}).transform("t800,200");

		},
		progress : function(digit) {
			this.loadingProgress.transform("t700,100").attr({
				along : digit
			});
			this.percent.transform("t800,200").attr({
				text : digit * 100 + "%"
			});
		},
		loading : function() {

			var me = this;

			var queue = pipeline();

			queue.add(function() {
				setTimeout(function() {
					me.loadingProgress.transform("t700,100").stop().animate({
						along : 0.2
					}, 3000, function() {
						me.baseProgress.hide();
						queue.next();
					});
				}, 1000);
			});

			queue.add(function() {
				me.onLoadingOver();
				
			});

			queue.run();

		},
		loadingOver : function() {
			var me = this;
			setTimeout(function() {
				me.loadingProgress.animate({
					path : me.pathRect,
					transform : "t600,100"
				}, 400);
			}, 200);
			me.percent.remove();
			me.baseProgress.remove();
			loginRow.animate({
				marginLeft : "10%"
			}, function() {
				loginForm.show();
			});
		}
	};
	
	var Loader = {
		onLoaded: function(callback){
			this.callback = callback;
			return this;
		},
		loadResp: function(){
			
			var me = this;
			var manifest = [
			    {src:"img/btn.png", id:"btn"},
			    {src:"img/bullet.png", id:"bullet"},
			    {src:"img/enemy.png", id:"enemy"},
			    {src:"img/tower.png", id:"tower"},
			    {src:"js/D3.net.1.0.js", id:"D3-net-js"},
			    {src:"js/D3.net.packet.1.0.js", id:"D3-net-packet-js"},
			    {src:"js/lib/jquery.localscroll-1.2.7-min.js", id:"localscroll-js"},
			    {src:"js/lib/nbw-parallax.js", id:"nbw-parallax-js"},
			    {src:"js/lib/scrollTo.js", id:"scrollTo-js"},
			    {src:"js/lib/jOne.js", id:"jOne-js"},
			    {src:"js/D3.chat.1.0.js", id:"D3-chat-js"},
			    {src:"js/D3.raphael.1.0.js", id:"D3-raphael-js"},
			    {src:"js/D3.monster.1.0.js", id:"D3-monster-js"},
			    {src:"js/D3.turret.1.0.js", id:"D3-turret-js"},
			    {src:"js/D3.shell.1.0.js", id:"D3-shell-js"},
			    {src:"js/D3.map.1.0.js", id:"D3-map-js"},
			    {src:"js/D3.info.1.0.js", id:"D3-info-js"},
			    {src:"js/D3.player.1.0.js", id:"D3-player-js"},
			    {src:"js/D3.game.1.0.js", id:"D3-game-js"}
			];

			var respLength = manifest.length, loaded = 0;

			loader = new createjs.LoadQueue(false);
			loader.on("fileload", handleFileLoad);
			loader.on("complete", handleComplete);
			loader.loadManifest(manifest);

			function handleFileLoad(file) {
				loaded++;
				var digit = (loaded / respLength).toFixed(2);
				Loading.progress(digit);
//				console.log(digit);
			}

			function handleComplete() {
				Loading.loadingOver();
				if(me.callback)
					me.callback.call();
				console.log("complete");
			}
		}	
	};
	
	var RoomList = {
		showMe: function(pkt){
			this.data = pkt;
			slideWrapper.scrollTo("#box12", 500);
			this.showRooms();
			this.bind();
		},
		showRooms: function(){
			var me = this;
			var roomList = $("#room-list ul");
			roomList.html("");
			$(this.data.tuple).each(function(idx, itm){
				var room = $("<li>" + itm.name + "</li>");
				room.data("idx", itm.id);
				roomList.append(room);
			});
		},
		send: function(){
			var pkt = D3.makePacketByType(D3.ROOM, D3.ROOM_JOIN, {id: this.roomIdx});
			D3.session.send(pkt);
		},
		bind: function(){
			var rooms = box12.find("li"),
				me = this;

			rooms.live("click", function() {
				var self = $(this);
				rooms.css({
					background : "#FFFFFF"
				});
				self.css({
					background : "#5BB75B"
				});
				me.roomIdx = self.data("idx");
			});
			
			var join = box12.find(".next-step");
			join.click(function() {
				me.send();
				return false;
			});
		}
	};
	
	var Room = function(){
		var _init = function(){
			
				
					D3.addProcessor(D3.ROOM, D3.ROOM_JOIN_SUCCESS, 
					function(pkt){
						Room.showMe(pkt);
					});
					
					D3.addProcessor(D3.ROOM, D3.ROOM_LEAVE, 
					function(pkt){
						Room.showMe(pkt);
					});
					
					D3.addProcessor(D3.ROOM, D3.ROOM_PREPARE, 
					function(pkt){
						//Room.showMe(pkt);
//						console.log(pkt);
						var player = D3.Player.getPlayer(pkt.tuple);
						player.ready4Game();
						$("#" + pkt.tuple).css({border: "2px solid #5cb85c"});
						SBOX.systemMessage(pkt.tuple);
					});
					/**
					 * 开始游戏
					 */
					D3.addProcessor(D3.ROOM, D3.ROOM_START_GAME,
					function(pkt){
						slideWrapper.scrollTo("#box14", 500);
						D3.Game.start();
					});
					/**
					 * 出怪
					 */
					D3.addProcessor(D3.ROOM, D3.ROOM_MAKE_MONSTER,
					function(pkt){
						D3.Monster.create(0, 50, pkt.tuple.id);
					});
			
			_bind();
		};
		var _bind = function(){
			var box13Next = box13.find(".next-step");
			box13Next.click(function() {
//				slideWrapper.scrollTo("#box14", 500);
				var pkt = D3.makePacketByType(D3.ROOM, D3.ROOM_PREPARE, D3.playerId);
				D3.session.send(pkt);
				return false;
			});
		};
		return {
			init: function(){
				_init();
			},
			showMe: function(pkt){
				this.data = pkt;
				slideWrapper.scrollTo("#box13", 500);
				this.showPlayers();
			},
			showPlayers: function(){
				
				var playerList = $("#player-list ul");
				
				playerList.html("");
				D3.Player.clear();
				
				$(this.data.tuple).each(function(idx, itm){
					/**
					 * 显示房间所有玩家
					 */
					var player = $("<li id='"+ itm.sid +"'>" + itm.name + "</li>");
					player.data("idx", itm.id);
					playerList.append(player);
					/**
					 * 增加一个玩家
					 */
					D3.Player.create(itm.seat, itm.name, itm.sid);
				});
				D3.Player.update();
			}
		};
	}();

	var Game = {
		init : function() {
			Loading.init();
			
			Loader.onLoaded(function(){
				D3.cid = jOne.createUUID();
				D3.cid = 39600;
				D3.session = D3.createSession("ws://112.124.115.136:10086/d3socket");
				
//				slideWrapper.scrollTo("#box14", 500);
				
			}).loadResp();
			this.bind();
		},
		bind : function() {
			var me = this;

			$(document).delegate('a.link', 'click', function() {
				var n = $(this).parent().parent().attr("pp");
				var href = $(this).attr('href');
				me.currPos = href;

				if (href.indexOf("file" != 0)) {
					href = href.slice(-6);
				}

				$('#wrapper1').scrollTo(href, 500);
				return false;
			});

			$(window).resize(function() {
				if (me.currPos)
					$('#wrapper1').stop().scrollTo(me.currPos, 500);
			});

			/**
			 * 登录按钮
			 */
			$("#login-btn").click(function() {
				var 
					username = $("#username").val(),
					password = $("#password").val(),
					pkt = D3.loginPacket({username: username, password: password});
				/**
				 * 登录成功，显示房间列表
				 */
				D3.addProcessor(D3.ROOM, D3.ROOM_LIST, 
				function(pkt){
					Room.init();
					RoomList.showMe(pkt);
				});
				
				D3.session.send(pkt);
				return false;
			});

		}
	};
	Game.init();
});