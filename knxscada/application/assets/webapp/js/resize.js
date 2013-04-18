$(document).ready(
	function() {
		$(window).resize(
			function() {
	
				var windowWidth = $(window).width();
				var windowHeight = $(window).height();
	
//				var mainMargin = $('#main').css('margin').replace(/[^-\d\.]/g, '');
//				var mainPadding = $('#main').css('padding').replace(/[^-\d\.]/g, '');
//				var headerHeight = $('#header').css('height').replace(/[^-\d\.]/g, '');
//				var headerPadding = $('#header').css('padding').replace(/[^-\d\.]/g, '');
//				var footerHeight = $('#footer').css('height').replace(/[^-\d\.]/g, '');
				
				var mainMargin = 20;
				var mainPadding = 10;
				var headerHeight = 30;
				var headerPadding = 10;
				var footerHeight = 60;
				
				var newWidth = windowWidth - (mainMargin * 2) - (mainPadding * 2);
				var newHeight = windowHeight - headerHeight - footerHeight - (mainMargin * 2) - (mainPadding * 2) - (headerPadding * 2);
	
				$("#main").css({
					width : newWidth,
					height : newHeight
				});
				
				$("#sidebar").css({
					height : newHeight
				});
			});
		$(window).resize();
	});