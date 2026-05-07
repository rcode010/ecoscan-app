package com.ecoscan.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ecoscan.app.R;
import com.ecoscan.app.ui.main.MainActivity;

public class NotificationHelper {
    public static final String CHANNEL_ID = "expiry_notifications";
    public static final String CHANNEL_NAME = "Expiry Alerts";
    public static final String CHANNEL_DESC = "Notifications for products expiring soon";


    public static final String CHANNEL_ID_2 = "tip_of_the_day";
    public static final String CHANNEL_NAME_2 = "Tip of the day";
    public static final String CHANNEL_DESC_2 = "tip of the day";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);

            NotificationChannel channel_2 = new NotificationChannel(
                    CHANNEL_ID_2,
                    CHANNEL_NAME_2,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel_2.setDescription(CHANNEL_DESC_2);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.createNotificationChannel(channel_2);
            }
        }
    }

    public static void sendExpiryNotification(Context context, String itemName, int notificationId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Expiring Soon!")
                .setContentText(itemName + " will expire in 2 days. Use it soon to save money!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(notificationId, builder.build());
        } catch (SecurityException e) {
            // Handle the case where notification permission is not granted on Android 13+
            e.printStackTrace();
        }
    }
}