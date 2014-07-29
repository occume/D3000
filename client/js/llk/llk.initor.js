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
	
	var Loader = {
			onLoaded: function(callback){
				this.callback = callback;
				return this;
			},
			loadResp: function(){
				
				var me = this;
				var manifest = [
				    
				    {src:"js/common/D3.eh.js", id:"D3-eh-js"},
				    {src:"js/MapData.js", id:"mapdata-js"},
				    {src:"js/lib/jquery.localscroll-1.2.7-min.js", id:"localscroll-js"},
				    {src:"js/lib/nbw-parallax.js", id:"nbw-parallax-js"},
				    {src:"js/lib/scrollTo.js", id:"scrollTo-js"},
				    {src:"js/lib/jOne.js", id:"jOne-js"},
				    {src:"js/D3.net.1.0.js", id:"D3-net-js"},
				    {src:"js/D3.net.packet.1.0.js", id:"D3-net-packet-js"},
				   
				    {src:"js/D3.raphael.1.0.js", id:"D3-raphael-js"},
				   
//				    {src:"js/D3.game.1.0.js", id:"D3-game-js"}
				];

				var respLength = manifest.length, loaded = 0;

				loader = new createjs.LoadQueue(false);
				loader.on("fileload", handleFileLoad);
				loader.on("complete", handleComplete);
				loader.loadManifest(manifest);

				function handleFileLoad(file) {
					loaded++;
					var digit = (loaded / respLength).toFixed(2);
//					Loading.progress(digit);
//					console.log(digit);
				}

				function handleComplete() {
//					Loading.loadingOver();
					if(me.callback)
						me.callback.call();
					console.log("complete");
				}
			}	
		};
	
	var LLK = {
		init : function() {
			Loader.onLoaded(function(){
				D3.cid = 39600;
				D3.session = D3.createSession("ws://localhost:10086/d3socket");
				
				var pkt = D3.loginPacket({username: "ggshop", password: "123123"});
				/**
				 * 登录成功，显示房间列表
				 */
				D3.addProcessor(D3.ROOM, D3.ROOM_LIST, 
				function(pkt){
					console.log("room list");
				});
				
				setTimeout(function(){
					D3.session.send(pkt);
				}, 500);
				
				LLK.run();
			}).loadResp();
//			slideWrapper.scrollTo("#box14", 500);
//			this.run();
		},
		run: function(){
			
			var 
			container = $("#paper4"),
			paperLeft = container.aPosition().left,
			paperTop = container.aPosition().top;
			
			var paper = Paper.create("paper4", 1200, 600),
				tileSet = paper.set(),
				paper1 = Paper.create("paper4", 1200, 600);
			paper.css({
				position: "absolute",
			});
			paper1.css({
				position: "absolute",
			});
			
			var 
				col = 10,
				row = 10,
				mapCellLen = 50;
			var
				i = 0,
				k = 0,
				tile,
				len = cellW = cellH = mapCellLen;
		
			for(; i < col; i++){
				for(; k < row; k++){
//					tileSet.push(paper.newImage("img/brick1.jpg",i * len, k * len, len, len));
					if((i > 0 && i < col - 1) && (k > 0 && k < row - 1)){
						tile = paper.newRect(i * len, k * len, len, len, 0).attr({fill: "#999", transform: "s0.98"});
						tile.data("coord", k + "_" + i);
						tileSet.push(tile);
					}
				}
				k = 0;
			}
			
			function getCoordinate(e){
				e = e || event;
				var px = e.pageX,
					py = e.pageY; 
				var 
					toX = px - paperLeft;
					toY = py - paperTop - 100;
				return {x: toX, y: toY};
			}
			
			var pair = 0,
				passed = [],
				startTile,
				endTile,
				start,
				end;
			
			tileSet.click(function(e){
//				var coord = getCoordinate(e);
				
//				var x = Math.floor(coord.x / 50),
//					y = Math.floor(coord.y / 50);
				var coord = this.data("coord");
				console.log(coord);
				pair++;
				if(pair == 1){
					start = coord;
					startTile = this;
				}
				else if(pair == 2){
					end = coord;
					endTile = this;
					pair = 0;
					var pkt = D3.makePacketByType(D3.SEEK_PAHT, 0, {start: start, end: end, passed: passed});
					D3.session.send(pkt);
				}
				this.animate({transform: "s0.8"}, 200);
			});
			
			D3.addProcessor(D3.SEEK_PAHT, 0, function(pkt){
				
				if(!pkt.tuple){
					setTimeout(function(){
						startTile.animate({transform: "s0.98"}, 200);
						endTile.animate({transform: "s0.98"}, 200);
					}, 1000);
					return;
				}
				
				var path = "";
				$(pkt.tuple).each(function(idx, itm){
					var coord = itm.split("_"),
						x = coord[1] * 50 + 25,
						y = coord[0] * 50 + 25;
//					paper1.newRect(x * 50, y * 50, 50, 50, 0).attr({fill: "#888", transform: "s0.1"});
					
					if(idx == 0){
						path += "M" + x + "," + y;
					}
					else{
						path += "L" + x + "," + y;
					}
				});
				passed.push(start);
				passed.push(end);
				paper1.path(path);
				
				setTimeout(function(){
					startTile.hide();
					endTile.hide();
					paper1.clear();
				}, 4000);
			});
			
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
	LLK.init();
});