 //地图绘制类
var Map = {
	//画出地图
	draw : function(map){
		
		var i,j;
	
		for(i = 0; i < 10;i++){
			
			for(j = 0;j<10;j++){
				//画背景地图
				if(MapData[i][j] == 0)Canvas.drawRect(map,i*50,j*50,50,50,'red');
                //画可以走的路
				else Canvas.fillRect(map,i*50,j*50,50,50,'black');
			}
		}
	}
}