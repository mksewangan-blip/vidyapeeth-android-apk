package in.sewangan.vidyapeeth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.concurrent.atomic.AtomicInteger;

public class VidyapeethMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "vidyapeeth_notifications";
    private static final AtomicInteger IDS = new AtomicInteger(2000);

    @Override public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences("push", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = "Sewangan Vidyapeeth";
        String body = "नई सूचना प्राप्त हुई है।";
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null) title = remoteMessage.getNotification().getTitle();
            if (remoteMessage.getNotification().getBody() != null) body = remoteMessage.getNotification().getBody();
        }
        if (remoteMessage.getData().containsKey("title")) title = remoteMessage.getData().get("title");
        if (remoteMessage.getData().containsKey("body")) body = remoteMessage.getData().get("body");
        showNotification(title, body);
    }

    private void showNotification(String title, String body) {
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel c = new NotificationChannel(CHANNEL_ID, "Vidyapeeth Notifications", NotificationManager.IMPORTANCE_HIGH);
            c.enableVibration(true);
            nm.createNotificationChannel(c);
        }
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        android.app.Notification.Builder b = Build.VERSION.SDK_INT >= 26 ? new android.app.Notification.Builder(this, CHANNEL_ID) : new android.app.Notification.Builder(this);
        b.setSmallIcon(R.drawable.ic_notification).setContentTitle(title).setContentText(body)
          .setStyle(new android.app.Notification.BigTextStyle().bigText(body)).setContentIntent(pi).setAutoCancel(true);
        nm.notify(IDS.incrementAndGet(), b.build());
    }
}
