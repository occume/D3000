;
(function(D3){
	
	var Monster = Class.create({
		init: function(cxt,img,type,x,y,width,height, id){
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
	});
	
	Monster.addMethods({
		draw: function(attrs){
			console.log(D3);
			var paper = D3.Game.getPaper("main");
			paper.newImage("img/pig2.png", 0, 32, 32, 32);
		},
		update: function(){
			
		}
	});
	
	var monsters = [];
	
	Monster.create = function(){
		var m = new Monster();
		monsters.push(m);
		return m;
	};
	
	
	Monster.update = function(){
		var m = Monster.create();
		m.draw();
		console.log(m);
	};
	
	D3.Monster = Monster;
}( window.D3 = window.D3 || {}));