package com.sunway.android.memoapp.view;

import android.content.Intent;
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
import com.sunway.android.memoapp.util.C;
import com.sunway.android.memoapp.util.FileOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */
public class DrawingMemoActivity extends AppCompatActivity {

    private DrawingView drawingView;

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
        final int memoid = getIntent().getExtras().getInt(C.TEXT_ID);
        Toolbar upper_toolbar = (Toolbar) findViewById(R.id.toolbar_upper_drawingmemo);
        setSupportActionBar(upper_toolbar);
        Toolbar bottom_toolbar = (Toolbar) findViewById(R.id.toolbar_bottom_drawingmemo);
        bottom_toolbar.inflateMenu(R.menu.bottom_drawingmemo_menu);
        final DrawingView drawingView = (DrawingView) findViewById(R.id.drawing_view_canvas);


        if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.EDITDRAWING)) {
            String filePath = "u_" + FileOperation.userID + "_drawing_" + memoid + ".jpg";
            File file = new File(FileOperation.mydir, filePath);
            if (file.exists()) {
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(file));
                    ImageView img = new ImageView(this);
                    img.setImageBitmap(b);
                    drawingView.addView(img);

                    System.out.println("the filepath demo is : " + img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
        bottom_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_save_drawing) {

                    Bitmap returnedBitmap = Bitmap.createBitmap(drawingView.getWidth(), drawingView.getHeight(), Bitmap.Config.ARGB_8888);
                    //Bind a canvas to it
                    Canvas canvas = new Canvas(returnedBitmap);
                    drawingView.draw(canvas);

                    Bitmap bi = drawingView.getDrawingCache();


                    String mDrawingName = "u_" + FileOperation.userID + "_drawing_" + memoid + ".jpg";
                    File drawFile = new File(FileOperation.mydir, mDrawingName);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(drawFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bi.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    Intent showMainActivity = new Intent(DrawingMemoActivity.this, MainActivity.class);
                    if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.ADDDRAWING)) {
                        showMainActivity.putExtra(C.ACTION_MODE, C.ADDDRAWING);
                    } else if (getIntent().getExtras().getString(C.ACTION_MODE).equals(C.EDITDRAWING)) {

                        showMainActivity.putExtra(C.ACTION_MODE, C.EDITDRAWING);
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
        if (id == R.id.action_save_drawing) {

        }

        return super.onOptionsItemSelected(item);
    }
}
