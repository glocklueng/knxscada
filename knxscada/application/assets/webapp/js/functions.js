function showPopupMenu(id, x, y){
	hidePopupMenu();
	$("#"+id).css("top", y).css("left", x);
	$("#"+id).show("blind",{},100);
}

function hidePopupMenu(){	
	$(".popup-menu").hide("fade",{},100);
}

function showProjectChooser(){
	$("#projectchooser").show("scale", {}, 200);
}

function hideProjectChooser(){
	$("#projectchooser").hide("scale", {}, 200);
}

function setFocused(elem){
	$(elem).addClass("focused");
}

function unsetFocused(elem){
	$(elem).removeClass("focused");
}

function setBackgroundColor(element, color){
	$(element).css("background-color", color);
}

function showLoadingPanel(){
	$("#loading").show("fade", {}, 200);
}

function hideLoadingPanel(){
	$("#loading").hide("fade", {}, 200);
}

function showProjectImageLoading(){
	$("#project-image-loading").show("fade",{},500);
}

function hideProjectImageLoading(){
	$("#project-image-loading").hide("fade",{},500);
}

function showTooltip(target, posX, posY){
	$("#tooltip").css("top", posY);
	$("#tooltip").css("left", posX);
	var html = $("#"+target).html();
	$("#tooltip").html(html);
	$("#tooltip").show("fade", {}, 200);
}

function hideTooltip(target){
	$("#tooltip").hide("fade", {}, 200);
}

function showProjectTooltip(posX, posY){
	$("#projectTooltip").css("top", posY);
	$("#projectTooltip").css("left", posX);
	$("#projectTooltip").show("fade", {}, 200);
}

function hideProjectTooltip(){
	$("#projectTooltip").hide("fade", {}, 200);
}

function showLayerIconChooser(){
	$(".layer-icon-chooser").show("fade", {}, 200);
}

function hideLayerIconChooser(){
	$(".layer-icon-chooser").hide("fade", {}, 500);
}

function initElementChooserPreview(){
	$(".element-chooser-preview").click(function(e){
		showElementChooser();
	});
}
function showElementChooser(){
	$(".element-chooser").show("fade", {}, 200);
}
function hideElementChooser(){
	$(".element-chooser").hide("fade", {}, 200);
}

function loadSettingsPanel(){
	$( "#sublayer-settings-accordion" ).accordion({
	      heightStyle: "content",
	      collapsible: true
		});
	$("#settings-area-trigger").off("click");
	$("#settings-area-trigger").click(function(e){
		$(".settings-panel").toggle();
			var elementsDisplay = $(".settings-panel").css("display");
			if(elementsDisplay == "block"){
				$(".elements-panel").hide();
				setElementPanelVisible();
			}else{
				setElementPanelHide();
			}	
	});
	
	$('#background-image-file').off("change");
	$('#background-image-file').on("change", function(){
		showLoadingPanel();
		$("#background-image-submit-button").click();
	});
	
	$("#background-image-choose-button").off("click");
	$("#background-image-choose-button").click(function(e){
		$("#background-image-file").click();
	});
}

var currentSize = 220;

function loadElementsPanel(){
	$( "#elements-area" ).resizable({
	    maxWidth: 610,
	    minWidth: 220,
		handles: "w",
		resize: function (event, ui) {
			$( "#elements-area" ).css("left", '0');
        },
        start: function (event, ui) {
        	$("#content").css("overflow","hidden");
        },
        stop: function (event, ui) {
        	$("#content").css("overflow","auto");
        	currentSize = ui.size.width;
        }
	});
	
	$("#elements-area-trigger").off("click");
	$("#elements-area-trigger").click(function(e){
			$(".elements-panel").toggle();
			var elementsDisplay = $(".elements-panel").css("display");
			if(elementsDisplay == "block"){
				$(".settings-panel").hide();
				setElementPanelVisible();
			}else{
				setElementPanelHide();
			}	
	});
		
	var counter = 0;
	$(".draggable-element").each(function(){
		var isDraggable = $(this).hasClass('ui-draggable');
		if(isDraggable){
			counter = counter + 1;
		}
	});
	if(counter == 0){
		initDragAndDrop();
	}
	setElementPanelHide();
}



