package com.sunway.android.memoapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sunway.android.memoapp.R;
import com.sunway.android.memoapp.controller.AlarmReceiver;
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class DrawingMemoActivity extends AppCompatActivity {

    private Intent intent;

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(new DrawingView(this,null));
        setContentView(R.layout.drawing_memo);
        final int memoID = getIntent().getExtras().getInt(C.TEXT_ID);

        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper_drawingmemo);
        setSupportActionBar(upper_toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom_drawingmemo);
        bottom_toolbar.inflateMenu(R.menu.bottom_drawingmemo_menu);
        final DrawingView drawingView = (DrawingView) findViewById(R.id.drawing_view_canvas);
        intent = getIntent();


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
                    if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.ADDDRAWING)) {
                        showMainActivity.putExtra(C.ACTION_MODE, C.ADDDRAWING);
                    } else if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.EDITDRAWING)) {

                        showMainActivity.putExtra(C.ACTION_MODE, C.EDITDRAWING);
                    }


                    boolean HAS_REMINDER = intent.getExtras().getBoolean(C.HAS_REMINDER);
                    boolean alarmUp = (PendingIntent.getBroadcast(getBaseContext(), memoID,
                            new Intent(getBaseContext(), AlarmReceiver.class),
                            PendingIntent.FLAG_NO_CREATE) != null);

                    if (HAS_REMINDER) {
                        Calendar targetCal = (Calendar) intent.getSerializableExtra(C.REMINDER_DETAILS);

                        IntentFilter filter = new IntentFilter();
                        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                                .putExtra(C.TEXT_ID, memoID)
                                .putExtra(C.INPUT_TITLE, getIntent().getExtras().getString(C.INPUT_TITLE))
                                .putExtra(C.INPUT_DETAILS, getIntent().getExtras().getString(C.INPUT_DETAILS))
                                .putExtra(C.ACTION_MODE, C.EDITDRAWING)
                                .putExtra(C.MEMO_TYPE, C.DRAWING_MEMO);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
                    } else if (alarmUp) {
                        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class)
                                .putExtra(C.TEXT_ID, memoID)
                                .putExtra(C.ACTION_MODE, C.EDITDRAWING);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), memoID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    }

                    startActivity(showMainActivity);

                }
                return false;
            }
        });
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

            saveDrawing(getIntent().getExtras().getInt(C.TEXT_ID));
            Intent showReminder = new Intent(this, ReminderActivity.class)
                    .putExtra(C.INPUT_TITLE, "Drawing memo reminder")
                    .putExtra(C.INPUT_DETAILS, "Click to view the drawing memo")
                    .putExtra(C.TEXT_ID, getIntent().getExtras().getInt(C.TEXT_ID))
                    .putExtra(C.PHOTOS, getIntent().getExtras().getInt(C.PHOTOS))
                    .putExtra(C.MEMO_TYPE, C.DRAWING_MEMO)
                    .putExtra(C.ACTION_MODE, getIntent().getExtras().getString(C.ACTION_MODE));
            startActivity(showReminder);
        }
        return super.onOptionsItemSelected(item);
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

}
