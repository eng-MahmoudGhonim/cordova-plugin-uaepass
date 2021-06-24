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
		System.err.println("Execute @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >:execute");

		if (action.equals("show") && args.length() > 0) {
			LOG.i(LOG_TAG, "Show Web View");
			final String url = args.getString(0);
			LOG.i(LOG_TAG, "UAEpassPlugin execute action Show"+url);

			Boolean isDevelopment = false;
			try {

				isDevelopment = args.getBoolean(1);

			} catch (Exception e) {
			}
			LOG.i(LOG_TAG, " Execute isDevelopment="+isDevelopment);


			if (!"".equals(url)) {
				LOG.i(LOG_TAG, " Execute Before call showWebView> Url is not null :  >"+url);
				showWebView(url, isDevelopment);
				JSONObject r = new JSONObject();
				r.put("responseCode", "ok");
				callbackContext.success(r);
			} else {
				LOG.i(LOG_TAG, " Execute Else with null URL ");
				callbackContext.error("Empty Parameter url");
			}

		} else if (action.equals("hide")) {
			LOG.i(LOG_TAG, "Execute else if  Hide Web View");
			results = args;
			hideWebView();
			JSONObject r = new JSONObject();
			r.put("responseCode", "ok");
			callbackContext.success(r);
		}
		else if (action.equals("subscribeCallback")) {
			LOG.i(LOG_TAG, "Execute subscribeCallback");
			subscribeCallbackContext = callbackContext;
		}

		else if (action.equals("subscribeExitCallback")) {
			LOG.i(LOG_TAG, "Execute Subscribing Cordova subscribeExitCallback");
			subscribeExitCallbackContext = callbackContext;
		}
		else if (action.equals("exitApp")) {
			LOG.i(LOG_TAG, "Execute Exiting app?");
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

	private void showWebView(final String url, Boolean isDevelopment) {
		LOG.i(LOG_TAG, "showWebView Url: " + url);
		//System.err.println(" showWebView @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Ste> >"+url);

		LOG.i(LOG_TAG,"showWebView UAEPassActivity.class > >"+ UAEPassActivity.class);

		Intent i = new Intent(this.cordova.getActivity(), UAEPassActivity.class);
		LOG.i(LOG_TAG,"showWebView showWebViewobject of intent  > >"+i);
		i.putExtra("url", url);
		i.putExtra("isDevelopment", isDevelopment);
		LOG.i(LOG_TAG,"showWebView showWebViewobject this > >"+this);
		this.cordova.setActivityResultCallback(this);
		this.cordova.getActivity().startActivityForResult(i, 1111);
	}

	private void hideWebView() {
		LOG.i(LOG_TAG, "hideWebView begin");
		if (subscribeCallbackContext != null) {
			LOG.i(LOG_TAG, "hideWebViewCalling subscribeCallbackContext success");
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
			LOG.i(LOG_TAG, "onActivityResult", data);
			JSONObject r = new JSONObject();
			LOG.i(LOG_TAG, "onActivityResult result> >"+r);

			try {
				r.put("url", data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.e(LOG_TAG, "onActivityResult exeception  >"+e);
			}
			subscribeCallbackContext.success(r);			
		}
	}


	
}
