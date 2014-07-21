//敌人类

function Enemy(cxt,img,type,x,y,width,height, id){
		
	this.cxt = cxt;
	this.img = img;
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	//敌人类型
	this.type = type;
	//敌人最大血量
	this.maxLife = 50 + type * 100;
    //敌人当前血量
	this.life = 50 + type * 100;
	this.sp = 2;
	//是否活着
	this.islive = true;
	//移动的方向
	this.dir = null;
    //下个移动位置
	this.nextPosition = null;
	//冰冻时间
	this.frozenTime = 0;
	//敌人的序号
	this.num = 0;
	//记录已经走过的位置
	this.hadWalk = {};
	
	this.id = id;
}

Enemy.prototype = {
    //敌人在图片中对应的位置
	enemyMap : [{x:0,y:0},{x:40,y:0},{x:80,y:0},{x:120,y:0},{x:160,y:0},{x:200,y:0},{x:240,y:0},{x:280,y:0},{x:320,y:0},{x:360,y:0},
				{x:400,y:0},{x:440,y:0},{x:480,y:0},{x:520,y:0},{x:560,y:0},{x:600,y:0},{x:640,y:0},{x:680,y:0},{x:720,y:0},{x:760,y:0}],
    //画出敌人
	draw : function(){
        //冰冻中,画出冰冻图
		if(this.frozenTime > 0){
			
			Canvas.drawImg(this.cxt,this.img,this.enemyMap[this.type].x,this.enemyMap[this.type].y+40,this.width,this.height,this.x,this.y,this.width,this.height);
		}
        //画出正常图
		else Canvas.drawImg(this.cxt,this.img,this.enemyMap[this.type].x,this.enemyMap[this.type].y,this.width,this.height,this.x,this.y,this.width,this.height);
		//计算血量百分比
		var persen = Math.floor(this.life / this.maxLife * 100) / 2;
		//画出血量
		Canvas.fillRect(this.cxt,this.x-5,this.y-5,persen,3,"rgba(38,223,116,0.8)");
	},
    //敌人结束
	over : function(flag){
		//敌人是走完全程
		if(flag){
			//更新剩余生命数
			Info.updateLife();
		}
        //敌人是给打死的
		else{
			//更新金额
			Info.updateScore(5);
		}
		
		this.islive = false;
		Game.enemyList.remove(this);
	},
    //减少敌人生命
	reduceLife : function(hurt,effer){
		
		this.life -= hurt;
		//选择子弹效果
		switch(effer.effer){
			
			case "frozen":
				this.frozenTime = effer.num;
				break;
			case "steal":
				Info.updateScore(effer.num);
				break;
			case "kill":
				if(Math.floor(Math.random()*100+1) <= effer.num)this.life = 0;
				break;
			default:
				break;
		}
		//判断生命
		//if(this.life <= 0)this.over();
	},
    //更新敌人信息
	update : function(){
		//超出坐标
		if(this.x >= 500){
			//this.over(true);
			S.send(D3.makePacketByType(D3.MONSTER, D3.MONSTER_OVER, this.id));
			return false;
		}
		
		var xIndex = parseInt(this.x / 50,10),
			yIndex = parseInt(this.y / 50,10);
		//判断是否有下个移动位置信息,或者下哥移动位置信息是否已经走到了
		if(!this.nextPosition || 
			((this.x >= this.nextPosition.x - 5 && this.x <= this.nextPosition.x) && (this.y >= this.nextPosition.y - 5 && this.y <= this.nextPosition.y))
		){
			//走到最右侧
			if(xIndex + 1 >= 10){
				xIndex = -1;
			}
			else{
                //判断往下能否走
				if(MapData[xIndex][yIndex+1] && !this.hadWalk[xIndex+"_"+(yIndex+1)]){
					
					this.dir = "down";
					yIndex += 1;
				}
                //判断往右能否走
				else if(MapData[xIndex+1][yIndex]  && !this.hadWalk[(xIndex+1)+"_"+yIndex]){
					this.dir = "right";
					xIndex += 1;
				}
				else if(MapData[xIndex][yIndex-1] && !this.hadWalk[xIndex+"_"+(yIndex-1)]){
					this.dir = "up";
					yIndex -= 1;
				}
				else if(MapData[xIndex-1][yIndex] && !this.hadWalk[(xIndex-1)+"_"+yIndex]){
					this.dir = "left";
					xIndex -= 1;
				}
			}
			//是否走到最右侧
			if(xIndex == -1){
				this.nextPosition = {x:500,y:yIndex*50+5};
			}
            //设置下个移动位置
			else {
				this.nextPosition = {x:xIndex*50+5,y:yIndex*50+5};
                //记录已经走过的位置
				this.hadWalk[xIndex+"_"+yIndex] = true;
			}
			
		}
		//有冰冻效果,时间减少
		if(this.frozenTime > 0){
			this.sp = 1;
			this.frozenTime -= 20;
		}
		else this.sp = 2;
		
        //移动
		switch(this.dir){
			
			case "down":
				this.y += this.sp;
				break;
			case "up":
				this.y -= this.sp;
				break;
			case "left":
				this.x -= this.sp;
				break;
			case "right":
				this.x += this.sp;
				break;
			default:
				
				break;
		}
		
	}
	
}

Enemy.processors = {
		"1": function(packet){
			var id = packet.tuple[1],
				enemy = Game.getEnemy(id),
				hurt = packet.tuple[2],
				effer = packet.tuple[3];
			
			enemy.life -= hurt;
			//选择子弹效果
			if(!effer){
				return;
			}
			switch(effer.effer){
				
				case "frozen":
					enemy.frozenTime = effer.num;
					break;
				case "steal":
					Info.updateScore(effer.num);
					break;
				case "kill":
					if(Math.floor(Math.random()*100+1) <= effer.num)enemy.life = 0;
					break;
				default:
					break;
			}
			//判断生命
			//if(enemy.life <= 0)enemy.over();
		},
		"3": function(packet){
		
			var id = packet.tuple[1],
				playerId = packet.tuple[0],
				enemy = Game.getEnemy(id);
			//敌人是走完全程
			if(packet.tuple[2] && packet.tuple[2] == "OUT_OF_MAP"){
				//更新剩余生命数
				Info.updateLife();
			}
			//敌人是给打死的
			else{
				//更新金额
				Info.updateScore(5, playerId);
			}
			
			enemy.islive = false;
			Game.enemyList.remove(enemy);
		}
	};

//更新所有敌人信息
function updateEnemy(){
	
	var enemy;
	
	for(var i=0,l=Game.enemyList.length;i<l;i++){
		
		enemy = Game.enemyList[i];
		
		if(!enemy)continue;
		
		enemy.update();
	}
	
}

//画出所有敌人
function drawEnemy(){
	
	var enemy;
	
	for(var i=0,l=Game.enemyList.length;i<l;i++){
		
		enemy = Game.enemyList[i];
		
		if(!enemy)continue;
		
		enemy.draw();
	}
	
}