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
import com.sunway.android.memoapp.view.DrawingMemoActivity;
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


        Toast.makeText(arg0, "Reminder received!", Toast.LENGTH_LONG).show();
        createNotification(arg0, intent.getExtras().getString(C.INPUT_TITLE),
                intent.getExtras().getString(C.INPUT_DETAILS), "New Reminder",
                intent.getExtras().getInt(C.MEMO_ID),
                intent.getExtras().getString(C.MEMO_TYPE));

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert, int memoid, String memoType) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        if (memoType.equals(C.TEXT_MEMO)) {
            intent = new Intent(context, TextDetailsMemoActivity.class)
                    .putExtra(C.ACTION_MODE, C.EDIT);

        } else if (memoType.equals(C.DRAWING_MEMO)) {
            intent = new Intent(context, DrawingMemoActivity.class)
                    .putExtra(C.ACTION_MODE, C.EDITDRAWING);
        }
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.putExtra(C.MEMO_ID, memoid);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, memoid,
                intent, 0);


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