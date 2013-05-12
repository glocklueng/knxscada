function initProjectDialog(addButtonLabel, cancelButtonLabel){
	
	initProjectImageFileChooser();
	
	var dialog_buttons = {}; 
	
	dialog_buttons[addButtonLabel] = function(){ 
		$("#project-form-submit-button").click();
 		$(this).dialog('close');
	};
	
	dialog_buttons[cancelButtonLabel] = function(){ 
		$(this).dialog('close'); 
	};  
	
	$("#dialog-window").dialog({
		  autoOpen: false,
	      resizable: true,
	      width: 500,
	      height: 350,
	      draggable: true,
	      show: {
	        effect: "fade",
	        duration: 500
	      },
	      hide: {
	        effect: "fade",
	        duration: 500
	      },
	      modal: false,
	      buttons: dialog_buttons
	      
	});
}

function initLayerDialog(addButtonLabel, cancelButtonLabel){
	
	initLayerIconChooser();
	
	var dialog_buttons = {}; 
	
	dialog_buttons[addButtonLabel] = function(){ 
		$("#layer-form-submit-button").click();
	};
	
	dialog_buttons[cancelButtonLabel] = function(){ 
		$(this).dialog('close'); 
	};   
	
	$("#dialog-window").dialog({
		  autoOpen: false,
	      resizable: true,
	      width: 500,
	      height: 350,
	      draggable: true,
	      show: {
	        effect: "fade",
	        duration: 500
	      },
	      hide: {
	        effect: "fade",
	        duration: 500
	      },
	      modal: true,
	      buttons: dialog_buttons
	      
	 });
}

function initElementDialog(addButtonLabel, cancelButtonLabel){
	
	initElementChooserPreview();
	
	var dialog_buttons = {}; 
	
	dialog_buttons[addButtonLabel] = function(){ 
		$("#element-form-submit-button").click();
	};
	
	dialog_buttons[cancelButtonLabel] = function(){ 
		$(this).dialog('close'); 
	}; 
	
	$("#dialog-window").dialog({
		  autoOpen: false,
	      resizable: true,
	      width: 620,
	      height: 450,
	      draggable: true,
	      show: {
	        effect: "fade",
	        duration: 500
	      },
	      hide: {
	        effect: "fade",
	        duration: 500
	      },
	      modal: false,
	      buttons: dialog_buttons
	      
	    });
}

function initTelegramsDialog(cancelButtonLabel){
	
	var dialog_buttons = {}; 

	dialog_buttons[cancelButtonLabel] = function(){ 
		$(this).dialog('close'); 
	};  
	
	$("#dialog-window").dialog({
		  autoOpen: false,
	      resizable: true,
	      width: 500,
	      height: 350,
	      draggable: true,
	      show: {
	        effect: "fade",
	        duration: 500
	      },
	      hide: {
	        effect: "fade",
	        duration: 500
	      },
	      modal: false,
	      buttons: dialog_buttons
	});
}

function initRemoveDialog(yesButtonLabel, noButtonLabel){
	
	var dialog_buttons = {}; 
	
	dialog_buttons[yesButtonLabel] = function(e){ 
		var callback = $("#delete-message").attr("callback");
		var dialog = this;
		
		$.ajax({
			url : callback,
			type : 'get',
			cache : false,
			success: function(data, textStatus, jqXHR){
				
				var msg = $.secureEvalJSON(data);
				
				var type = msg.type;
				var select = msg.select;
				var remove = msg.remove;
				
				if(remove != 'undefined'){
					$("#"+remove).hide("fade",{}, 300, function(){ 
						$(this).remove();		
					});
				}
				
				if(type == "project"){
					if(select == "other"){
						location.href = "?";
					}
				} else{
					if(select != 'undefined'){
						$("#"+select).children().children().click();
						$("#"+select).children().children().css("background", "#88c9e8");
					}
				}
				
				$(dialog).dialog('close');
	        }
		});
	};
	
	dialog_buttons[noButtonLabel] = function(e){ 
		$(this).dialog('close'); 
	};  
	

	$("#dialog-window").dialog({
		  autoOpen: false,
	      resizable: true,
	      draggable: true,
	      width: 400,
	      height: 160,
	      show: {
	        effect: "fade",
	        duration: 300
	      },
	      hide: {
	        effect: "fade",
	        duration: 300
	      },
	      modal: true,
	      buttons: dialog_buttons
	});
}

function showDialog(){
	var title = $(".dialogs").attr("title");
	$("#dialog-window").dialog( "option", "title", title );
	$("#dialog-window").dialog("open");
}

function hideDialog(){
	$("#dialog-window").dialog("close");
}