function initDragAndDrop(){

	initDraggableElements();
	initVisualisationElements();

	$("#elements-container").droppable({
		  accept: ".draggable-element, .visualisation-element",
	      drop: function( event, ui ) {
	    	    var offset = $("#content").offset();
	    	    var x = ui.offset.left - offset.left;
				var y = ui.offset.top - offset.top;
				var isDraggableElement = $(ui.draggable).hasClass("draggable-element");
				
				var elem = $(ui.draggable);
				
				if(isDraggableElement){
					var element = elem.clone();
					
					transformElement($(element).children());
					
					$(element).css("position", "absolute");
					$(element).css("top", y);
					$(element).css("left", x);
					$(element).addClass("visualisation-element");
					$(element).removeClass("draggable-element");
					$(element).removeClass("popup-menu-trigger");
					
					disableElementDraggable(elem);
					makeElementRemovable(element);

					$(this).append(element);
				}
				
				var callback = $(elem).attr("callback");
				var elementid = $(elem).attr("elementid");
				var isRemoved = $(elem).hasClass("removed");
				if(!isRemoved){
					changeElementPosition(callback, elementid, x, y);
	      		}
				loadControllers();
	      }
	});
	$("#elements-area").droppable({
		greedy: true
	});
}

function initDraggableElements(){
	$(".draggable-element").each(function(){
		var element = $(this);
		makeElementDraggable(element);
		
		var isDraggable = element.attr("draggable");
		
		if(isDraggable !== undefined && isDraggable=="false"){
			disableElementDraggable(this);
		}
	});
}

function initVisualisationElements(){
	$(".visualisation-element").each(function(){
		makeElementRemovable(this);
		transformElement($(this).children());
	});
}

function transformElement(element){
	$(element).removeClass("focused");
	
	var clazz = $(element).attr("class");
	var newClass = clazz + "-v";
	$(element).removeClass(clazz);
	$(element).addClass(newClass);
}

function makeElementDraggable(element){
	$( element ).draggable({ 
		scroll: true,
		helper: "clone",
		start: function( event, ui ) {
			
			transformElement($(ui.helper).children());
			
			$(".elements-panel").css("overflow","visible");
			$(".elements").css("overflow","visible");
		},
		stop: function( event, ui ) {
			$(".elements-panel").css("overflow","auto");
			$(".elements").css("overflow","auto");
		}
	});
}


function enableElementDraggable(element){
	$( element ).draggable("option", "disabled", false );
}

function disableElementDraggable(element){
	$( element ).draggable("option", "disabled", true );
	$(element).removeClass("ui-draggable-disabled");
	$(element).removeClass("ui-state-disabled");	 
}

function changeElementPosition(callback, elementid, x, y){
	
	var data = {
			id : elementid,
			type: "update",
			x  : x,
			y  : y
	};
	
	sendJSONData(callback, data);
}

function removeVisualisationElement(callback, elementid){
	
	$(".draggable-element").each(function(){
		var elemid = $(this).attr("elementid");
		if(elemid == elementid){
			enableElementDraggable($(this));
		}
	});
	
	var data = {
			id : elementid,
			type: "remove"
	};
	
	sendJSONData(callback, data);
}

function removeElement(elementid){
	$(".visualisation-element").each(function(){
		var elemid = $(this).attr("elementid");
		if(elemid == elementid){
			$(this).remove();
		}
	});
}

function sendJSONData(callback, data){
	$.ajax({
		url : callback,
		type : 'post',
		cache : false,

		data : $.toJSON(data),
		contentType : 'application/json',
		dataType : 'json',
		complete : function(xhr, status) {
			console.log(status);
		}
	});
}

function setElementPanelHide(){
	$("#elements-area").resizable( "option", "disabled", true );
	$("#elements-area").css("width","20px");
	$("#triggers").css("left", "-2px");
}

