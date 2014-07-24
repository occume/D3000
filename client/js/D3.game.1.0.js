;
(function(D3){
	
	var paperMapOffsetX = 0,
		paperMapOffsetY = 5;
	
	var Game = {
		papers: {},
		init: function(){
			this.papers.topInfo = Paper.create("header", 1200, 100);
			this.papers.bottomInfo = Paper.create("paper4", 1200, 600);
			this.papers.turret = Paper.create("paper4", 1200, 600);
			this.papers.main = Paper.create("paper4", 1200, 600);
			this.papers.map = Paper.create("paper4", 1200, 600);
			
			this.initPapers();
			
			D3.BottomInfo.init();
		},
		getPaper: function(name){
			return this.papers[name];
		},
		initPapers: function(){
			var map = this.papers.map,
				bottomInfo = this.papers.bottomInfo,
				turret = this.papers.turret,
				main = this.papers.main;
			map.css({
				position: "absolute",
				top : 0
			});
			main.css({
				position: "absolute",
				top : 0
			});
			turret.css({
				position: "absolute",
				top : 0
			});
			bottomInfo.css({
				position: "absolute",
				top : 0
			});
			
			D3.Map.drawMap();
			
		},
		loop: function(){
			this.papers.main.clear();
			D3.Monster.update();
			D3.Turret.update();
			D3.Shell.update();
			D3.Player.update();
		},
		start: function(){
			//D3.Turret.create(50, 0).draw();
			setInterval(function(){
				Game.loop();
			}, 30);
		}
		
	};
	
	D3.Game = Game;
	Game.init();
	Game.start();
	
}( window.D3 = window.D3 || {}));