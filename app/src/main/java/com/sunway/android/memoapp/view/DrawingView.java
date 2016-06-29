package com.sunway.android.memoapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr_RexZ on 6/14/2016.
 */

public class DrawingView extends RelativeLayout {
    public Paint paint = new Paint();
    private Path path = new Path();
    private ArrayList<Path> paths = new ArrayList<Path>();
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
    private int newColor = -1;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
        for (Path p : paths) {
            paint.setColor(colorsMap.get(p));
            canvas.drawPath(p, paint);
        }

        paint.setColor(newColor);
        canvas.drawPath(path, paint);
        System.out.println("COUNT: " + paths.size());

    }


    public void setColor(int newColor) {
        colorsMap.put(path, paint.getColor());
        paths.add(path);

        path = new Path();
        paint.setColor(newColor);
        this.newColor = newColor;
        postInvalidate();
    }

    public Bitmap get() {
        return this.getDrawingCache();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

}

