package com.sunway.android.memoapp.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.controller.AlarmReceiver;
import com.sunway.android.memoapp.model.MemoDrawingItem;
import com.sunway.android.memoapp.model.Reminder;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;
import com.sunway.android.memoapp.util.ListOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class DrawingMemoActivity extends AppCompatActivity {

    private final int REGISTER_REMINDER = 3;
    private Intent intent;
    private String ACTION_MODE;
    private int adapterPosition;
    private Calendar targetCal;
    private MemoDrawingItem memoDrawingItem;

    private DrawingView drawingView;

    private PopupWindow changeColorBrush;
    private Reminder tempReminder = new Reminder(0, 0, 0, 0, 0, 0);



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_memo);
        final int memoID = getIntent().getExtras().getInt(C.MEMO_ID);

        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper_drawingmemo);
        setSupportActionBar(upper_toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom_drawingmemo);
        bottom_toolbar.inflateMenu(R.menu.bottom_drawingmemo_menu);
        drawingView = (DrawingView) findViewById(R.id.drawing_view_canvas);
        intent = getIntent();
        ACTION_MODE = intent.getExtras().getString(C.ACTION_MODE);

        if (ACTION_MODE.equals(C.EDITDRAWING)) {
            memoDrawingItem = (MemoDrawingItem) ListOperation.getMemoItemFromID(memoID);
        }



        String filePath = "u_" + FileOperation.userID + "_drawing_" + memoID + ".jpg";
            File file = new File(FileOperation.mydir, filePath);
            if (file.exists()) {
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(file));
                    ImageView img = new ImageView(this);
                    img.setImageBitmap(b);
                    drawingView.addView(img);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_save_drawing) {

                    saveDrawing(memoID);
                    Intent showMainActivity = new Intent(DrawingMemoActivity.this, MainActivity.class);

                    int year = tempReminder.getYear();
                    int month = tempReminder.getMonth();
                    int day = tempReminder.getDay();
                    int hour = tempReminder.getHour();
                    int minute = tempReminder.getMinute();
                    int second = tempReminder.getSecond();



                    if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.ADDDRAWING)) {
                        showMainActivity.putExtra(C.ACTION_MODE, C.ADDDRAWING);
                    } else if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.EDITDRAWING)) {

                        Reminder selectedReminder = memoDrawingItem.getReminder();
                        int oldYear = selectedReminder.getYear();
                        int oldMonth = selectedReminder.getMonth();
                        int oldDay = selectedReminder.getDay();
                        int oldHour = selectedReminder.getHour();
                        int oldMinute = selectedReminder.getMinute();
                        int oldSecond = selectedReminder.getSecond();


                        if (targetCal == null) {
                            year = oldYear;
                            month = oldMonth;
                            day = oldDay;
                            hour = oldHour;
                            minute = oldMinute;
                            second = oldSecond;
                        }


                        FileOperation.replaceSelected(
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + (memoID) + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "Drawing" + FileOperation.DELIMITER_LINE
                                        + "reminder=" + oldYear + "," + oldMonth + "," + oldDay + "," + oldHour + "," + oldMinute + "," + oldSecond + FileOperation.DELIMITER_LINE,
                                FileOperation.DELIMITER_LINE + FileOperation.DELIMITER_UNIT + (memoID) + FileOperation.DELIMITER_UNIT + FileOperation.DELIMITER_LINE + "Drawing" + FileOperation.DELIMITER_LINE
                                        + "reminder=" + year + "," + month + "," + day + "," + hour + "," + minute + "," + second + FileOperation.DELIMITER_LINE
                        );
                        ListOperation.modifyDrawingList(memoID, year, month, day, hour, minute, second);

                    }




                    if (ACTION_MODE.equals(C.ADDDRAWING))
                        writeDrawingMemo(year, month, day, hour, minute, second);


                    if (targetCal != null) {
                        IntentFilter filter = new IntentFilter();
                        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
                    }


                    boolean alarmUp = (PendingIntent.getBroadcast(getBaseContext(), memoID,
                            new Intent(getBaseContext(), AlarmReceiver.class),
                            PendingIntent.FLAG_NO_CREATE) != null);

                    if (alarmUp) {
                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                                .putExtra(C.ACTION_MODE, C.EDITDRAWING)
                                .putExtra(C.MEMO_ID, memoID)
                                .putExtra(C.INPUT_TITLE, "Drawing memo reminder")
                                .putExtra(C.INPUT_DETAILS, "Click to view drawing")
                                .putExtra(C.MEMO_TYPE, C.DRAWING_MEMO);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    }

                    startActivity(showMainActivity);

                }
                return false;
            }
        });


        TextView backButton = (TextView) findViewById(R.id.action_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView selectBrushColor = (TextView) findViewById(R.id.action_select_color);
        selectBrushColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPopup(DrawingMemoActivity.this, new Point(300, 2100));
            }
        });
    }


    private void writeDrawingMemo(int year, int month, int day, int hour, int minute, int second) {

        try {
            FileOperation.writeDrawingMemo(year, month, day, hour, minute, second);
        } catch (Exception e) {
            System.out.println("Error writing to drawing memo:" + e.getMessage());
        }
        FileOperation.replaceSelected(
                FileOperation.DELIMITER_LINE + "counter=" + ((FileOperation.getMemoTextCountId()) - 1) + FileOperation.DELIMITER_LINE,
                FileOperation.DELIMITER_LINE + "counter=" + ((FileOperation.getMemoTextCountId())) + FileOperation.DELIMITER_LINE);

        memoDrawingItem = new MemoDrawingItem((FileOperation.getMemoTextCountId() - 1),
                new Reminder(year, month, day, hour, minute, second),
                ListOperation.DRAWING_MEMO_SORT_ORDER);
        ListOperation.addToList(memoDrawingItem);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.upper_drawingmemo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reminder) {

            Intent showReminder = new Intent(this, ReminderActivity.class)
                    .putExtra(C.MEMO_TYPE, C.DRAWING_MEMO);
            startActivityForResult(showReminder, REGISTER_REMINDER);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REGISTER_REMINDER:
                    targetCal = (Calendar) data.getSerializableExtra(C.REMINDER_DETAILS);
                    tempReminder = new Reminder(targetCal.get(Calendar.YEAR),
                            targetCal.get(Calendar.MONTH),
                            targetCal.get(Calendar.DAY_OF_MONTH),
                            targetCal.get(Calendar.HOUR),
                            targetCal.get(Calendar.MINUTE),
                            targetCal.get(Calendar.SECOND)
                    );
                    break;
            }
        }
    }

    private void saveDrawing(int memoID) {
        DrawingView drawingView = (DrawingView) findViewById(R.id.drawing_view_canvas);
        Bitmap returnedBitmap = Bitmap.createBitmap(drawingView.getWidth(), drawingView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        drawingView.draw(canvas);
        Bitmap bi = drawingView.getDrawingCache();
        String mDrawingName = "u_" + FileOperation.userID + "_drawing_" + memoID + ".jpg";
        File drawFile = new File(FileOperation.mydir, mDrawingName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(drawFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bi.compress(Bitmap.CompressFormat.PNG, 90, fos);
    }


    private void showColorPopup(final Activity context, Point p) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_drawing_popup_select_color, null);

        changeColorBrush = new PopupWindow(context);
        changeColorBrush.setContentView(layout);
        changeColorBrush.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeColorBrush.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeColorBrush.setFocusable(true);
        changeColorBrush.setAnimationStyle(android.R.style.Animation_Dialog);

        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        changeColorBrush.setBackgroundDrawable(new BitmapDrawable());

        changeColorBrush.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


        TextView black_color_selected = (TextView) layout.findViewById(R.id.select_brush_black);
        black_color_selected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                drawingView.setColor(Color.BLACK);
                changeColorBrush.dismiss();
            }
        });

        TextView white_color_selected = (TextView) layout.findViewById(R.id.select_brush_white);
        white_color_selected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.WHITE);
                changeColorBrush.dismiss();
            }
        });

        TextView blue_color_selected = (TextView) layout.findViewById(R.id.select_brush_blue);
        blue_color_selected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawingView.setColor(Color.BLUE);
                changeColorBrush.dismiss();
            }
        });


    }

}
