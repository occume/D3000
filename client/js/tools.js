 //画布类
 var Canvas = {
     //清除画布
    clear : function(cxt,x,y){
        cxt.clearRect(0,0,x,y);
    },
	clearRect : function(cxt,x,y,width,height){
		cxt.clearRect(x,y,width,height);
	},
     //画图
    drawImg : function(cxt,img,x,y,sw,sh,dx,dy,dw,dh){
	
         if(!sw)cxt.drawImage(img,x,y);
		 else cxt.drawImage(img,x,y,sw,sh,dx,dy,dw,dh);
    },
     //画文字
    drawText : function(cxt,string,x,y,color){
         
         cxt.fillStyle = color;
         cxt.font = 'bold 12px sans-serif';
         cxt.fillText(string,x,y);
    },
    //画填充的方
	fillRect : function(cxt,x,y,width,height,color){
		
		cxt.fillStyle = color;
		cxt.fillRect(x,y,width,height);
	},
    //画边框的方
	drawRect : function(cxt,x,y,width,height,color){
		
		cxt.strokeStyle = color;
		cxt.lineWidth = 1;
		cxt.strokeRect(x,y,width,height);
	},
	 //画圆
	//ctx:context2d对象,x:圆心x坐标,y:圆心y坐标,radius:半径,color:颜色
	fillArc : function(cxt,x,y,radius,color){
		cxt.fillStyle = color;
		cxt.beginPath();
		cxt.arc(x,y,radius,0,Math.PI*2,true);
		cxt.closePath();
		cxt.fill();
	}
 }
 
 var T = {
	//判断一个点是否在一个矩形中
	pointInRect : function(point,rect){
		
		if(point.x >= rect.x && point.x <= (rect.x+rect.width)
			&& point.y >= rect.y && point.y <= (rect.y + rect.height))
		return true;
		
		return false;
	},
    //判断两个圆是否相交
	circleInCircle : function(cir1,cir2){
		
		if(Math.sqrt(Math.pow(cir1.x-cir2.x,2)+Math.pow(cir1.y-cir2.y,2)) < (cir1.radius+cir2.radius))return true;
		
		return false;
		
	},
    //判断矩形与圆相交
	rectInCircle : function(rect,cir){
		
		var x1 = rect.x,y1 = rect.y,
			x2 = rect.x+rect.width,y2= rect.y+rect.height;
			
		if(Math.sqrt(Math.pow(x1-cir.x,2)+Math.pow(y1-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x1-cir.x,2)+Math.pow(y2-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x2-cir.x,2)+Math.pow(y2-cir.y,2)) < cir.radius ||
			Math.sqrt(Math.pow(x2-cir.x,2)+Math.pow(y1-cir.y,2)) < cir.radius)
			return true;
		
		return false;
	}
	
 }
//扩展查询方法
Array.prototype.index = function(obj){
	
	for(var i=0,l=this.length;i<l;i++){
		
		if(obj == this[i]){
			
			return i;
		}
	}
	return -1;
} 
//扩展删除
Array.prototype.remove = function(obj){
	
	for(var i=0,l=this.length;i<l;i++){
		
		if(obj == this[i]){
			this.splice(i,1);
			break;
		}
	}
}