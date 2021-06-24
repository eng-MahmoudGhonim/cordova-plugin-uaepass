package cordova.plugin.uaepassplugin;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import <YOUR-PROJECT-PACKGENAME>.R


import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cordova.LOG;


public class UAEPassActivity extends Activity {
	private static final String LOG_TAG = "UAEPassActivity";
	private String mSuccessURLUAEPass = "";
	private String mFailureURLUAEPass = "";
	Logger logger = Logger.getLogger(UAEPassActivity.class.getName());
	//@Override
//	protected void onResume() {super.onResume();
//		logger.log(Level.INFO," @onResume");
//		if (mSuccessURLUAEPass!=null &&!mSuccessURLUAEPass.isEmpty()) {
//			//mSuccessURLUAEPass = null;
//			LOG.i(LOG_TAG, "@mSuccessURLUAEPass is not null and mSuccessURLUAEPass ="+mSuccessURLUAEPass);
//			webView.loadUrl(mSuccessURLUAEPass);
//			mSuccessURLUAEPass = null;
//		}
//
//	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LOG.i(LOG_TAG, "onNewIntent is ="+intent.getDataString());
		String intentData=intent.getDataString();
		if (intentData!=null &&intentData.contains("uaepassrtaapp://dubaidrivesuccess")) {
			//mSuccessURLUAEPass = null;
			LOG.i(LOG_TAG, "onNewIntent  mSuccessURLUAEPass ="+mSuccessURLUAEPass);
			webView.loadUrl(mSuccessURLUAEPass);
			mSuccessURLUAEPass = null;
		}else if(intentData!=null &&intentData.contains("uaepassrtaapp://dubaidrivefailure")){
			LOG.i(LOG_TAG, "onNewIntent   mFailureURLUAEPass ="+mFailureURLUAEPass);
			webView.loadUrl(mFailureURLUAEPass);
			mFailureURLUAEPass = null;
		}

	}

	private WebView webView;
	private AlertDialog webViewAlertDialog;
	@SuppressLint("SetJavaScriptEnabled")
	private void login(String url) {

		CookieManager.getInstance().removeAllCookie();
		webViewAlertDialog = new AlertDialog.Builder(this).create();
		webViewAlertDialog.setTitle(null);

		RelativeLayout dialogView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.uaepass_login, null);
		webViewAlertDialog.setView(dialogView);
		webViewAlertDialog.setCancelable(true);
		webView = dialogView.findViewById(R.id.webView);


		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAppCacheEnabled(false);
		webView.clearCache(true);
		webView.clearHistory();
		if (Build.VERSION.SDK_INT >= 26) {
			settings.setSafeBrowsingEnabled(false);
		}

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LOG.i(LOG_TAG,  "setWebViewClient show URL@@  :" + url);
				if (url.contains("uaepass://digitalid")) {
					if(isDevelopment) {
						url = url.replace("uaepass://", "uaepassqa://");
						LOG.i(LOG_TAG, "setWebViewClient @replace uaepass with uaepassqa  :cause isDevelopment=" + isDevelopment);
					}else{
						LOG.i(LOG_TAG,  "setWebViewClient @donot replace uaepass with uaepassqa  :cause isDevelopment=" + isDevelopment);
					}
					LOG.i(LOG_TAG,  "setWebViewClient openinng native URL  :" + url);
					mSuccessURLUAEPass = getQueryParameterValue(url, "successurl");
					 mFailureURLUAEPass = getQueryParameterValue(url, "failureurl");
					if (url.contains("successurl")) {
						LOG.i(LOG_TAG,  "setWebViewClient OnSuccessurl is:" + url);

						url = replaceUriParameter(Uri.parse(url), "successurl", "uaepassrtaapp://dubaidrivesuccess").toString();
					}
					if (url.contains("failureurl")) {
						url = replaceUriParameter(Uri.parse(url), "failureurl", "uaepassrtaapp://dubaidrivefailure").toString();
						LOG.i(LOG_TAG, "setWebViewClient  @Failureurl is:" + url);
					}
					LOG.i(LOG_TAG,  "setWebViewClient OnSuccessurl is @@@@@@@:" + url);
					Intent launchIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
					PackageManager packageManager = getPackageManager();
					if (launchIntent.resolveActivity(packageManager) != null) {
						startActivity(launchIntent);

					} else {
						//Log.d(TAG, "No Intent available to handle action");
						//GlobalUtils.openUAEPassAppInPlayStore(SingleLoginActivity.this);
						webViewAlertDialog.dismiss();
					}
					return true;
				} else {
					LOG.i(LOG_TAG,  "setWebViewClient else UAE Pass start checking :" + url);
					//	if (url.startsWith("uaepassdemoapp://dubaidrive")) {
					if (url.contains("UAEPassCallback/uaePassRedirect") && url.contains("code=")) {

						String code = getQueryParameterValue(url, "code");
						String state_ = getQueryParameterValue(url, "state");
						String error = getQueryParameterValue(url, "error");
						LOG.i(LOG_TAG,  "setWebViewClient UAEPassCallback/uaePassRedirect code is  " + code);
						if (error != null) {
							webViewAlertDialog.dismiss();
							if (error.contains("access_denied")) {
								// access _deneid
								LOG.i(LOG_TAG,  "setWebViewClient @access denied :");
							} else {
								// some error heepend here
							}
							webViewAlertDialog.dismiss();
							return false;
						}

						webViewAlertDialog.dismiss();

						Intent i = new Intent();
						Uri data = Uri.parse(url);
						i.setData(data);
						setResult(Activity.RESULT_OK, i);
						finish();

						return true;
					} else if (url.contains("UAEPassCallback/uaePassRedirect"))						{

						LOG.i(LOG_TAG,  "setWebViewClient UAE Pass without code return back :" + url);


						String error = getQueryParameterValue(url, "error");
						if (error != null) {


								// access _deneid
								webViewAlertDialog.dismiss();
								Intent i = new Intent();
								Uri data = Uri.parse(url);
								i.setData(data);
								setResult(Activity.RESULT_OK, i);
								finish();
								LOG.i(LOG_TAG, "setWebViewClient @access denied :");
								return true;
							}

							view.loadUrl(url);
						return false;
					} else {
						LOG.i(LOG_TAG,  "else load url :"+url);
						view.loadUrl(url);
						return false;
					}
				}
			}

			public void onPageFinished(WebView view, String url) {
				LOG.i(LOG_TAG,  "onPageFinished  url :"+url);
//    if(url.contains("retry.do")&&url.contains("tenantDomain=carbon.super")){
//		LOG.i(LOG_TAG,  "onPageFinished  user cancel : "+url);
//		webViewAlertDialog.dismiss();
//
//		Intent i = new Intent();
//		Uri data = Uri.parse(url);
//		i.setData(data);
//		setResult(Activity.RESULT_OK, i);
//		finish();
//	}


				super.onPageFinished(view, url);

			}
		});
		EditText edit = (EditText)dialogView.findViewById(R.id.editText);
		edit.setFocusable(true);
		edit.requestFocus();
		webView.loadUrl(url);

		webViewAlertDialog.show();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		String url = b.getString("url");
		LOG.i(LOG_TAG, "onCreate URL is:"+ url);
		try{
			isDevelopment = b.getBoolean("isDevelopment");
		}catch (Exception e){
			e.printStackTrace();
			LOG.e(LOG_TAG, "onCreate URL is:"+ e);
		}
		login(url);



	}
	boolean isDevelopment=false;
	private String getQueryParameterValue(String url, String queryParameter) {
		Uri uri = Uri.parse(url);
		String value = uri.getQueryParameter(queryParameter);
		LOG.i(LOG_TAG, "getQueryParameterValue value is:"+ value);
		return value;
	}



	private static Uri replaceUriParameter(Uri uri, String key, String newValue) {
		final Set<String> params = uri.getQueryParameterNames();
		final Uri.Builder newUri = uri.buildUpon().clearQuery();
		for (String param : params) {
			newUri.appendQueryParameter(param,
					param.equals(key) ? newValue : uri.getQueryParameter(param));
		}
		LOG.i(LOG_TAG, "replaceUriParameter newUri is:"+ newUri);
		return newUri.build();
	}
}
