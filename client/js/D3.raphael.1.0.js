;
(function(){
	
	var CONS = {
		dashed: {fill: "green", stroke: "none", "stroke-dasharray": ""}
	};
	
	var Paper = Class.create({
		init: function(holder, width, height){
			this.paper = Raphael(holder, width, height);
		}
	});
	
	Paper.addMethods({
		css: function(attrs){
			jOne.extend(this.paper.canvas.style, attrs);
		},
		set: function(){
			return this.paper.set();
		},
		clear: function(){
			this.paper.clear();
		},
		newBall: function(x, y, r){
			return this.paper.circle(x, y, r).attr(CONS.dashed);
		},
		newRect: function(x, y, width, height, r){
			return this.paper.rect(x, y, width, height, r).attr(CONS.dashed);
		},
		newImage: function(src, x, y, width, height){
			return this.paper.image(src, x, y, width, height);
		},
		path: function(str){
			return this.paper.path(str);
		},
		text: function(x, y, str){
			return this.paper.text(x, y, str);
		}
	});
	
	Paper.create = function(holder, width, height){
		return new Paper(holder, width, height);
	};
	
	window.Paper = Paper;
})();