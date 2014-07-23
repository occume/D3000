(function(win,doc){

	var STR = {
		isEmpty:function(s){
			return s.replace(/^[\s\xa0\u3000]+|[\u3000\xa0\s]+$/g, "").length == 0;
		}
	};
	
	win.STR = STR;
	
})(window,document);

(function(win, doc) {
	var visi = !1, $wrap, $win, winWidth, winHeight, wrapWidth, wrapHeight;
	var SBOX = function() {
		return {
			init : function() {
				$win = $(win), $wrap = $("#wrap"), winWidth = $win.width(),
						winHeight = $win.height(), wrapWidth = $wrap.width(),
						wrapHeight = $wrap.height();
				var scrTop = $win.scrollTop();
				$wrap.css({
					left : (winWidth - wrapWidth) / 2,
					top : (scrTop + winHeight - 80)
				});
				$("#help").css({
					left : 0,
					top : -205,
					display : "none"
				});
				var ll = $wrap.offset().left - $("#im").width(), tt = $wrap
						.offset().top;
				
				$("#im").css({
					left : ll,
					top : tt,
					display : "none"
				});
				$wrap.css({
					display : "none"
				});
				// this.bind();
			},
			replace : function() {
				var scrTop = $win.scrollTop();
				$wrap.css({
					top : scrTop + winHeight - 80
				});

				var tt = scrTop + winHeight - 100;
				$("#im").css({
					top : tt
				});
			},
			show : function() {
				$wrap.fadeIn();
				$("#wrap :input").focus();

			},
			close : function() {
				$("#wrap :input").val("");
				$("#help").fadeOut();
				$wrap.fadeOut();
			},
			bind : function() {
				var me = this;
				$(win).scroll(function() {
					SBOX.replace();
				});
				$(doc).keypress(function(e) {
					// alert(123);
					e = e || win.event;
					var kc = e.keyCode;
					if (kc === 13) {
						me.show();
					}
				});
				$("#inbox :input").live("keypress", function(e) {

					e = e || win.event;
					var kc = e.keyCode, val = $(this).val();
					if (kc === 13) {
						STR.isEmpty(val) ? me.close() : me.parse(val);
					} else {
						return true;
					}
					return false;
				});
			}
		// CONS:{VISI:!1}
		};
	}();
	/**
	 * 
	 * 
	 * 
	 */
	(function() {
		var orderReg = /-(?:(\w+)\s(.+)|(\d+))|.*/;
		var parse = function(info) {
			var ret = orderReg.exec(info);
			if (!ret) {
				SBOX.close();
				return;
			}
			if (ret[1]) {
				if (ret[1] in parse) {
					parse[ret[1]](ret[2]);
				} else {

				}
				return;
			}
			if (ret[3]) {
				parse.query(ret[3]);
				return;
			}
			parse.IM(ret[0]);
		}
		/**
		 * 快速打开链接
		 */
		var URLS = {
			cs : "http://cs.2211.com",
			baidu : "http://www.baidu.com",
			fy : "http://fy.2211.com",
			faq : "http://work.2211.com/ctService/faqManage/addFaq.jsp"
		};
		parse.open = function(url) {
			SBOX.close();
			if (URLS[url]) {
				win.open(URLS[url]);
			} else {

			}
		};
		/**
		 * 快速发通知
		 */
		parse.notice = function() {

		};
		/**
		 * 快速发通知
		 */
		parse.help = function() {
			$("#wrap input").val("");
			$("#help").fadeIn();
		};
		/**
		 * 快速查询
		 */
		parse.query = function(id) {
			SBOX.close();
			$("#woID").val(id);
			gdcx.currentPage = 1;
			gdcx.queryType = 1;
			gdcx.BindList();
		};
		/**
		 * IM
		 */
		parse.IM = function(msg) {
			SBOX.close();
			
			var name = $("#welcome").text().substring(4);
			$("#im").fadeIn();
//			$.ajax({
//				url : "/im/sendIM",
//				method : "post",
//				data : {
//					message : msg,
//					username : name
//				},
//				cache : false
//			});
			D3.addProcessor(D3.CHAT, D3.CHAT_TO_ALL, function(pkt){
				showIM(pkt);
			});
			D3.session.send(D3.makePacketByType(D3.CHAT, D3.CHAT_TO_ALL, msg));
		};
		/**
		 * 异常提示
		 */
		parse.excep = function(msg) {

		};
		SBOX.parse = parse;
	})();
	win.SBOX = SBOX;
})(window, document);

var colors = [ "#063", "#903" ];
function showIM(data) {

	$("#im").fadeIn();
	var p = $("<p>" + data.from + ":" + data.tuple + "</p>");

	$("#im").append(p);
	
	if (D3.playerId == data.from) {
		p.css({
			color : colors[0]
		});
	} else {
		p.css({
			color : colors[1]
		});
	}
	var h = p.height(), t = $("#im").offset().top, tt = $("#wrap").offset().top;

	$("#im").animate({
		top : t - h
	});
	var timer = setTimeout(function() {
		p.fadeOut();
		clearInterval(timer);
	}, 3 * 60 * 1000);
}
SBOX.systemMessage = function(msg){
	showIM({from: "SYSTEM", tuple: msg});
};
$(function() {

	SBOX.init();
	SBOX.bind();
	$(window).scroll(function() {
		SBOX.replace();
	});
});