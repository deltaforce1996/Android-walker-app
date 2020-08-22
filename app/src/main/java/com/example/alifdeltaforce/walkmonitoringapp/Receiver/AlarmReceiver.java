package com.example.alifdeltaforce.walkmonitoringapp.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.alifdeltaforce.walkmonitoringapp.AlertActivity;
import com.example.alifdeltaforce.walkmonitoringapp.R;

/**
 * Created by octoboy on 22/11/2557.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private boolean num = true;

    @Override
    public void onReceive(Context context, Intent arg1) {

        Intent i = new Intent(context, AlertActivity.class);
        i.putExtra("NUMBERS",num);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm actived!")
                .setContentText("Wake UP")
                .setSound(sound)
                .setVibrate(new long[] { 500, 1000, 500 })
                .setLights(Color.BLUE, 3000, 3000)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }

}