;
(function(D3){
	
	
	
	var BottomInfo = {
		init: function(){
			var 
				container = $("#paper4"),
				paperLeft = container.offset().left,
				paperTop = container.offset().top;
			var bottomInfoPaper = D3.Game.getPaper("bottomInfo"),
				turretPaper = D3.Game.getPaper("turret");
			
			var T1 = bottomInfoPaper.newImage("img/baicai.png", 0, 500, 100, 100),
				_T1 = bottomInfoPaper.newImage("img/baicai.png", 0, 0, 50, 50).hide(),
				tempRect = null,
				halfT1 = _T1.getBBox().width / 2;
			
			function getCoordinate(e){
				e = e || event;
				var px = e.pageX,
					py = e.pageY; 
				
				var 
					toX = px - paperLeft,
					toY = py - paperTop;
				return {x: toX, y: toY};
			}
			
			T1.click(function(e){
//				if(!this.data("ACTIVE")){
					this.data("ACTIVE", !0);
					var coord = getCoordinate(e);
					_T1.transform("t" + (coord.x - halfT1) + "," + (coord.y - halfT1)).show().attr({opacity: 0.7});
					moveBind();
//				}
//				else{
//					$(document).unbind("mousemove");
//				}
			});
			
			var oldX = -1, oldY = -1;
			var mapData = D3.Map.getCurrMap();
			
			function moveBind(){
				$(document).bind("mousemove", function(e){
					var coord = getCoordinate(e);
//					_T1.attr({transform: "t" + coord.x + "," + coord.y});
					_T1.transform("t" + (coord.x - halfT1) + "," + (coord.y - halfT1));
					
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
			
			_T1.click(function(e){
				var coord = getCoordinate(e);
				
				var x = Math.floor(coord.x / 50),
					y = Math.floor(coord.y / 50);
					
				if(!mapData[x]){
					clear_T1();
					return;
				}
				if(mapData[x][y] == 0){
					D3.Turret.create(x * 50, y *50).draw();
					clear_T1();
				}
				else if(mapData[x][y] == 1){
					SBOX.systemMessage("不能创建");
				}
				else{
					clear_T1();
				}
			});
			
			function clear_T1(){
				if(tempRect)
					tempRect.remove();
				_T1.hide();
				$(document).unbind("mousemove");
			}
		}
	};
	
	D3.BottomInfo = BottomInfo;
	
}( window.D3 = window.D3 || {}));