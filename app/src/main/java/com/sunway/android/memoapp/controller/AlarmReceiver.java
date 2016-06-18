package com.sunway.android.memoapp.controller;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.view.TextDetailsMemoActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context arg0, Intent intent) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(arg0, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Toast.makeText(arg0, "Alarm worked.", Toast.LENGTH_LONG).show();
        createNotification(arg0, intent.getExtras().getString(C.INPUT_TITLE), intent.getExtras().getString(C.INPUT_DETAILS), "Alert", intent.getExtras().getInt(C.TEXT_ID), intent.getExtras().getInt(C.PHOTOS));

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert, int memoid, int photosCount) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        /*
        PendingIntent notificIntent = PendingIntent.getActivity(context,notifyID,new Intent(context,ReminderActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);



        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(notifyID, PendingIntent.FLAG_UPDATE_CURRENT);
*/


        Intent intent = new Intent(context, TextDetailsMemoActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(C.INPUT_TITLE, msg)
                .putExtra(C.INPUT_DETAILS, msgText)
                .putExtra(C.PHOTOS, photosCount)
                .putExtra(C.ACTION_MODE, C.VIEW_MEMO_NOTIFICATION)
                .putExtra(C.TEXT_ID, memoid);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, memoid,
                intent, 0);

        System.out.println("LOGTAGYO PHOTOSCOUNT:" + photosCount);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(msgText)
                .setContentIntent(pendingIntent);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        mNotificationManager.notify(memoid, mBuilder.build());
    }


}