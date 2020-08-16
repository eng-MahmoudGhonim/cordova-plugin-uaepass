var exec = require('cordova/exec');


    exports._show=function(url, successCallback, errorCallback, loading) {
    		if(loading){
    			exec(successCallback, errorCallback, 'UAEpassPlugin', 'show', [url, loading]);
    		}
    		else{
    			exec(successCallback, errorCallback, 'UAEpassPlugin', 'show', [url]);
    		}
    	};
		exports._subscribeCallback=function(successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, 'UAEpassPlugin', 'subscribeCallback', []);
	};