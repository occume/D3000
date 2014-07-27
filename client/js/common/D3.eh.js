;
(function(){
	var slice = Array.prototype.slice;
	/**
	 * 
	 */
	if(!Function.prototype.bind){
		Function.prototype.bind = function (oThis) {
			var args = slice.call(arguments, 1),
			fToBind = this,
			fNOP = function () {},
			fBound = function () {
					return fToBind.apply(this instanceof fNOP && oThis
					? this
					: oThis,
					args.concat(slice.call(arguments)));
				};
				fNOP.prototype = this.prototype;
				fBound.prototype = new fNOP();
				return fBound;
			}; 
	}
	if(!Array.prototype.include){
		Array.prototype.include = function (condition, elem) {
			var included = false;
			//console.log("out this = " + this);
			jOne.each(this, function(o, i){
				//console.log("inner this = " + o);
				if(o[condition] === elem[condition]) {
					included = true;
					return false;
				}
			});
			return included;
		}; 
	}
	
	if(!Array.prototype.remove){
		Array.prototype.remove = function(obj){
			
			for(var i=0,l=this.length;i<l;i++){
				
				if(obj == this[i]){
					this.splice(i,1);
					break;
				}
			}
		};
	}
	
	if(jQuery){
		jQuery.fn.aPosition = function() {
		    thisLeft = this.offset().left;
		    thisTop = this.offset().top;
		    thisParent = this.parent();

		    parentLeft = thisParent.offset().left;
		    parentTop = thisParent.offset().top;

		    return {
		        left: thisLeft-parentLeft,
		        top: thisTop-parentTop
		    };
		};
	}
})();