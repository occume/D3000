 //子弹类
function Bullet(cxt,img,type,enemy,level,x,y,radius,tower){
		
	this.cxt = cxt;
	this.img = img;
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.radiusAll = radius * 2;
	//子弹类型
	this.type = type;
	
	this.tower = tower;
	
	this.enemy = enemy;
	//子弹级别
	this.level = level;
	
	this.sp = 5;
	//穿刺弹的移动速度
	this.lineAngle = this.getLineAngle();
	if(x > enemy.x+20)this.lineAngle.xsp *= -1;
	if(y > enemy.y+20)this.lineAngle.ysp *= -1;
	
	this.lineEnemy = {};
}
Bullet.prototype = {
	//子弹在图像中的对应
	bulletMap : [{x:0,y:0},{x:10,y:0},{x:20,y:0},{x:30,y:0},{x:40,y:0}],
    //画子弹
	draw : function(){
		
		Canvas.drawImg(this.cxt,this.img,this.bulletMap[this.type].x,this.bulletMap[this.type].y,this.radiusAll,this.radiusAll,this.x,this.y,this.radiusAll,this.radiusAll);
	},
    //更新子弹信息
	update : function(enemyList){
		
		var bulletInfo = BulletType[this.type]["level_"+this.level],
			enemy;
		//判断是否是穿刺子弹
		if(this.type == 3){
			
			this.x += this.lineAngle.xsp;
			this.y += this.lineAngle.ysp;
			
			if(this.y >= 500 || this.y <= 0 || this.x >= 500 || this.x <= 0){
				return this.over();
			}
			//遍历每个敌人
			for(var i=0,l=enemyList.length;i<l;i++){
			
				enemy = enemyList[i];
				
				if(!enemy)continue;
				//如果穿刺过,跳过
				if(this.lineEnemy[enemy.num])continue;
				//判断子弹击中敌人
				if(T.circleInCircle(this,{x:enemy.x+20,y:enemy.y+20,radius:20})){
					
					this.lineHurt(enemy);
                    //将穿刺过的敌人保存
					this.lineEnemy[enemy.num] = true;
				}
			}
		}
        //非穿刺弹
		else{
			//敌人死了,子弹结束
			if(!this.enemy.islive)return this.over();
			//移动的速度
			var sp = this.getLineAngle();
			
			if(this.x > this.enemy.x+20)this.x -= sp.xsp;
			else if(this.x < this.enemy.x +20)this.x += sp.xsp;
			
			if(this.y > this.enemy.y+20)this.y -= sp.ysp;
			else if(this.y < this.enemy.y + 20)this.y += sp.ysp;
			
			if(this.y >= 500 || this.y <= 0 || this.x >= 500 || this.x <= 0){
				return this.over();
			}
			//判断子弹击中敌人
			if(T.circleInCircle(this,{x:this.enemy.x+20,y:this.enemy.y+20,radius:20})){
				
				return this.over(this.enemy);
			}
		}
		
	},
    //获取子弹对于敌人的移动速度
	getLineAngle : function(){
		
		var ydif = Math.abs(this.y-this.enemy.y-15),
			xdif = Math.abs(this.x-this.enemy.x-15),
			xsp,ysp;
			
		if(ydif >= xdif){
			
			ysp = this.sp;
			xsp = Math.floor(this.sp * (xdif / ydif));
		}
		else {
			xsp = this.sp;
			ysp = Math.floor(this.sp * (ydif / xdif));
		}
		
		return {xsp:xsp,ysp:ysp};
	},
    //子弹结束
	over : function(enemy){
		//判断子弹是因为击中敌人而结束
		if(enemy){
            
			var effer = {},bType = BulletType[this.type]["level_"+this.level];
			//设置子弹的效果
			if(bType.forzen){
				
				effer = {effer:"frozen",num:bType.forzen};
			}
			else if(bType.steal){
				
				effer = {effer:"steal",num:bType.steal};
			}
			else if(bType.kill){
				effer = {effer:"kill",num:bType.kill};
			}
			else effer = {effer:"nomal"};
			//较少鸡蛋的生命
			//enemy.reduceLife(bType.hurt,effer);
			console.log(bType.hurt);
			//console.log(this.tower);
			if(this.tower.playerId == D3.from)
			S.send(D3.makePacketByType(D3.BULLET, D3.BULLET_HIT_MONSTER, [this.tower.playerId, enemy.id, bType.hurt, effer]));
		}
		//移除子弹
		Game.bulletList.remove(this);
		//设置穿刺子弹的击中敌人列表为空
		this.lineEnemy = null;
		
		return false;
	},
    //穿刺子弹的伤害敌人
	lineHurt : function(enemy){
		
		enemy.reduceLife(BulletType[this.type]["level_"+this.level].hurt,{effer:"normal"});
	}
}

//子弹类型
var BulletType = [
	
	{
		level_1:{
			hurt:10,steal:0
		},
		level_2:{
			hurt:12,steal:0
		},
		level_3:{
			hurt:12,steal:1
		}
	},
	{
		level_1:{
			hurt:5,forzen:3000
		},
		level_2:{
			hurt:8,forzen:4000
		},
		level_3:{
			hurt:10,forzen:4000
		}
	},
	{
		level_1:{
			hurt:12
		},
		level_2:{
			hurt:15
		},
		level_3:{
			hurt:20
		}
	},
	{
		level_1:{
			hurt:100
		},
		level_2:{
			hurt:200
		},
		level_3:{
			hurt:300
		}
	},
	{
		level_1:{
			hurt:15,kill:5
		},
		level_2:{
			hurt:20,kill:8
		},
		level_3:{
			hurt:30,kill:10
		}
	}
]

//更新所有子弹信息
function updateBullet(){
	
	var bullet;
	
	for(var i=0,l=Game.bulletList.length;i<l;i++){
		
		bullet = Game.bulletList[i];
		
		if(!bullet)continue;
		
		bullet.update(Game.enemyList);
	}
	
}

//画出所有子弹
function drawBullet(){
	
	var bullet;
	
	for(var i=0,l=Game.bulletList.length;i<l;i++){
		
		bullet = Game.bulletList[i];
		
		if(!bullet)continue;
		
		bullet.draw();
	}
	
}