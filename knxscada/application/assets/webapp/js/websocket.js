var WebSocketServer = {
	connect : function(contextPath) {
		var baseUrl = document.location.protocol+"//"+document.location.host + contextPath;
		var location = baseUrl.replace('http://', 'ws://').replace('https://', 'wss://')+ "/wscommunication/";

		this._ws = new WebSocket(location);
		this._ws.onopen = this._onopen;
		this._ws.onclose = this._onclose;
		this._ws.onmessage = this._onmessage;
	},

	_onopen : function() {
		var msg = {
				msg :'websockets are open for communications!'
		};
		WebSocketServer.sendJSON(msg);
	},
	
	_onclose : function(event) {
		this._ws = null;
	},
	
	_onmessage : function(event) {
	},
	
	_send : function(message) {
		if (this._ws) {
			this._ws.send(message);
		}
	},

	send : function(text) {
		if (text != null && text.length > 0)
			this._send(text);
	},
	
	sendJSON : function(obj) {
		var msg = $.toJSON( obj );
		this._send(msg);
	},
	
	setOnMessageFn : function(fn) {
		this._ws.onmessage = fn;
	},
	
	setOnOpenFn : function(fn) {
		this._ws.onopen = fn;
	},
	
	setOnCloseFn : function(fn) {
		this._ws.onclose = fn;
	}
};