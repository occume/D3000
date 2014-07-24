;
(function(D3){
	
	var Monster = Class.create({
		init: function(x, y){
//			this.cxt = cxt;
//			this.img = img;
			this.x = x;
			this.y = y;
			this.width = 50;
			this.height = 50;
			this.passed = {};
			this.v = 2;
			this.maxLife = 50;
			this.life = 50;
			//敌人类型
//			this.type = type;
			//敌人最大血量
//			this.maxLife = 50 + type * 100;
		    //敌人当前血量
//			this.life = 50 + type * 100;
//			this.sp = 2;
			//是否活着
//			this.islive = true;
			//移动的方向
			this.dir = null;
		    //下个移动位置
			this.nextStep = null;
			//冰冻时间
//			this.frozenTime = 0;
			//敌人的序号
//			this.num = 0;
			//记录已经走过的位置
//			this.hadWalk = {};
			
//			this.id = id;
		}
	});
	
	Monster.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("main");
			paper.newImage("img/pig2.png", this.x, this.y, this.width, this.height);
			
			var persent = (this.life / this.maxLife).toFixed(2);
			//画出血量
//			Canvas.fillRect(this.cxt,this.x-5,this.y-5,persen,3,"rgba(38,223,116,0.8)");
			
			paper.newRect(this.x, this.y - 5, this.width * persent, 5, 0);
		},
		onHit: function(harm){
			this.life -= harm;
			if(this.life <= 0)
				this.over();
		},
		over: function(){
			monsters.remove(this);
		},
		arriveNext: function(){
			return (this.x >= this.nextStep.x - 5 && this.x <= this.nextStep.x) 
					&& 
				   (this.y >= this.nextStep.y - 5 && this.y <= this.nextStep.y);
		},
		update: function(){
			
			var x = parseInt(this.x / 50,10),
				y = parseInt(this.y / 50,10);
			
			var mapData = D3.Map.getCurrMap();
			
			if(!this.nextStep || this.arriveNext()){
				if(!this.nextStep){
					this.passed[x + "_" + y] = !0;
				}
//				if(x + 1 >= 10){
//					x = -1;
//				}else{
					if(mapData[x][y - 1] && !this.passed[x + "_" + (y - 1)]){
						this.dir = 1;
						y -= 1;
					}
					else if(mapData[x + 1][y] && !this.passed[(x + 1) + "_" + y]){
						this.dir = 2;
						x += 1;
					}
					else if(mapData[x][y + 1] && !this.passed[x + + "_" + (y + 1)]){
						this.dir = 3;
						y += 1;
					}
					else if(mapData[x - 1][y] && !this.passed[(x - 1) + "_" + y]){
						this.dir = 4;
						x -= 1;
					}
//				}
				
//				if(x == -1){
//					this.nextStep = {x: 500, y: y * 50 + 5};
//				}
	            //设置下个移动位置
//				else {
					this.nextStep = {x: x * 50 + 5, y: y * 50 + 5};
	                //记录已经走过的位置
					this.passed[x + "_" + y] = !0;
//				}
				
			}
			
			switch(this.dir){
			
				case 1:
					this.y -= this.v;
					break;
				case 2:
					this.x += this.v;
					break;
				case 3:
					this.y += this.v;
					break;
				case 4:
					this.x -= this.v;
					break;
			}
		}
	});
	
	var monsters = [];
	
	Monster.create = function(x, y){
		var m = new Monster(x, y);
		monsters.push(m);
		return m;
	};
	
	Monster.create(50, 0);
	
	Monster.getMonsters = function(){
		return monsters;
	};
	
	Monster.update = function(){
		for(var i = 0, len = monsters.length; i < len; i++){
			var m = monsters[i];
			m.draw();
			m.update();
		}
	};
	
	D3.Monster = Monster;
}( window.D3 = window.D3 || {}));