;
(function(D3){
	
	var paperMapOffsetX = 0,
		paperMapOffsetY = 5;
	
	var Game = {
		papers: {},
		init: function(){
			this.papers.map = Paper.create("paper4", 1200, 600);
			this.papers.main = Paper.create("paper4", 1200, 600);
			this.drapMap();
		},
		getPaper: function(name){
			return this.papers[name];
		},
		drapMap: function(){
			var map = this.papers.map,
				main = this.papers.main;
			map.css({
				position: "absolute",
				top : 0
			});
			main.css({
				position: "absolute",
				top : 0
			});
			
			var col = MapOne.length,
				row = MapOne[0].length,
				mapCellLen = 32;
			var
			i = 0,
			k = 0,
			len = cellW = cellH = mapCellLen,
			c;
		
			for(; i < col; i++){
				for(; k < row; k++){
					c = MapOne[i][k];
					if(c == 0){
						temp = map.newImage("img/brick.png",k * len + paperMapOffsetX, i * len + paperMapOffsetY, len, len);
//						walls.push(modle("Rect")(temp, null, {x: k, y: i}));
//						walls1.push(temp);
					}else{
						//temp = map.newImage("img/grass.png", k * len +paperMapOffsetX, i * len + paperMapOffsetY, len, len);
//						pass.push(modle("Rect")(temp, null, {x: k, y: i}));
//						pass1.push(temp);
					}
				}
				k = 0;
			}
			
		},
		loop: function(){
			D3.Monster.update();
		}
		
	};
	
	D3.Game = Game;
	Game.init();
	Game.loop();
	
}( window.D3 = window.D3 || {}));