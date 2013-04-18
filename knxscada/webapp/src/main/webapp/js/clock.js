function updateClockDate (){ 	
    var currentTime = new Date ();
    var currentHours = currentTime.getHours ();
    var currentMinutes = currentTime.getMinutes ();
    var currentSeconds = currentTime.getSeconds ();
    var currentDay = currentTime.getDate();
    var currentMonth = currentTime.getMonth()+1;
    var currentYear = currentTime.getFullYear();
    currentHours = ( currentHours < 10 ? "0" : "" ) + currentHours;
    currentMinutes = ( currentMinutes < 10 ? "0" : "" ) + currentMinutes;
    currentSeconds = ( currentSeconds < 10 ? "0" : "" ) + currentSeconds;
    currentMonth = ( currentMonth < 10 ? "0" : "" ) + currentMonth;
    var currentTimeString = currentHours + ":" + currentMinutes + ":" + currentSeconds;
    var currentDateString = currentDay+"."+currentMonth+"."+currentYear;
    
    $("#clockdate").html(currentTimeString+"<br>"+currentDateString);
}

$(document).ready(function() {
	  
      setInterval( function() {
      var seconds = new Date().getSeconds();
      var sdegree = seconds * 6;
      var srotate = "rotate(" + sdegree + "deg)";
      
      $("#sec").css({"-moz-transform" : srotate, "-webkit-transform" : srotate, "-o-transform": srotate});
	  
      }, 1000 );
      
  
      setInterval( function() {
      var hours = new Date().getHours();
      var mins = new Date().getMinutes();
      var hdegree = hours * 30 + (mins / 2);
      var hrotate = "rotate(" + hdegree + "deg)";
      
      $("#hour").css({"-moz-transform" : hrotate, "-webkit-transform" : hrotate, "-o-transform": hrotate});
	  
      }, 1000 );


      setInterval( function() {
      var mins = new Date().getMinutes();
      var mdegree = mins * 6;
      var mrotate = "rotate(" + mdegree + "deg)";
      
      $("#min").css({"-moz-transform" : mrotate, "-webkit-transform" : mrotate, "-o-transform": mrotate});
	  
      }, 1000 );
      setInterval('updateClockDate()', 1000);
        
}); 