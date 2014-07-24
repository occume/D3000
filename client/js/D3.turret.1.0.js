;
(function(D3){
	
	var Turret = Class.create({
		init: function(x, y){
			this.x = x;
			this.y = y;
			this.width = 50;
			this.height = 50;
			this.interval = 30;
		}
	});
	
	Turret.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("turret");
			paper.newImage("img/baicai.png", this.x, this.y, this.width, this.height);
		},
		update: function(){
			if(this.interval > 0){
				this.interval -= 1;
				return;
			}
			this.interval = 30;
			
			var monsters = D3.Monster.getMonsters(),
				monster;
			for(var i = 0, len = monsters.length; i < len; i++){
				monster = monsters[i];
				if(!monster){
					continue;
				}
				if(rectInCircle(monster, {x: this.x + 25,y: this.y + 25,radius:100})){
//					console.log("攻击！");
					D3.Shell.create(this.x + 20, this.y + 20, monster).draw();
				}
			}
		}
	});
	
	function rectInCircle(rect,cir){
		
		var x1 = rect.x, y1 = rect.y,
			x2 = rect.x + rect.width, y2 = rect.y + rect.height;
			
		if(Math.sqrt(Math.pow(x1-cir.x,2)+Math.pow(y1-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x1-cir.x,2)+Math.pow(y2-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x2-cir.x,2)+Math.pow(y2-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x2-cir.x,2)+Math.pow(y1-cir.y,2)) < cir.radius)
			return true;
		
		return false;
	}
	
	var turrets = [];
	
	Turret.create = function(x, y){
		var m = new Turret(x, y);
		turrets.push(m);
		return m;
	};
	
	Turret.update = function(){
		for(var i = 0, len = turrets.length; i < len; i++){
			var t = turrets[i];
			t.update();
		}
	};
	
	D3.Turret = Turret;
	
}( window.D3 = window.D3 || {}));