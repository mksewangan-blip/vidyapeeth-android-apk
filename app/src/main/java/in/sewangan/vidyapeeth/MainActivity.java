package in.sewangan.vidyapeeth;

import android.Manifest;
import android.app.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.webkit.*;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {
    private static final String CHANNEL_ID = "vidyapeeth_notifications";
    private static final AtomicInteger NOTIFICATION_ID = new AtomicInteger(1000);
    private WebView webView;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        createNotificationChannel();
        requestNotificationPermission();
        webView = new WebView(this);
        setContentView(webView, new FrameLayout.LayoutParams(-1,-1));
        WebSettings s=webView.getSettings();
        s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setAllowFileAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false); s.setBuiltInZoomControls(false); s.setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override public void onPermissionRequest(PermissionRequest request){
                runOnUiThread(() -> {
                    if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA},200);
                    }
                    request.grant(request.getResources());
                });
            }
        });
        webView.addJavascriptInterface(new NativeBridge(),"VidyapeethAndroid");
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=26){
            NotificationChannel c=new NotificationChannel(CHANNEL_ID,getString(R.string.notification_channel_name),NotificationManager.IMPORTANCE_HIGH);
            c.setDescription(getString(R.string.notification_channel_description)); c.enableVibration(true);
            getSystemService(NotificationManager.class).createNotificationChannel(c);
        }
    }
    private void requestNotificationPermission(){
        if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},201);
    }
    public class NativeBridge {
        @JavascriptInterface public void notify(String title,String message){ runOnUiThread(() -> showNotification(title,message)); }
        @JavascriptInterface public boolean isAndroidApp(){ return true; }
        @JavascriptInterface public String getPushToken(){ return getSharedPreferences("push", MODE_PRIVATE).getString("fcm_token", ""); }
    }
    private void showNotification(String title,String message){
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,CHANNEL_ID):new Notification.Builder(this);
        b.setSmallIcon(R.drawable.ic_notification).setContentTitle(title==null?"Sewangan Vidyapeeth":title).setContentText(message).setStyle(new Notification.BigTextStyle().bigText(message)).setAutoCancel(true);
        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID.incrementAndGet(),b.build());
    }
    @Override public void onBackPressed(){ if(webView!=null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }
}
