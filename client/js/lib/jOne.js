/**
 * sdfsdf哈哈啊哈哈哈啊哈 
 * @module tree
 * @class Node.Tree
 */
;
var IS_DONTENUM_BUGGY = (function(){
    for (var p in { toString: 1 }) {
      // check actual property name, so that it works with augmented Object.prototype
      if (p === 'toString') return false;
    }
    return true;
})();

(function(){
	
	var glob = function(){return this;}();
	
	function $A(iterable) {
		  if (!iterable) return [];
		  // Safari <2.0.4 crashes when accessing property of a node list with property accessor.
		  // It nevertheless works fine with `in` operator, which is why we use it here
		  if ('toArray' in Object(iterable)) return iterable.toArray();
		  var length = iterable.length || 0, results = new Array(length);
		  while (length--) results[length] = iterable[length];
		  return results;
		}
	var Class = function(){
		
		function subclass() {};
		function create() {
		    var parent = null, properties = $A(arguments),
		    	len = 0;
		    if (jOne.isFunction(properties[0]))
		      parent = properties.shift();

		    function klass() {
		    	//console.log(arguments);
		    	len = parent ? parent.prototype.init.length : 0;
		    	supArg = slice.apply(arguments, [0, len]),
		    	thisArg = slice.apply(arguments, [len]);
		    	if(parent){
		    		parent.prototype.init.apply(this, supArg);
		    	}
		    	if(jOne.isFunction(this.init))
		    		this.init.apply(this, thisArg);
		    }

		    jOne.extend(klass, Class.Methods);
		    klass.superclass = parent;
		    klass.subclasses = [];

		    if (parent) {
		      subclass.prototype = parent.prototype;
		      klass.prototype = new subclass;
		      parent.subclasses.push(klass);
		    }

		    for (var i = 0, length = properties.length; i < length; i++)
		      klass.addMethods(properties[i]);

		    if (!klass.prototype.init)
		      klass.prototype.init = function(){};

		    klass.prototype.constructor = klass;
		    return klass;
		}
		function addMethods(source){
			var ancestor   = this.superclass && this.superclass.prototype,
	        	properties = jOne.keys(source),
	        	me = this;
			
		    if (IS_DONTENUM_BUGGY) {
		      if (source.toString != Object.prototype.toString)
		        properties.push("toString");
		      if (source.valueOf != Object.prototype.valueOf)
		        properties.push("valueOf");
		    }
	
		    for (var i = 0, length = properties.length; i < length; i++) {
		      var property = properties[i], value = source[property];
//		      if(ancestor && property === 'init' && jOne.isFunction(value)){
//		    	  value = function(){
//		    		 // ancestor.init.apply(me, arguments);
//		    		  value.apply(me, arguments);
//		    	  }
//		      }
//		      if (ancestor && Object.isFunction(value) &&
//		          value.argumentNames()[0] == "$super") {
//		        var method = value;
//		        value = (function(m) {
//		          return function() { return ancestor[m].apply(this, arguments); };
//		        })(property).wrap(method);
//
//		        value.valueOf = (function(method) {
//		          return function() { return method.valueOf.call(method); };
//		        })(method);
//		        
//		        value.toString = (function(method) {
//		          return function() { return method.toString.call(method); };
//		        })(method);
//		      }
		      this.prototype[property] = value;
		    }

		    return this;
		}
		  
		  
		return {
			create: create,
			Methods: {
			      addMethods: addMethods
			}
		};
		
	}(); 
	
	glob.Class = Class;
	
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
})()

;
(function(){
	var glob = this,
	
		ArrayProto = Array.prototype,
		ObjProto = Object.prototype;
		
	var toString = ObjProto.toString,
		hasOwnProperty = ObjProto.hasOwnProperty,
		
		Native = {
			ForEach: ArrayProto.forEach,
			IsArray: ArrayProto.isArray
		};
		
	
	var J = jOne = function(obj){
			return new Wrapper(obj);
		};
		
		(typeof module != "undefined" && module.exports) ? 
				(module.exports = jOne) : (typeof define != "undefined" ? 
						(define(function() { return jOne; })) : (glob.jOne = jOne));
	
	var Wrapper = function(obj){
		this._wrapped = obj;
	};
	
	Wrapper.prototype = jOne.fn = jOne.prototype;
	
	jOne.extend = jOne.fn.extend = function(){
		var target = arguments[0] || {},
			i = 1,
			len = arguments.length, 
			deep = false, options;
		if(typeof target === 'boolean'){
			deep = target;
			target = arguments[1];// target == jOne
			i = 2;
		}
		if(len == i){
			target = this;// target == jOne(elem)
			--i;
		}
		for(; i < len; i++){
			if( (options = arguments[i]) != null){
				for(var name in options){
					var copy = options[ name ],src = target[ name ];
					if ( copy == null || copy === src )
					continue;
					if(deep && typeof copy === 'object'){
						if(src){
							jOne.extend(deep,src,copy);
						}else{
							target[ name ] = jOne.extend(deep, (copy.length ? [] : {}), copy);
						}
					}else{
						target[ name ] = copy;
					}
				}
			}
		}
		return target;
	};
	
	jOne.createUUID = (function (uuidRegEx, uuidReplacer) {
        return function () {
            return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(uuidRegEx, uuidReplacer).toUpperCase();
        };
    })(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == "x" ? r : (r & 3 | 8);
        return v.toString(16);
    });
	
	var each = jOne.each = jOne.forEach = function(obj, callback, context){
		if(obj == null) return;
		var i = 0, 
			len = obj.length,
			name,
			value;
		if(Native.ForEach && J.isArray(obj)){
			obj.forEach(callback, context);
		}else if(len !== undefined){
			for(value = obj[i]; i < len && callback.call(value, value, i) !== false; value = obj[++i]);
		}else{
			for(name in obj){
				if(hasOwnProperty.call(obj, name)){
					value = obj[name];
					if(callback.call(value, value, name) === false) break;
				}
			}
		}
		return obj;
	};
	
	jOne.extend({
		showDetail: function(obj){
			var log = console && console.log;
			for(var name in obj){
				log(name + " : " + obj[name]);
			}
		}
	});
	
	jOne.fn.each = function(callback, context){
		return jOne.each(this._wrapped, callback);
	};
	
	
	 var DONT_ENUMS = ['toString', 'toLocaleString', 'valueOf',
	                   'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable', 'constructor'];
	
	function keys(object) {
		    
		var results = [];
		for (var property in object) {
			if (hasOwnProperty.call(object, property))
				results.push(property);
		}

		if (IS_DONTENUM_BUGGY) {
			for (var i = 0; property = DONT_ENUMS[i]; i++) {
				if (hasOwnProperty.call(object, property))
					results.push(property);
			}
		}
		    
		return results;
	}
	jOne.keys = Native.keys || keys;
	jOne.IS_DONTENUM_BUGGY = IS_DONTENUM_BUGGY;
	jOne.isArray = Native.IsArray || function(obj){ return toString.call(obj) === "[object Array]";};
	jOne.isFunction = Native.IsArray || function(obj){ return toString.call(obj) === "[object Function]";};
	
})()
;
