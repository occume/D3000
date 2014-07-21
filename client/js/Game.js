 //游戏数据控制类
var Game = {
    //图片列表信息
	imgList : {},
    //画布列表信息
	canvasList : {},
    //塔的列表
	towerList : [],
    //敌人类表
	enemyList : [],
    //子弹列表
	bulletList : [],
    //关卡数
	mission : 0,
    //每关已出的敌人数
	missionEnemy : 0,
    //每关的休息时间
	missionTime : 2000,
    //每关的延迟时间
	missionLazy : 2000,
    //每个敌人的出场间隔时间
	enemyTime : 1000,
    //每个敌人的出场间隔延迟
	enemyLazy : 0,
    //计时器ID
	timer : 0,
    //当前选中的塔
	nowSelectTower : null,
    //初始化
	init : function(){
		
		this.initImg();
		this.initCanvas();
		this.initData();
		this.initBind();
	},
    //初始化图片
	initImg : function(){
		
		this.imgList = {
			
			enemy_img : document.getElementById("enemy_img"),
			tower_img : document.getElementById("tower_img"),
			bullet_img : document.getElementById("bullet_img"),
			btn_img : document.getElementById("btn_img")
		}
	},
    //初始化画布
	initCanvas : function(){
		
		this.canvasList = {
			
			map : document.getElementById("map").getContext("2d"),
			main : document.getElementById("main").getContext("2d"),
			info : document.getElementById("info").getContext("2d"),
			select : document.getElementById("select").getContext("2d"),
			tower : document.getElementById("tower").getContext("2d")
		}
	},
    //初始化数据
	initData : function(){
	
		Info.init(this.canvasList.info,this.imgList.tower_img);
	},
    //初始化绑定塔的事件
	initBind : function(){
		
		var select = document.getElementById("select");
		
		select.onclick = function(e){
			
			var x = e.offsetX || e.layerX,
				y = e.offsetY || e.layerY;
			//遍历塔的列表
			for(var i=0,l=Game.towerList.length;i<l;i++){
				//判断是否选择到了塔
				if(T.pointInRect({x:x,y:y},Game.towerList[i])){
					//画出范围
					Info.drawScope(Game.towerList[i]);
					
					if(Game.nowSelectTower){
						//升级或卖掉
						Info.upgradeOrSell(x,y);
					}
					
					Game.nowSelectTower = Game.towerList[i];
					
					break;
				}
			}
			//没有选中,清除
			if(i == l){
				Canvas.clear(Game.canvasList.select,500,500);
				
				Game.nowSelectTower = null;
			}
		}
		
	},
    //出敌人
	initEnemy : function(){
		
		if(Game.missionLazy > 0){
			
			Game.missionLazy -= 20;
			
			return false;
		}
		
		if(Game.enemyLazy > 0){
			
			Game.enemyLazy -= 20;
			
			return false;
		}
		else{
			
			Game.enemyLazy = Game.enemyTime;
		}
		
		if(Game.missionEnemy > 20){
			
			Game.missionEnemy = 1;
			
			Game.mission += 1;
			Info.updateMission();
			
			Game.missionLazy = Game.missionTime;
			
			if(Game.mission >= 20){
				
				Game.initEnemy = function(){
					
					if(Game.enemyList.length <= 0)Game.win();
				};
				return false;
			}
			
			return false;
		}
		
		Game.missionEnemy += 1;
		//新增一个敌人
		var enemy = new Enemy(Game.canvasList.main,Game.imgList.enemy_img,Game.mission,55,0,40,40);
		enemy.num = Game.mission*20+Game.missionEnemy;
		
		Game.enemyList.push(enemy);
	},
	getEnemy: function(id){
		for(var i = 0, len = Game.enemyList.length; i < len; i++){
			if(id == Game.enemyList[i].id){
				return Game.enemyList[i];
			}
		}
	},
	processors: {
		"1": function(packet){
			console.log(packet);
		
		if(Game.missionEnemy > 20){
			
			Game.missionEnemy = 1;
			
			Game.mission += 1;
			Info.updateMission();
			
			Game.missionLazy = Game.missionTime;
			
			if(Game.mission >= 20){
				
				Game.initEnemy = function(){
					
					if(Game.enemyList.length <= 0)Game.win();
				};
				return false;
			}
			
			return false;
		}
		
		Game.missionEnemy += 1;
		//新增一个敌人
		var enemy = new Enemy(Game.canvasList.main,Game.imgList.enemy_img,Game.mission,55,0,40,40, packet.tuple.id);
		enemy.num = Game.mission*20+Game.missionEnemy;
		
		Game.enemyList.push(enemy);
		}
	},
    //开始
	start : function(mapId){
		switch(mapId){
			case "1":
                
				MapData = MapOne;
                
				break;
            
			case "2":
                
				MapData = MapTwo;
                
				break;
            
			default:
                
				MapData = MapOne;
                
			break;
       
		}
        
		Map.draw(this.canvasList.map);
        
		this.timer = setInterval(Game.loop,30);
	},
    //重新开始
    restart : function(){
        
        this.stop();
        
        this.towerList = [];
        this.enemyList = [];
        this.bulletList = [];
        this.mission = 0;
        this.missionEnemey = 0;
        this.missionLazy = 2000;
        this.enemyLazy = 0;
        this.nowSelectTower = null;
        
        Info.score = 100;
        Info.life = 10;
        Info.mission = 1;
        Info.installTower = {};
        
        Canvas.clear(this.canvasList.map,500,500);
        Canvas.clear(this.canvasList.main,500,500);
        Canvas.clear(this.canvasList.tower,500,500);
        Canvas.clear(this.canvasList.select,500,500);
        
        Info.redraw();
        
        this.start();
    },
    //停止
	stop : function(){
		
		clearInterval(this.timer);
	},
    //结束
	over : function(){
		this.stop();
		alert("你输了!");
	},
    //赢了
	win : function(){
		this.stop();
		alert("你赢了!");
	},
    //循环体
	loop : function(){
		
		Canvas.clear(Game.canvasList.main,500,500);
		
		//Game.initEnemy();
		
		drawEnemy();
		drawBullet();
		
		updateEnemy();
		updateTower();
		updateBullet();
	}
}

Game.init();