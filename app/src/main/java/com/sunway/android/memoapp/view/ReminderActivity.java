package com.sunway.android.memoapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.controller.AlarmReceiver;
import com.sunway.android.memoapp.util.C;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    private final BroadcastReceiver mybroadcast = new AlarmReceiver();
    DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm; //used to set alarm
    TextView info; //displays date and time reminder was set on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_main);

        info = (TextView) findViewById(R.id.info);
        pickerDate = (DatePicker) findViewById(R.id.pickerdate);
        pickerTime = (TimePicker) findViewById(R.id.pickertime);
        //creates calendar object
        Calendar now = Calendar.getInstance();
        //Set date picker to current date
        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);
        //Set time picker to current time
        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));
        //Create listener for button
        buttonSetAlarm = (Button) findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Calendar current = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                cal.set(pickerDate.getYear(),
                        pickerDate.getMonth(),
                        pickerDate.getDayOfMonth(),
                        pickerTime.getCurrentHour(),
                        pickerTime.getCurrentMinute(),
                        00);

                if (cal.compareTo(current) <= 0) {
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time for Reminder",
                            Toast.LENGTH_LONG).show();
                } else {
                    setAlarm(cal);
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setAlarm(Calendar targetCal) {


        info.setText("\n\n***\n"
                + "Reminder is set on " + targetCal.getTime() + "\n"
                + "***\n");

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                .putExtra(C.INPUT_TITLE, getIntent().getStringExtra(C.INPUT_TITLE))
                .putExtra(C.INPUT_DETAILS, getIntent().getStringExtra(C.INPUT_DETAILS))
                .putExtra(C.TEXT_ID, getIntent().getExtras().getInt(C.TEXT_ID))
                .putExtra(C.PHOTOS, getIntent().getExtras().getInt(C.PHOTOS));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), getIntent().getExtras().getInt(C.TEXT_ID), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent showMainActivity = new Intent(this, MainActivity.class)
                .putExtra(C.INPUT_TITLE, getIntent().getStringExtra(C.INPUT_TITLE).toString())
                .putExtra(C.INPUT_DETAILS, getIntent().getStringExtra(C.INPUT_DETAILS).toString())
                .putExtra(C.PHOTOS, getIntent().getExtras().getInt(C.PHOTOS))
                .putExtra(C.ACTION_MODE, C.REGISTER_REMINDER);
        startActivity(showMainActivity);
    }

}