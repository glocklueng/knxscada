$(document).ready(
	function() {
		$(window).resize(
			function() {
				resize();
			}
		);		
		$(window).resize();
});

function resize(){
	var windowWidth = $(window).width();
	var windowHeight = $(window).height();
	
	var sidebarWidth = 0;
	var sideBarPadding = 0;
	
	var sideBarDisplay = $("#sidebar").css("display");
	if(sideBarDisplay == "table") {
		sidebarWidth = 165;
		sideBarPadding = 10;
		$("#main").css("left","195px");
	}else{
		$("#main").css("left","0px");
	}
	
	var mainMargin = 10;
	var headerHeight = 40;
	var headerPadding = 10;
	var footerHeight = -10;
	var sublayersHeight = 60;
	
	var newWidth = windowWidth - (mainMargin * 2)  - sidebarWidth - (sideBarPadding*2);
	var newHeight = windowHeight - headerHeight - footerHeight - (mainMargin * 4) - (headerPadding * 2);
	var newContentHeight = newHeight - sublayersHeight;
	
	$("#main").css({
		width : newWidth,
		height : newHeight
	});
	
	$("#sidebar").css({
		height : newHeight - (sideBarPadding*2)
	});
	
	$("#content").css({
		height : newContentHeight
	});
	
	$("#elements-area").css({
		height : newContentHeight - 50
	});
	$(".accordion-div").css({
		height : newContentHeight - 115
	});
		
	var loadingTop = (windowHeight/2) - 100;
	$("#loading-img").css("top", loadingTop+"px");
	
	//PROJECT CHOOSER RESIZE
	var titleHeight = 80;
	var footerHeight = 100;
	
	var newWidth = windowWidth - (mainMargin * 2) ;
	var newHeight = windowHeight - titleHeight - footerHeight;

	$("#projects").css({
		height : newHeight
	});
}