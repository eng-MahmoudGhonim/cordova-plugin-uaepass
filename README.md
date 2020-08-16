# cordova-plugin-uaepass

## Step 1

How To Add: `cordova plugin add https://github.com/eng-MahmoudGhonim/cordova-plugin-uaepass.git`

How To Remove :`cordova plugin remove cordova.plugin.uaepassplugin`

must build platform after Add|Remove :
`cordova build andoird|ios`

## Step 2 :UAEPassActivity must added in AndroidManifest.xml
 `<activity android:label="UAE Pass Activity" android:launchMode="singleTask" android:name="cordova.plugin.uaepassplugin.UAEPassActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen">
            <intent-filter>
                <data android:scheme="uaepassdemoapp" />
                <data android:host="dubaidrive" />
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>`
## step 3 :create new layout "uaepass_login.xml"
`<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    <!-- Webview does not get the focus directly so had to do this hack. Do not remove the edit text.-->
    <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
    <WebView
        android:id="@+id/webView"
        android:focusable="true"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</RelativeLayout>`
