;
(function(D3){
	
	var Shell = Class.create({
		init: function(x, y, monster){
			this.monster = monster;
			this.x = x;
			this.y = y;
			this.width = 10;
			this.height = 10;
			this.sp = 5;
			this.radius = 5;
		}
	});
	
	Shell.addMethods({
		draw: function(){
			var paper = D3.Game.getPaper("main");
			paper.newImage("img/baicai.png", this.x, this.y, this.width, this.height);
		},
		update: function(){
			
			var v = this.getLineAngle();
			
			if(this.x > this.monster.x+20)this.x -= v.xsp;
			else if(this.x < this.monster.x +20)this.x += v.xsp;
			
			if(this.y > this.monster.y+20)this.y -= v.ysp;
			else if(this.y < this.monster.y + 20)this.y += v.ysp;
			
			if(this.y >= 500 || this.y <= 0 || this.x >= 500 || this.x <= 0){
				return this.over();
			}
			//判断子弹击中敌人
			if(circleInCircle(this,{x:this.monster.x+20,y:this.monster.y+20,radius:20})){
				console.log("击中！");
				this.over(this.monster);
			}
			
		},
		over: function(monster){
			monster.onHit(15);
			shells.remove(this);
		},
		getLineAngle : function(){
			
			var ydif = Math.abs(this.y-this.monster.y-15),
				xdif = Math.abs(this.x-this.monster.x-15),
				xsp,ysp;
				
			if(ydif >= xdif){
				
				ysp = this.sp;
				xsp = Math.floor(this.sp * (xdif / ydif));
			}
			else {
				xsp = this.sp;
				ysp = Math.floor(this.sp * (ydif / xdif));
			}
			
			return {xsp:xsp,ysp:ysp};
		}
	});
	
	function circleInCircle(cir1,cir2){
		
		if(Math.sqrt(Math.pow(cir1.x-cir2.x,2)+Math.pow(cir1.y-cir2.y,2)) < (cir1.radius+cir2.radius))return true;
		
		return false;
		
	}
	
	var shells = [];
	
	Shell.create = function(x, y, monster){
		var m = new Shell(x, y, monster);
		shells.push(m);
		return m;
	};
	
	Shell.update = function(){
		for(var i = 0, len = shells.length; i < len; i++){
			var s = shells[i];
			if(!s){
				continue;
			}
			s.draw();
			s.update();
		}
	};
	
	D3.Shell = Shell;
	
}( window.D3 = window.D3 || {}));