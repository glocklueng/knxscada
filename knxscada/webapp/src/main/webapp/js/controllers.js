function loadControllers(){
	loadCompoundButtons();
	loadSliders();
}

function loadCompoundButtons(){
	$(".compound-button").off("click");
	$(".compound-button").click(function(){
		var isOff = $(this).hasClass("compound-button-off");
		if(isOff){
			setCompoundButtonOn(this);
		}else{
			setCompoundButtonOff(this);
		}
	});	
}

function setCompoundButtonOn(button){
	$(button).addClass("compound-button-on");
	$(button).removeClass("compound-button-off");	
	
	var isSwitch = $(button).hasClass("switch");
	var isToggle = $(button).hasClass("toggle");
	
	if(isSwitch){
		$(button).children(".switch-thumb").html($(button).attr("onvalue"));
	}
	if(isToggle){
		$(button).children(".toggle-label").html($(button).attr("onvalue"));
	}
	
	var callback = $(button).attr("callback");
	setCompoundButtonState(callback, "on");
}

function setCompoundButtonOff(button){
	$(button).addClass("compound-button-off");
	$(button).removeClass("compound-button-on");
	
	var isSwitch = $(button).hasClass("switch");
	var isToggle = $(button).hasClass("toggle");
	if(isSwitch){
		$(button).children(".switch-thumb").html($(button).attr("offvalue"));
	}
	if(isToggle){
		$(button).children(".toggle-label").html($(button).attr("offvalue"));
	}
	
	var callback = $(button).attr("callback");
	setCompoundButtonState(callback, "off");
}

function setCompoundButtonState(callback, s){
	
	var data = {
			state: s
	};
	sendJSONData(callback, data);
}

function loadSliders(){
	$(".controller-slider").each(function(){
		var val = $(this).attr("value");
		if(val == 'undefined'){
			val = 0;
		}
		initSlider(this, val);
		var isLightSlider = $(this).hasClass("light-controller-slider");
		if(isLightSlider){
			setSliderImage(this, val);
		}else{
			setSliderValue(this, val);
		}
		
	});
}

function initSlider(slider, initValue){
	$( slider ).slider({
	      range: "min",
	      value: initValue,
	      min: 0,
	      max: 100,
	      slide: function( event, ui ) {
	    	var isLightSlider = $(this).hasClass("light-controller-slider");
	    	$(this).attr("value", ui.value);
	    	if(isLightSlider){
	    		setSliderImage(this, ui.value);
	    	}else{
	    		setSliderValue(this, ui.value);
	    	}
	      },
	      stop: function(event, ui){
	    	  var callback = $(this).attr("callback");
	    	  setSliderPosition(callback, ui.value);
	      }
	    });
}

function setSliderValue(slider, value){
	$(slider).parent().children( ".controller-value" ).html( value + " %");
}

function setSliderImage(slider, value){
	var showClass = "";
	
	if(value == 0){
		showClass = "slider_off";
	}else if(value > 0 && value < 10){
		showClass = "slider_10";
	}else if(value >= 10 && value < 20){
		showClass = "slider_20";
	}else if(value >= 20 && value < 30){
		showClass = "slider_30";
	}else if(value >= 30 && value < 40){
		showClass = "slider_40";
	}else if(value >= 40 && value < 50){
		showClass = "slider_50";
	}else if(value >= 50 && value < 60){
		showClass = "slider_60";
	}else if(value >= 60 && value < 70){
		showClass = "slider_70";
	}else if(value >= 70 && value < 80){
		showClass = "slider_80";
	}else if(value >= 80 && value < 90){
		showClass = "slider_90";
	}else if(value >= 90){
		showClass = "slider_100";
	}
	
	$(slider).parent().children( ".controller-value" ).children().each(function(){
		var hasClass = $(this).hasClass(showClass);
		if(hasClass){
			$(this).show();
		}else{
			$(this).hide();
		}
	});
}

function setSliderPosition(callback, position){
	var data = {
			value: position
	};
	sendJSONData(callback, data);
}