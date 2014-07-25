;
(function(D3){
	
	jQuery.fn.aPosition = function() {
	    thisLeft = this.offset().left;
	    thisTop = this.offset().top;
	    thisParent = this.parent();

	    parentLeft = thisParent.offset().left;
	    parentTop = thisParent.offset().top;

	    return {
	        left: thisLeft-parentLeft,
	        top: thisTop-parentTop
	    };
	};
	
	var BottomInfo = {
		init: function(){
			var 
				container = $("#paper4"),
				paperLeft = container.aPosition().left,
				paperTop = container.aPosition().top;
			var bottomInfoPaper = D3.Game.getPaper("bottomInfo"),
				turretPaper = D3.Game.getPaper("turret"),
				turretModelSet = bottomInfoPaper.set(),
				shadowSet = bottomInfoPaper.set();
			
			var T1 = bottomInfoPaper.newImage("img/baicai.png", 0, 500, 60, 60),
				_T1 = bottomInfoPaper.newImage("img/baicai.png", 0, 0, 50, 50).hide(),
				T2 = bottomInfoPaper.newImage("img/blower.png", 100, 500, 70, 60),
				_T2 = bottomInfoPaper.newImage("img/blower.png", 0, 0, 70, 60).hide(),
				tempRect = null,
				halfT1 = _T1.getBBox().width / 2;
			
			T1.data("shadow", _T1);
			T2.data("shadow", _T2);
			
			turretModelSet.push(T1);
			turretModelSet.push(T2);
			
			_T1.data("type", 0);
			_T2.data("type", 1);
			
			shadowSet.push(_T1);
			shadowSet.push(_T2);
			
			function getCoordinate(e){
				e = e || event;
				var px = e.pageX,
					py = e.pageY; 
//				console.log(px);
				var 
					toX = px - paperLeft;
					toY = py - paperTop - 100;
				return {x: toX, y: toY};
			}
			
			turretModelSet.click(function(e){
//				console.log("turret model click: " + paperLeft);
				var coord = getCoordinate(e);
				this.data("shadow")
					.transform("t" + (coord.x - halfT1) + "," + (coord.y - halfT1))
					.show()
					.attr({opacity: 0.7});
				console.log(coord);
				moveBind(this);
			});
			
			var oldX = -1, oldY = -1;
			var mapData = D3.Map.getCurrMap();
			
			function moveBind(model){
				var shadow = model.data("shadow");
				$(document).bind("mousemove", function(e){
					var coord = getCoordinate(e);
//					_T1.attr({transform: "t" + coord.x + "," + coord.y});
					shadow.transform("t" + (coord.x - halfT1) + "," + (coord.y - halfT1));
					
					var x = Math.floor(coord.x / 50),
						y = Math.floor(coord.y / 50);
						
					if(x == oldX && y == oldY){
						
					}
					else{
						oldX = x;
						oldY = y;
						if(!mapData[x]){
							return;
						}
						
						if(tempRect)
							tempRect.remove();
						
						if(mapData[x][y] == 0){
							tempRect = turretPaper.newRect((x * 50), (y * 50), 50, 50, 0);
						}
						else if(mapData[x][y] == 1){
							tempRect = turretPaper.newRect((x * 50), (y * 50), 50, 50, 0).attr({fill: "red"});
						}
					}
				});
			}
			
			shadowSet.click(function(e){
				var coord = getCoordinate(e);
				
				var x = Math.floor(coord.x / 50),
					y = Math.floor(coord.y / 50);
					
				if(!mapData[x]){
					clearShadow(this);
					return;
				}
				if(mapData[x][y] == 0){
//					D3.Turret.create(x * 50, y *50, this.data("type")).draw();
					D3.session.send(D3.makePacketByType(D3.INFO, D3.INFO_BUILD_TURRET, [x * 50, y *50, this.data("type")]));
					clearShadow(this);
				}
				else if(mapData[x][y] == 1){
					SBOX.systemMessage("不能创建");
				}
				else{
					clearShadow(this);
				}
			});
			
			function clearShadow(shadow){
				if(tempRect)
					tempRect.remove();
				shadow.hide();
				$(document).unbind("mousemove");
			}
		}
	};
	
	// net message 处理
	D3.addProcessor(D3.INFO, D3.INFO_BUILD_TURRET,
	/**
	 * 创建炮塔
	 * x, y 坐标
	 * typeIdx 炮塔类型
	 */
	function(pkt){
		var tuple = pkt.tuple,
			x = tuple[0],
			y = tuple[1],
			typeIdx = tuple[2];
		var player = D3.Player.getPlayer(pkt.from);
		D3.Turret.create(x, y, typeIdx, player).draw();
	});
	
	D3.BottomInfo = BottomInfo;
	
}( window.D3 = window.D3 || {}));