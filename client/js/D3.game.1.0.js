;
(function(D3){
	
	var game = {
		papers: {},
		init: function(){
			this.papers.map = Raphael("paper4", 1200, 600);
			this.papers.info = Raphael("paper4", 1200, 600);
			this.drapMap();
		},
		drapMap: function(){
			var map = this.papers.map,
				info = this.papers.info;
			$(map.canvas).css({
				position: "absolute",
				top : 0,
				left : 15
			});
			$(info.canvas).css({
				position: "absolute",
				top : 0,
				left : 15
			});
			map.text(100, 200, "map");
			info.text(300, 500, "info");
		}
	};
	
	game.init();
	
}( window.D3 = window.D3 || {}));