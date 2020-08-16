package cordova.plugin.uaepassplugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.LOG;

/**
 * This class echoes a string called from JavaScript.
 */
public class UAEpassPlugin extends CordovaPlugin {

// default created 
	// UAE Pass plugin-uaepassplugin
	

	private static final String LOG_TAG = "UAEpassPlugin";
	private static CallbackContext subscribeCallbackContext = null;
	private static CallbackContext subscribeExitCallbackContext = null;
	private static JSONArray results = null;

	public UAEpassPlugin() {

	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 *
	 * @param cordova
	 *            The context of the main Activity.
	 * @param webView
	 *            The CordovaWebView Cordova is running in.
	 */
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		System.err.println("UAEpassPlugin initialize @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >1:");

		super.initialize(cordova, webView);
	}

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action
	 *            The action to execute.
	 * @param args
	 *            JSONArry of arguments for the plugin.
	 * @param callbackContext
	 *            The callback id used when calling back into JavaScript.
	 * @return True if the action was valid, false if not.
	 */
	 @Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		System.err.println("UAEpassPlugin execute @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >:execute");

		if (action.equals("show") && args.length() > 0) {
			LOG.d(LOG_TAG, "Show Web View");
			final String url = args.getString(0);
			System.err.println("UAEpassPlugin execute @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+url);

			Boolean shouldShowLoading = false;
			try {
				shouldShowLoading = args.getBoolean(1);
			} catch (Exception e) {

			}
			
			if (!"".equals(url)) {
				System.err.println("UAEpassPlugin execute Before call showWebView @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+url);
				showWebView(url, shouldShowLoading);
				JSONObject r = new JSONObject();
				r.put("responseCode", "ok");
				callbackContext.success(r);
			} else {
				callbackContext.error("Empty Parameter url");
			}

		} else if (action.equals("hide")) {
			LOG.d(LOG_TAG, "Hide Web View");
			results = args;
			hideWebView();
			JSONObject r = new JSONObject();
			r.put("responseCode", "ok");
			callbackContext.success(r);
		}
		else if (action.equals("subscribeCallback")) {
			LOG.d(LOG_TAG, "Subscribing Cordova CallbackContext");
			subscribeCallbackContext = callbackContext;
		}

		else if (action.equals("subscribeExitCallback")) {
			LOG.d(LOG_TAG, "Subscribing Cordova ExitCallbackContext");
			subscribeExitCallbackContext = callbackContext;
		}
		else if (action.equals("exitApp")) {
			LOG.d(LOG_TAG, "Exiting app?");
			if (subscribeExitCallbackContext != null) {
				subscribeExitCallbackContext.success();
				subscribeExitCallbackContext = null;
			}
			this.cordova.getActivity().finish();
		}
		else {
			return false;
		}
		return true;
	}

	private void showWebView(final String url, Boolean shouldShowLoading) {
		LOG.d(LOG_TAG, "Url: " + url);
		System.err.println("UAEpassPlugin showWebView @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+url);
		
		System.err.println("UAEpassPlugin showWebView UAEPassActivity.class @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+UAEPassActivity.class);

		Intent i = new Intent(this.cordova.getActivity(), UAEPassActivity.class);
		System.err.println("UAEpassPlugin showWebView object of intent  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+i);
		i.putExtra("url", url);
		i.putExtra("shouldShowLoading", shouldShowLoading);
		System.err.println("UAEpassPlugin showWebView this  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+this);
		this.cordova.setActivityResultCallback(this);
		this.cordova.getActivity().startActivityForResult(i, 1111);
	}

	private void hideWebView() {
		LOG.d(LOG_TAG, "hideWebView");
		if (subscribeCallbackContext != null) {
			LOG.d(LOG_TAG, "Calling subscribeCallbackContext success");
			subscribeCallbackContext.success(results);
			subscribeCallbackContext = null;
			results = null;
		}
		this.cordova.getActivity().finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK && requestCode == 1111) {
			String data = intent.getData().toString();
			LOG.d("Data", data);
			JSONObject r = new JSONObject();
			System.err.println("UAEpassPlugin onActivityResult @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+r);

			try {
				r.put("url", data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			subscribeCallbackContext.success(r);			
		}
	}


	
}
