# cordova-plugin-uaepass Android Platform


## Step 1 :Setup

* How To Add: `cordova plugin add https://github.com/eng-MahmoudGhonim/cordova-plugin-uaepass.git`
* How To Remove :`cordova plugin remove cordova.plugin.uaepassplugin`
* Must build platform after Add | Remove :`cordova build andoird`

## Step 2 :UAEPassActivity need to be added to AndroidManifest.xml
![](img/manifest.png)
 `<activity android:label="UAE Pass Activity" android:launchMode="singleTask" android:name="cordova.plugin.uaepassplugin.UAEPassActivity" android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen">
            <intent-filter>
                <data android:scheme="uaepassdemoapp" />
                <data android:host="dubaidrive" />
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
</activity>`
## step 3 :Create new layout "uaepass_login.xml"
![](img/layout.png)
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

## step 4 :UAEPassActivity.java
update import R according to your android Package name 
`import <YOUR-PROJECT-PACKGENAME>.R;`
