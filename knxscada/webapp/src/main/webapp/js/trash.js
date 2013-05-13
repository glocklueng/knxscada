function initTrash() {
	$("#trash-icon").droppable({
		hoverClass : "over-trash",
		accept : ".removable",
		greedy : true,
		tolerance: 'pointer',
		drop : function(event, ui) {
						
			var callback = $(ui.draggable).attr("callback");
			var elementid = $(ui.draggable).attr("elementid");
			
			if (typeof callback !== 'undefined' && callback !== false) {
				removeVisualisationElement(callback, elementid);
			}
			$(ui.draggable).addClass("removed");
			$(ui.draggable).remove();
			hideTrash();
		}
	});
}

function showTrash() {
	$("#trash").show("fade", {}, 200);
}
function hideTrash() {
	$("#trash").hide("fade", {}, 200);
}

function makeElementRemovable(element) {
	$(element).addClass("removable");
	$(element).draggable({
		scroll : true,
		start : function(event, ui) {
			var isRemovable = $(this).hasClass("removable");
			if (isRemovable) {
				showTrash();
				$(this).css({
					"opacity" : "0.5"
				});
			}
		},
		stop : function(event, ui) {
			var isRemovable = $(this).hasClass("removable");
			if (isRemovable) {
				hideTrash();
				$(this).css({
					"opacity" : "1"
				});
			}
		}
	});
}