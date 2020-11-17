var exec = require('cordova/exec');


    exports._show=function(url, successCallback, errorCallback, isDevelopment) {
    		if(isDevelopment){
    			exec(successCallback, errorCallback, 'UAEpassPlugin', 'show', [url, isDevelopment]);
    		}
    		else{
    			exec(successCallback, errorCallback, 'UAEpassPlugin', 'show', [url]);
    		}
    	};
		exports._subscribeCallback=function(successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, 'UAEpassPlugin', 'subscribeCallback', []);
	};
