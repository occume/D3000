;
(function(D3){
	
	var Player = Class.create({
		init: function(id, name){
			this.id = id;
			this.name = name;
			this.width = 300;
			this.height = 80;
			this.money = 100;
			this.x = 0;
			this.y = 0;
		}
	});
	
	Player.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("topInfo");
			paper.newRect(this.x, this.y, this.width, this.height, 0);
		},
		update: function(){
			
			
		},
		over: function(monster){
			monster.onHit(15);
			shells.remove(this);
		}
		
	});
	
	function circleInCircle(cir1,cir2){
		
		if(Math.sqrt(Math.pow(cir1.x-cir2.x,2)+Math.pow(cir1.y-cir2.y,2)) < (cir1.radius+cir2.radius))return true;
		
		return false;
		
	}
	
	var players = [];
	
	Player.create = function(id, name){
		var m = new Player(id, name);
		players.push(m);
		return m;
	};
	
	Player.update = function(){
		for(var i = 0, len = players.length; i < len; i++){
			var s = players[i];
			if(!s){
				continue;
			}
			s.draw();
			s.update();
		}
	};
	
	D3.Player = Player;
	
}( window.D3 = window.D3 || {}));