function setElementPanelVisible(){
	$("#elements-area").css("width",currentSize+"px");
	$("#elements-area").resizable( "option", "disabled", false );
	$("#triggers").css("left", "-24px");
}

function showElementPanel(){
	$(".elements-panel").css("display", "block");
}

function initProjectName(){
	$("#projectName").off("mouseover");
	$("#projectName").off("mouseout");
	
	$("#projectName").click(function(e){
		showProjectChooser();
	});
	
	$("#projectName").mouseover(function(e){
		$("#projectName").css("color", "#00E600");
		showProjectTooltip(e.pageX, e.pageY);
	});
	
	$("#projectName").mouseout(function(e){
		$("#projectName").css("color", "#D8E2F2");
		hideProjectTooltip();
	});
}

var selectedLayerId = 0;
function initLayers(){
	
	$(".layer").off("mouseover");
	$(".layer").off("mouseout");
	$(".layerItem").off("mouseover");
	$(".layerItem").off("mouseout");
	$(".layerItemName").off("mouseover");
	$(".layerItemName").off("mouseout");
	
	$(".layer").mouseover(function(e){
 		//var target = $(this).attr("descriptionBlockId");
 		//showTooltip(target, e.pageX, e.pageY);
 	});
 	$(".layer").mouseout(function(e){
 		//var target = $(this).attr("descriptionBlockId");
 		//hideTooltip(target);
 	});
 	
 	var isSelected = false;
 	$(".layerItem").each(function(e){
 		var layerid = $(this).parent().attr("layerid");
 		if(layerid == selectedLayerId){
 			$(this).addClass("selected");
 			$(this).css("background","#88c9e8");
 		}
 		var hasClass = $(this).hasClass("selected");
 		if(hasClass){
 			isSelected = true;
 		}
 	});

 	if(!isSelected){
 		$(".layerItem").first().addClass("selected");
 		$(".layerItem").first().css("background","#88c9e8");
 	}
 	
 	$(".layerItem").click(function(e){
 		showLoadingPanel();
 		$(".layerItem").each(function(e){
			$(this).removeClass("selected");
			$(this).css("background", "rgba(172, 227, 7, 1)");
		});
		$(this).addClass("selected");
		$(this).css("background", "rgba(245,187,61,1)");
		
		selectedLayerId = $(this).parent().attr("layerid");
		selectedSubLayerId = 0;
		
	});
	
	$(".layerItem").mouseover(function(e){
		if($(this).hasClass("selected")){
			$(this).css("background", "rgba(245,187,61,1)");
		}else{
			$(this).css("background", "rgba(245,187,61,1)");
		}
	});
	
	$(".layerItem").mouseout(function(e){
		if($(this).hasClass("selected")){
			$(this).css("background", "#88c9e8");
		}else{
			$(this).css("background", "rgba(172, 227, 7, 1)");
		}
	});
}

var selectedSubLayerId = 0;
function initSubLayers(){
	$(".sublayer").off("mouseover");
	$(".sublayer").off("mouseout");
	$(".subLayerItem").off("mouseover");
	$(".subLayerItem").off("mouseout");
	$(".subLayerItemName").off("mouseover");
	$(".subLayerItemName").off("mouseout");
	
	$(".sublayer").mouseover(function(e){
 		//var target = $(this).attr("descriptionBlockId");
 		//showTooltip(target, e.pageX, e.pageY);
 	});
 	$(".sublayer").mouseout(function(e){
 		//var target = $(this).attr("descriptionBlockId");
 		//hideTooltip(target);
 	});
 	
 	var isSelected = false;
 	$(".subLayerItem").each(function(e){
 		var sublayerid = $(this).parent().attr("sublayerid");
 		if(sublayerid == selectedSubLayerId){
 			$(this).addClass("selected");
 			$(this).css("background","#88c9e8");
 		}
 		var hasClass = $(this).hasClass("selected");
 		if(hasClass){
 			isSelected = true;
 		}
 	});
 	if(!isSelected){
 		$(".subLayerItem").first().addClass("selected");
 		$(".subLayerItem").first().css("background","#88c9e8");
 	}
 	
 	$(".subLayerItem").click(function(e){
 		showLoadingPanel();
 		$(".subLayerItem").each(function(e){
			$(this).removeClass("selected");
			$(this).css("background", "rgba(172, 227, 7, 1)");
		});
		$(this).addClass("selected");
		$(this).css("background", "rgba(245,187,61,1)");
		selectedSubLayerId = $(this).parent().attr("sublayerid");
		return true;
	});
	
	$(".subLayerItem").mouseover(function(e){
		if($(this).hasClass("selected")){
			$(this).css("background", "rgba(245,187,61,1)");
		}else{
			$(this).css("background", "rgba(245,187,61,1)");
		}
	});
	
	$(".subLayerItem").mouseout(function(e){
		if($(this).hasClass("selected")){
			$(this).css("background", "#88c9e8");
		}else{
			$(this).css("background", "rgba(172, 227, 7, 1)");
		}
	});
}

