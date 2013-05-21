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
	loadElementsPanel();
	loadSettingsPanel(); 
	setElementPanelVisible(); 
	showElementPanel();
}


function onMessage(event){
	if (event.data) {						
		var json = $.secureEvalJSON(event.data);
		console.log(json.msg);
	};
};