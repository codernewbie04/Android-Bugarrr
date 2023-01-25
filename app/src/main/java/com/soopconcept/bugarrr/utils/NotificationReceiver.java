package com.soopconcept.bugarrr.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.activity.SplashAct;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("SleepNotification")) {
            sleepNotification(context);
        }
        if (intent.getAction().equals("WakeUpNotification")) {
            wakeUpNotification(context);
        }
        if (intent.getAction().equals("DrinkNotification")) {
            drinkNotification(context);
        }
        if (intent.getAction().equals("StandNotification")) {
            standNotification(context);
        }
    }

    private void standNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent r_intent = new Intent(context, SplashAct.class);
        r_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 100, new Intent[]{r_intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sleep")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Kamu Belum bergerak?")
                .setContentText("Yuk luangkan waktu untuk bergerak selama 1 menit!")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());
    }

    private void drinkNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent r_intent = new Intent(context, SplashAct.class);
        r_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 100, new Intent[]{r_intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sleep")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Sudah minum?")
                .setContentText("Jangan lupa untuk tetap terhidrasi, catat minum kamu di aplikasi.")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());
    }

    private void wakeUpNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent r_intent = new Intent(context, SplashAct.class);
        r_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 100, new Intent[]{r_intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sleep")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Selamat Pagi!")
                .setContentText("Catat waktu bangun jika kamu sudah bangun.")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());
    }

    private void sleepNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Intent r_intent = new Intent(context, SplashAct.class);
        r_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 100, new Intent[]{r_intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sleep")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Selamat malam.")
                .setContentText("Sekarang waktunya tidur, catat waktu tidur jika kamu sudah bangun.")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());
    }
}