function initProjectImageFileChooser(){
	$('#project-image-file').on("change", function(){ 
		showProjectImageLoading();
		$("#project-image-submit-button").click();
	});
	$("#project-image-choose-button").click(function(e){
		$("#project-image-file").click();
	});
	
	hideProjectImageLoading();
}

function initLayerIconChooser(){
	$(document).click(function(e){
		hideLayerIconChooser();
	});
	
	$(".layer-icon-container").click(function(e){
		showLayerIconChooser();
		return false;
	});
}

function initPopupMenu(){
	
	$(document).click(function(e){
		hidePopupMenu();
	});
						
	$(".popup-menu-trigger").off('mousedown');
	$(".popup-menu-trigger").off('taphold');
	
	$(".popup-menu-trigger").mousedown(function(e){ 
	    if( e.button == 2 ) { 
	      var id = $(this).attr("popupMenuId");
	      var x = e.pageX;
	      var y = e.pageY;
	     
	      var isSubLayer = $(this).hasClass("sublayer");
	  	  if(isSubLayer){
	  		var offset = $(".sublayers").offset();
	  		x = x - offset.left;
	      	y = y - offset.top;
	  	  }
      	
	      var isController = $(this).hasClass("controller");
	      if(isController){
	      	var offset = $(".elements").offset();
	      	x = x - offset.left;
	      	y = y - offset.top;
	      }
	      showPopupMenu(id, x, y);
	      return false; 
	    } 
	    return true; 
	  });
	
//		$(".popup-menu-trigger").on('taphold', function(e){ 
//		      var id = $(this).attr("popupMenuId");
//		      var x = e.pageX;
//		      var y = e.pageY;					  
//		      showPopupMenu(id, x, y);
//		      return false; 
//		 });
}

function initTelegramFilters(){
	$("#telegram-priorities").buttonset();
	$("#telegram-types").buttonset();
	
	$("#telegrams-filters-on-page").spinner({
	      min: 5,
	      max: 1000,
	      step: 5,
	      start: 5,
	      numberFormat: "n",
	      stop: function( event, ui ) {
	    	  $("#telegrams-filters-submit-button").click();
	      }
	 });
	
	$("#telegram-filter-date-from, #telegram-filter-date-to").datepicker({
        defaultDate: "+1w",
        numberOfMonths: 1,
        dateFormat: 'yy-mm-dd',
        onSelect: function( selectedDate ) {
        	$("#telegrams-filters-submit-button").click();
        },
        onClose: function(){
           
        }
    });
		
	$("#telegrams-filters-form :input").change(function(){
		$("#telegrams-filters-submit-button").click();
	});
	
}

function initTelegramDetails(){
	$(".telegram-details").off("click");
	$(".telegram-details").click(function(e){
		hideTelegramDetails();
	});
}

function showTelegramDetails(){
	initTelegramDetails();
	$(".telegram-details").show();
}

function hideTelegramDetails(){
	$(".telegram-details").hide();
}