;
(function(D3){
	
	var Monster = Class.create({
		init: function(x, y, id){
			this.id = id;
			this.x = x;
			this.y = y;
			this.width = 50;
			this.height = 50;
			this.passed = {};
			this.v = 2;
			this.maxLife = 50;
			this.life = 50;
			this.dir = null;
			this.nextStep = null;
		}
	});
	
	Monster.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("main");
			paper.newImage("img/pig2.png", this.x, this.y, this.width, this.height);
			
			var persent = (this.life / this.maxLife).toFixed(2);
			
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
			
			if(this.x >= 1150){
				//this.over(true);
				D3.session.send(D3.makePacketByType(D3.MONSTER, D3.MONSTER_OVER, this.id));
				return false;
			}
			
			var x = parseInt(this.x / 50,10),
				y = parseInt(this.y / 50,10);
			
			var mapData = D3.Map.getCurrMap();
			
			if(!this.nextStep || this.arriveNext()){
				if(!this.nextStep){
					this.passed[x + "_" + y] = !0;
				}
				if(x + 1 >= 23){
					x = -1;
				}else{
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
				}
//				
				if(x == -1){
					this.nextStep = {x: 1150, y: y * 50 + 5};
				}
	            //设置下个移动位置
				else {
					this.nextStep = {x: x * 50 + 5, y: y * 50 + 5};
	                //记录已经走过的位置
					this.passed[x + "_" + y] = !0;
				}
				
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
	
	Monster.create = function(x, y, id){
		var m = new Monster(x, y, id);
		monsters.push(m);
		return m;
	};
	
	Monster.getMonsters = function(){
		return monsters;
	};
	
	Monster.getMonster = function(id){
		for(var i = 0, len = monsters.length; i < len; i++){
			if(id == monsters[i].id){
				return monsters[i];
			}
		}
	};
	
	Monster.update = function(){
		for(var i = 0, len = monsters.length; i < len; i++){
			var m = monsters[i];
			m.draw();
			m.update();
		}
	};
	
	// net message 处理
	D3.addProcessor(D3.MONSTER, D3.MONSTER_DECREMENT_LIFE,
	/**
	 * 怪掉血
	 */
	function(pkt){
		var id = pkt.tuple[1],
		monster = Monster.getMonster(id),
		harm = pkt.tuple[2];
//		effer = packet.tuple[3];
	
		monster.life -= harm;
	});
	
	D3.addProcessor(D3.MONSTER, D3.MONSTER_OVER,
	/**
	* 击杀怪 或者 走出地图
	*/
	function(pkt){
		var 
			playerId = pkt.tuple[0],
			player = D3.Player.getPlayer(playerId),
			id = pkt.tuple[1],
			monster = Monster.getMonster(id);
		if(pkt.tuple[2] && pkt.tuple[2] == "OUT_OF_MAP"){
			//Info.updateLife();
		}
		else{
			//Info.updateScore(5, playerId);
			player.score += 1;
			D3.Player.update();
		}
		monsters.remove(monster);
	});
	
	D3.Monster = Monster;
}( window.D3 = window.D3 || {}));