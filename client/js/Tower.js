 //塔类
function Tower(cxt,img,type,x,y,width,height, playerId){
		
	this.cxt = cxt;
	this.img = img;
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	//塔的类型
	this.type = type;
	//塔的级别
	this.level = 1;
	//塔的攻击冷却时间
	this.cd = 0;
	this.playerId = playerId;
}
Tower.prototype = {
	//塔的图片位置
	towerMap : [{x:0,y:0},{x:50,y:0},{x:100,y:0},{x:150,y:0},{x:200,y:0}],
    //画塔
	draw : function(){
		
		Canvas.drawImg(this.cxt,this.img,this.towerMap[this.type].x,this.towerMap[this.type].y,this.width,this.height,this.x,this.y,this.width,this.height);
	},
    //更新塔的信息
	update : function(enemyList){
		//判断冷却时间
		if(this.cd > 0){
			this.cd -= 1;
			return false;
		}
		
		var towerInfo = TowerType[this.type]["level_"+this.level],
			canShot = towerInfo.bullet,
			enemy;
		
		this.cd = towerInfo.cd;
		//遍历敌人
		for(var i=0,l=enemyList.length;i<l;i++){
			
			enemy = enemyList[i];
			
			if(!enemy)continue;
			//判断敌人是否在塔的攻击范围内
			if(T.rectInCircle(enemy,{x:this.x+25,y:this.y+25,radius:towerInfo.scope})){
				//可发送的子弹数减少
				canShot -= 1;
				//新增一个子弹,加入到子弹列表中
				Game.bulletList.push(new Bullet(Game.canvasList.main,Game.imgList.bullet_img,this.type,enemy,this.level,this.x+20,this.y+20,5, this));
				
				//如果可用子弹没了,退出
				if(canShot <= 0)break;
			}
		}
		
	}
}


var TowerType = [
	{
		level_1:{
			scope:100,buyIt:50,bullet:1,cd:20
		},
		level_2:{
			scope:110,buyIt:50,bullet:1,cd:18
		},
		level_3:{
			scope:120,buyIt:50,bullet:1,cd:15
		}
	},
	{
		level_1:{
			scope:120,buyIt:75,bullet:1,cd:18
		},
		level_2:{
			scope:130,buyIt:75,bullet:1,cd:15
		},
		level_3:{
			scope:140,buyIt:75,bullet:2,cd:12
		}
	},
	{
		level_1:{
			scope:140,buyIt:100,bullet:3,cd:18
		},
		level_2:{
			scope:150,buyIt:100,bullet:4,cd:15
		},
		level_3:{
			scope:160,buyIt:100,bullet:5,cd:12
		}
	},
	{
		level_1:{
			scope:130,buyIt:125,bullet:1,cd:50
		},
		level_2:{
			scope:140,buyIt:125,bullet:1,cd:40
		},
		level_3:{
			scope:150,buyIt:125,bullet:1,cd:30
		}
	},
	{
		level_1:{
			scope:150,buyIt:150,bullet:1,cd:20
		},
		level_2:{
			scope:160,buyIt:150,bullet:1,cd:15
		},
		level_3:{
			scope:170,buyIt:150,bullet:1,cd:12
		}
	}
]

//更新塔
function updateTower(){
	
	var tower;
	
	for(var i=0,l=Game.towerList.length;i<l;i++){
		
		tower = Game.towerList[i];
		
		if(!tower)continue;
		
		tower.update(Game.enemyList);
	}
	
}
