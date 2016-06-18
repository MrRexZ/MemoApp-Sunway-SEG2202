package com.sunway.android.memoapp.controller;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.sunway.android.memoapp.util.BitmapOperation;
import com.sunway.android.memoapp.util.C;

import java.lang.ref.WeakReference;

/**
 * Created by Mr_RexZ on 6/18/2016.
 */
public class BitmapReadingWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    public String filePath = null;
    public int displayMode = 0;
    private int maxWidth;

    public BitmapReadingWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        filePath = params[0];
        displayMode = Integer.parseInt(params[1]);
        return BitmapOperation.decodeSampledBitmapFromFile(filePath, 200, 200);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (displayMode == C.DETAILS_ACTIVITY_DISPLAY) maxWidth = 500;
        else maxWidth = 200;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) height / width;
        float resizedHeight = aspectRatio * maxWidth;
        bitmap = BitmapOperation.getResizedBitmap(bitmap, maxWidth, (int) resizedHeight);

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }


}

