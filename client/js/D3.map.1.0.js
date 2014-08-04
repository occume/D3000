;
(function(D3){
	
	var map1 =     [[0,1,0,0,0,0,0,0,0,0],
					[0,1,1,1,1,1,1,1,1,0],
					[0,0,0,0,0,0,0,0,1,0],
					[0,0,0,0,0,0,0,0,1,0],
					[0,0,0,0,0,0,0,0,1,0],
					[0,1,1,1,1,1,1,1,1,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,1,1,1,1,1,0,0,0],
					[0,0,0,0,0,0,1,0,0,0],
					[0,0,0,0,0,0,1,0,0,0],
					[0,0,0,0,0,0,1,0,0,0],
					[0,1,1,1,1,1,1,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,0,0,0,0,0,0,0,0],
					[0,1,1,1,1,1,1,1,0,0],
					[0,0,0,0,0,0,0,1,0,0],
					[0,0,0,0,0,0,0,1,0,0],
					[0,0,0,0,0,0,0,1,0,0],
					[0,0,0,0,0,0,0,1,0,0]];
	
	var maps = [0, map1];
	
	var Map = {
		_mapData: map1,
		setMap: function(index){
			this._mapData = maps[index];
			return this;
		},
		getCurrMap: function(){
			return this._mapData;
		},
		drawMap: function(){
			
			var paper = D3.Game.getPaper("map");
			
			var 
				mapData = this._mapData,
				col = mapData.length,
				row = mapData[0].length,
				mapCellLen = 50;
			var
			i = 0,
			k = 0,
			len = cellW = cellH = mapCellLen,
			c;
		
			for(; i < col; i++){
				for(; k < row; k++){
					c = mapData[i][k];
					if(c == 0){
						temp = paper.newImage("img/brick1.jpg",i * len, k * len, len, len);
	//					walls.push(modle("Rect")(temp, null, {x: k, y: i}));
	//					walls1.push(temp);
					}
				}
				k = 0;
			}
			return this;
		},
	};
	D3.Map = Map;
	
}( window.D3 = window.D3 || {}));