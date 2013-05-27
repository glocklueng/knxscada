$(function(){
					
	WebSocketServer.connect("");
	WebSocketServer.setOnMessageFn(onMessage);

	$(document).bind("contextmenu",function(e){
		return false;
	});
	
	load();
	initProjectName();
	initLayers();
	initSubLayers();	        				        		
	
	loadElementsPanel();
	loadSettingsPanel();
	loadControllers();
	
});

function load(){
	initPopupMenu();
	initTrash();
	hideLoadingPanel();
	$("#content").css("overflow","hidden");
	$(window).resize();
}

function loadAfterUpdateProject(){
	load();
	initProjectName();
	hideDialog();
}

function loadAfterUpdateLayer(){
	load();
	initLayers();
	initSubLayers();
	hideDialog();
}

function loadLayer(){
	load(); 
	initSubLayers(); 
	loadElementsPanel(); 
	loadSettingsPanel();
	loadControllers();
}

function loadSubLayer(){
	load();  
	loadElementsPanel(); 
	loadSettingsPanel();
	loadControllers();
}

function loadElements(){
	load();  
	hideDialog();
	loadSettingsPanel();
	initDraggableElements();
	setElementPanelVisible(); 
	showElementPanel();
	loadControllers();
}


function onMessage(event){
	if (event.data) {						
		var object = $.secureEvalJSON(event.data);
		console.log(event.data);
		if(object.operation != 'undefined'){
			if(object.operation == "update"){
				if(object.type == "compound-button"){
					updateCompoundButtonValue(object.id, object.value);
				}else if(object.type == "slider"){
					updateSliderValue(object.id, object.value);
				}else if(object.type == "value-viewer"){
					updateValueViewerValue(object.id, object.value);
				}
			}
		}
	};
};