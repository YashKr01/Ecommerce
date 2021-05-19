package com.shopping.bloom.notis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shopping.bloom.R;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.NotificationModel;
import com.shopping.bloom.utils.LoginManager;

import java.util.Date;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    LoginManager loginManager;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        loginManager = new LoginManager(this);
        loginManager.setIs_firebase_token_changed(true);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        loginManager = new LoginManager(this);
        setNotificationChannel();

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            if (loginManager.is_notification_on()) {
                
                sendNotification(title, body);

                String flag = remoteMessage.getData().get("flag");
                if (flag != null) {
                    if (flag.equals("1")) {
                        Date date = new Date();
                        System.out.println("flag = " + flag);
                        String dateString = String.valueOf(date);
                        NotificationModel notificationModel = new NotificationModel(dateString, title, body);
                        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
                            EcommerceDatabase.getInstance().notificationDao().insertNotification(notificationModel);
                        });
                    }
                }
            }

        }

    }

    private void setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "Notification", "MessageChannel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(String title, String body) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Notification")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.toolbar_logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(12, builder.build());
    }
}
