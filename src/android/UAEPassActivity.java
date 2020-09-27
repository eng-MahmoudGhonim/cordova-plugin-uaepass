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

import <YOUR-PROJECT-PACKGENAME>.R;


import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UAEPassActivity extends Activity {
	private String mSuccessURLUAEPass = "";
	private String mFailureURLUAEPass = "";
	Logger logger = Logger.getLogger(UAEPassActivity.class.getName());
	@Override
	protected void onResume() {super.onResume();
		logger.log(Level.INFO," @onResume");
		if (mSuccessURLUAEPass!=null &&!mSuccessURLUAEPass.isEmpty()) {
			//mSuccessURLUAEPass = null;
			logger.log(Level.INFO,"@mSuccessURLUAEPass is not null and mSuccessURLUAEPass ="+mSuccessURLUAEPass);
			webView.loadUrl(mSuccessURLUAEPass);
			mSuccessURLUAEPass = null;
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

				if (url.contains("uaepass://digitalid")) {
					url = url.replace("uaepass://", "uaepassqa://");
					mSuccessURLUAEPass = getQueryParameterValue(url, "successurl");
					String mFailureURLUAEPass = getQueryParameterValue(url, "failureurl");
					if (url.contains("successurl")) {
						logger.log(Level.INFO, "@Successurl :" + url);

						url = replaceUriParameter(Uri.parse(url), "successurl", "uaepassdemoapp://dubaidrive").toString();
					}
					if (url.contains("failureurl")) {
						url = replaceUriParameter(Uri.parse(url), "failureurl", "uaepassdemoapp://dubaidrive").toString();
						logger.log(Level.INFO, "@Failureurl :" + url);
					}
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
					logger.log(Level.INFO, "UAE Pass start checking :" + url);
					//	if (url.startsWith("uaepassdemoapp://dubaidrive")) {
					if (url.contains("UAEPassCallback/uaePassRedirect") && url.contains("code=")) {
						String code = getQueryParameterValue(url, "code");
						String state_ = getQueryParameterValue(url, "state");
						String error = getQueryParameterValue(url, "error");
						if (error != null) {
							webViewAlertDialog.dismiss();
							if (error.contains("access_denied")) {
								// access _deneid
								logger.log(Level.INFO, "@access denied :");
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

						logger.log(Level.INFO, "UAE Pass without code return back @@@@@@@@@@@@@@ :" + url);


						String error = getQueryParameterValue(url, "error");
						if (error != null) {

							if (error.contains("access_denied")) {
								// access _deneid
								webViewAlertDialog.dismiss();
								Intent i = new Intent();
								Uri data = Uri.parse(url);
								i.setData(data);
								setResult(Activity.RESULT_OK, i);
								finish();
								logger.log(Level.INFO, "@access denied :");
								return true;
							}}

							view.loadUrl(url);
						return false;
					} else {
						logger.log(Level.INFO, "else @@@@@@@@@@@@@@ :");
						view.loadUrl(url);
						return false;
					}
				}
			}

			public void onPageFinished(WebView view, String url) {
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
		logger.log(Level.INFO,"@URL :"+ url);

		login(url);



	}

	private String getQueryParameterValue(String url, String queryParameter) {
		Uri uri = Uri.parse(url);
		String value = uri.getQueryParameter(queryParameter);
		return value;
	}



	private static Uri replaceUriParameter(Uri uri, String key, String newValue) {
		final Set<String> params = uri.getQueryParameterNames();
		final Uri.Builder newUri = uri.buildUpon().clearQuery();
		for (String param : params) {
			newUri.appendQueryParameter(param,
					param.equals(key) ? newValue : uri.getQueryParameter(param));
		}
		return newUri.build();
	}
}
