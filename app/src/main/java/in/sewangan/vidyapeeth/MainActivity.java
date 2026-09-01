package in.sewangan.vidyapeeth;

import android.Manifest;import android.app.*;import android.os.*;import android.content.pm.PackageManager;import android.webkit.*;import androidx.appcompat.app.AppCompatActivity;import androidx.core.app.ActivityCompat;import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
 WebView web;
 @Override public void onCreate(Bundle b){super.onCreate(b); if(Build.VERSION.SDK_INT>=21){getWindow().setStatusBarColor(0xFFF47B20);getWindow().setNavigationBarColor(0xFFFFFFFF);} web=new WebView(this);setContentView(web); WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setMediaPlaybackRequiresUserGesture(false);s.setAllowFileAccess(true);s.setAllowContentAccess(true);web.setWebViewClient(new WebViewClient());web.setWebChromeClient(new WebChromeClient(){@Override public void onPermissionRequest(PermissionRequest r){runOnUiThread(()->{if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)r.grant(r.getResources());else{pending=r;ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},44);}});}}); requestNativePermissions();web.loadUrl("file:///android_asset/www/index.html"); }
 PermissionRequest pending;
 void requestNativePermissions(){if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},44);if(Build.VERSION.SDK_INT>=33&&ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},45);}
 @Override public void onRequestPermissionsResult(int q,String[] p,int[] g){super.onRequestPermissionsResult(q,p,g);if(q==44&&pending!=null){if(g.length>0&&g[0]==PackageManager.PERMISSION_GRANTED)pending.grant(pending.getResources());else pending.deny();pending=null;}}
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
