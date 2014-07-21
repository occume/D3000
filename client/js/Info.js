 //右侧信息显示类
var Info = {
	score : 100,
	life : 10,
	mission : 1,
    //塔的图片对象
	towerImg : null,
    //每种塔在右侧的位置信息
	towerPosition : [],
    //已经安装的塔的位置信息
	installTower : {},
	init : function(cxt,img){
		
		this.redraw();
        //画塔
		for(var i = 0;i<5;i++){
			Canvas.drawImg(cxt,img,i*50,0,50,50,25,100+i*70,50,50);
			
			this.towerPosition.push({x:25,y:100+i*70,width:50,height:50});
		}
		//画塔下的描述信息
		Canvas.drawText(cxt,"50(↑50)",25,160,'orange');
		Canvas.drawText(cxt,"75(↑75)",25,230,'orange');
		Canvas.drawText(cxt,"100(↑100)",20,300,'orange');
		Canvas.drawText(cxt,"125(↑125)",20,370,'orange');
		Canvas.drawText(cxt,"150(↑150)",20,440,'orange');
		
		this.towerImg = img;
		this.bindEvent();
	},
    //绑定右侧塔的事件
	bindEvent : function(){
		
		var self = this,info = document.getElementById("info"),
			select = document.getElementById("select"),
			main = Game.canvasList.tower,
			cxt = Game.canvasList.select,
			
			//jd
			oldX, oldY
			;
		//鼠标按下
		info.onmousedown = function(e){

			var x = e.offsetX || e.layerX,
				y = e.offsetY || e.layerY,
				xIndex,yIndex;
            //遍历右侧的塔位置
			for(var i=0;i<self.towerPosition.length;i++){
                //点击的是塔
				if(T.pointInRect({x:x,y:y},self.towerPosition[i])){
                    //金钱不够,推出
					if(self.score - TowerType[i]["level_1"].buyIt < 0)break;
					//绑定移动移动事件,也可以说是拖动
					select.onmousemove = function(e){

						x = e.offsetX || e.layerX;
						y = e.offsetY || e.layerY;
						
						xIndex = Math.floor(x / 50);
						yIndex = Math.floor(y / 50);
						
						Canvas.clear(cxt,500,500);
                        //画出塔在左侧区域
						Canvas.drawImg(cxt,self.towerImg,i*50,0,50,50,x-25,y-25,50,50);
						//画出范围,如果当前位置没有塔而且是可放塔的
						if(MapData[xIndex][yIndex] == 0 && !self.installTower[xIndex+"_"+yIndex])Canvas.fillArc(cxt,x,y,TowerType[i]["level_1"].scope,"rgba(25,174,70,0.5)");
						else Canvas.fillArc(cxt,x,y,TowerType[i]["level_1"].scope,"rgba(252,82,7,0.5)");
						//画出塔具体的放置位置
						if(oldX == xIndex && oldY == yIndex){
							
						}
						else{
							oldX = xIndex;
							oldY = yIndex;
							S.send(D3.makePacketByType(D3.INFO, D3.INFO_MOVE_TOWER, [xIndex, yIndex]));
						}
						
					}
                    //绑定鼠标释放事件,就是拖动结束
					select.onmouseup = function(e){
						
						Canvas.clear(cxt,500,500);
						//此位置可以放塔
						if(MapData[xIndex][yIndex] == 0 && !self.installTower[xIndex+"_"+yIndex]){
                            //新增一个塔
							S.send(D3.makePacketByType(D3.INFO, D3.INFO_BUILD_TOWER, [xIndex, yIndex, i]));
						}
						//取消绑定
						this.onmousemove = null;
						this.onmouseup = null;
					}
					
					break;
				}
			}

		}
		//如果鼠标释放的位置还在左侧,则取消此次操作
		info.onmouseup = function(){
			
			Canvas.clear(cxt,500,500);
			
			select.onmousemove = null;
			select.onmousedown = null;
		}
	},
	processors: {
		"1": function(packet){
			var xIndex = packet.tuple[0],
				yIndex = packet.tuple[1];
			Canvas.drawRect(Game.canvasList.select, xIndex*50,yIndex*50,50,50,'black');
		},
		"2": function(packet){
		console.log(packet);
			var xIndex = packet.tuple[0],
				yIndex = packet.tuple[1],
				i = packet.tuple[2];
			var img = document.getElementById("tower_img");
			var tower = new Tower(Game.canvasList.tower, img, i, xIndex*50,yIndex*50,50,50, packet.from);
			tower.draw();
			//标记当前位置有塔
			Info.installTower[xIndex+"_"+yIndex] = i+"";
			//加入塔的列表中
			Game.towerList.push(tower);
			//更新金钱
			console.log(D3.from + ":" + packet.from);
			if(D3.from == packet.from)
				Info.updateScore(TowerType[i]["level_1"].buyIt * -1);
		}
	},
    //更新金钱
	updateScore : function(score, playerId){
		if(D3.from == playerId){
			this.score += score;
			this.redraw();
		}
	},
    //更新剩余生命
	updateLife : function(){
		
		this.life -= 1;
		
		this.redraw();
		
		if(this.life <= 0){
			Game.over();
		}
	},
    //更新波数
	updateMission : function(){
		
		this.mission += 1;
		
		this.redraw();
		
	},
    //重画
	redraw : function(){
	
		Canvas.clear(Game.canvasList.info,100,100);
	
		Canvas.drawText(Game.canvasList.info,"金钱:"+this.score,20,30,"red");
		Canvas.drawText(Game.canvasList.info,"第"+this.mission+"波",20,60,"red");
		Canvas.drawText(Game.canvasList.info,"剩余:"+this.life,20,90,"red");
	},
    //画出塔的攻击范围以及升级等信息
	drawScope : function(tower){
		
		var select = Game.canvasList.select;
		
		Canvas.clear(select,500,500);
		
		Canvas.fillArc(select,tower.x+25,tower.y+25,TowerType[tower.type]["level_"+tower.level].scope,"rgba(25,174,70,0.5)");
		
		if(tower.level < 3)Canvas.drawImg(select,Game.imgList.btn_img,0,0,20,20,tower.x,tower.y,20,20);
		
		Canvas.drawImg(select,Game.imgList.btn_img,20,0,20,20,tower.x+30,tower.y+30,20,20);
	},
    //升级或卖掉
	upgradeOrSell : function(x,y){
		
		var tower = Game.nowSelectTower;
		//升级
		if(tower.level < 3 && T.pointInRect({x:x,y:y},{x:tower.x,y:tower.y,width:20,height:20})){
			
			if(this.score - TowerType[tower.type]["level_"+(tower.level+1)].buyIt < 0)return false;
			
			tower.level += 1;
			
			this.updateScore(TowerType[tower.type]["level_"+tower.level].buyIt * -1);
			
			this.drawScope(tower);
			//update
		}
        //卖掉
		else if(T.pointInRect({x:x,y:y},{x:tower.x+30,y:tower.y+30,width:20,height:20})){
			
			var money = Math.floor((tower.level * TowerType[tower.type]["level_1"].buyIt)/2);
			
			this.updateScore(money);
			
			delete this.installTower[Math.floor(tower.x/50)+"_"+Math.floor(tower.y/50)];
			
			Game.towerList.remove(tower);
			
			Canvas.clearRect(Game.canvasList.tower,tower.x,tower.y,tower.width,tower.height);
			
			Canvas.clear(Game.canvasList.select,500,500);
			
			tower = null;
			//sell
		}
	}
}