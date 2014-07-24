;
(function(D3){
	
	var Player = Class.create({
		init: function(id, name){
			this.id = id;
			this.name = name;
			this.width = 300;
			this.height = 15;
			this.money = 100;
			this.score = 0;
			this.x = (id - 1) * 300;
			this.y = 0;
			this.color = colors[id];
		}
	});
	
	Player.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("topInfo");
			paper.newRect(this.x, this.y, this.width, this.height, 0).attr({fill: this.color});
			paper.newRect(this.x, this.y + 15, this.width, this.height + 70, 0).attr({fill: this.color, opacity: 0.2});
			
			paper.text(this.x + 50, this.y + 35, this.name).attr({"font-size": 25});//.attr({fill: this.color});
			paper.newRect(this.x, this.y + 55, this.width, 2, 0).attr({fill: this.color, opacity: 0.2});
			paper.text(this.x + 50, this.y + 80, "$: " + this.money).attr({"font-size": 20});
			paper.text(this.x + 200, this.y + 80, "SCORE: " + this.score).attr({"font-size": 20, fill: "#333"});
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
	
	var players = [],
		//绿 浅蓝 橙 红
		colors = [0, "#5cb85c", "#46b8da", "#eea236", "#d43f3a"];
	
	Player.create = function(id, name){
		var m = new Player(id, name);
		players.push(m);
		return m;
	};
	
	Player.create(1, "occume");
	Player.create(2, "ggshop");
	Player.create(3, "d_jin");
	Player.create(4, "nbxx");
	
	var drawed = 0;
	Player.update = function(){
		
		if(drawed > 0){
			return;
		}
		drawed++;
		console.log("draw player");
		
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